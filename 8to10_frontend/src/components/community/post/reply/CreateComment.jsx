import {useState} from "react";
import PropTypes from "prop-types";
import authenticatedApi from "@/api/AuthenticatedApi.js";
import {API_ENDPOINT_NAMES} from "@/constants/ApiEndPoints.js";
import {useNavigate} from "react-router-dom";

function CreateComment({ id, onCommentSubmit }) {

    const navigate = useNavigate();

    const [bodyData, setBodyData] = useState({
        postId : id,
        parentId : null,
        contents : '',
    });

    const handleContentChange = (e) => {
        const value = e.target.value;
        setBodyData({
            ...bodyData,
            contents: value,
        });
    }

    const handleCommentSubmit = async () => {
        try {
            const url = "/community/reply/save";
            const response = await authenticatedApi.post(
                url,
                bodyData,
                {
                    apiEndPoint: API_ENDPOINT_NAMES.CREATE_REPLY,
                }
            );
            // const data = response.data;
            // console.log("data : " + data);
            // const newComment = {
            //     id: data.replyId,
            //     contents: data.contents,
            //     createdAt: data.createdAt,
            //     updatedAt: data.updatedAt,
            //     nickname: data.nickname,
            //     writer: data.writer,
            //     parentId: data.parentId,
            //     totalLike: 0,
            // }
            //
            // onCommentSubmit(newComment);
            // setBodyData({...bodyData, contents: ''});
            navigate(0);
        }
        catch (error) {
            console.error("Error : \n", error.toString());
            console.error(error);
        }
    };


    return (
        <div>
            <form className="create-comment" onSubmit={(e) => {
                e.preventDefault();
                handleCommentSubmit();
            }}>
                <input
                    type="text"
                    id="comment-input"
                    placeholder="댓글을 입력하세요."
                    name="contents"
                    value={bodyData.contents}
                    onChange={handleContentChange}
                />
                <button type="submit" id="submit-comment">댓글 등록</button>
            </form>
        </div>
    );
}

CreateComment.propTypes = {
    id: PropTypes.number.isRequired,
    onCommentSubmit: PropTypes.func.isRequired,
}

export default CreateComment;