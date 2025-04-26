import {useEffect, useState} from 'react';
import {useLocation, useNavigate, useParams} from "react-router-dom";

import PropTypes from "prop-types";
import authenticatedApi from "@/api/AuthenticatedApi.js";
import {API_ENDPOINT_NAMES} from "@/constants/ApiEndPoints.js";

function CreateOrEditPost({ isEditMode }) {
    const [title, setTitle] = useState('');
    const [contents, setContents] = useState('');

    const navigate = useNavigate();
    const id = useParams();

    const location = useLocation();

    const handleSubmit = async (e) => {
        e.preventDefault();

        const postData = {
            title: title,
            contents: contents,
        };

        const updatePostData = {
            id: id.postId,
            title: title,
            contents: contents,
        }

        if (!title || !contents) {
            alert('제목과 내용을 입력해주세요');
            return;
        }

        try {

            const url = isEditMode ? `/community/post` : `/community/post/save`;
            const response = isEditMode ? await authenticatedApi.put(
                url,
                updatePostData,
                {
                    apiEndPoint: API_ENDPOINT_NAMES.EDIT_POST,
                }) : await authenticatedApi.post(
                    url,
                    postData,
                {
                    apiEndPoint: API_ENDPOINT_NAMES.CREATE_POST,
                });

            if (isEditMode) {
                navigate(`/community/post/${id.postId}`);
            } else {
                // const newPost = {
                //     ...data,
                //     hasLike: false,
                //     hasScrap: false,
                // }
                // navigate(`/community/post/${data.id}`, { state: {post:newPost} });
                navigate(`/community/board`);
            }
        } catch (error) {
            console.error("Error : \n", error.toString());
            console.error(error);
        }

    };

    useEffect(() => {
        if (isEditMode && id && location.state) {
            const { title: initialTitle, contents: initialContents } = location.state;
            setTitle(initialTitle || '');
            setContents(initialContents || '');
        }
    }, []);

    return (
        <div className="create-post-container">
            <div className="create-post-content">
                <div className="board-header">
                    <div className="board-header-top">
                        <h1>{isEditMode ? "게시글 수정" : "게시글 작성"}</h1>
                        <button
                            className="view-list-btn"
                            onClick={() => navigate("/community/board")}>
                            목록보기
                        </button>
                    </div>
                </div>
                <form className="post-form" onSubmit={handleSubmit}>
                    <div className="post-form-group">
                        <label htmlFor="post-title">글 제목</label>
                        <input
                            type="text"
                            placeholder="글 제목을 입력하세요"
                            value={title}
                            onChange={(e) => setTitle(e.target.value)}
                        />
                        <span className="error-message" id="title-error"></span>
                    </div>

                    <div className="post-form-group">
                        <label htmlFor="post-content">글 내용</label>
                        <textarea
                            rows="10"
                            placeholder="내용을 입력하세요"
                            value={contents}
                            onChange={(e) => setContents(e.target.value)}
                        />
                        <span className="post-error-message" id="content-error"></span>
                    </div>

                    <button className="post-submit-btn" type="submit">등록</button>
                </form>
            </div>

        </div>
    );
}


CreateOrEditPost.propTypes = {
    isEditMode: PropTypes.bool,
}

export default CreateOrEditPost;
