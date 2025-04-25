import {useNavigate} from "react-router-dom";
import PropTypes from "prop-types";

import {formatDateTime} from "@/helpers/TimeFormatter.js";
import {useState} from "react";
import authenticatedApi from "@/api/AuthenticatedApi.js";
import {API_ENDPOINT_NAMES} from "@/constants/ApiEndPoints.js";

function PostItem({ post, email }) {

    const navigate = useNavigate();

    const [hasLike, setHasLike] = useState(post.hasLike);
    const [totalLikes, setTotalLikes] = useState(post.totalLike);
    const [hasScrap, setHasScrap] = useState(post.hasScrap);
    const [totalScraps, setTotalScraps] = useState(post.totalScrap);

    const handleLikeClick = async () => {
        try {
            const url = `/community/post/${post.id}/heart`;
            hasLike ? await authenticatedApi.delete(
                url,
                {
                    apiEndPoint: API_ENDPOINT_NAMES.DELETE_POST_LIKE,
            }) : await authenticatedApi.post(
                url,
                {},
                {
                apiEndPoint: API_ENDPOINT_NAMES.CREATE_POST_LIKE,
            });

            setTotalLikes(!hasLike ? (totalLikes + 1) : (totalLikes - 1));
            setHasLike(!hasLike);
        } catch (error) {
            console.error("Error : \n", error.toString());
            console.error(error);
        }
    }

    const handleScrapClick = async () => {
        try {
            const url = `/community/post/${post.id}/scrap`;
            hasScrap ? await authenticatedApi.delete(
                url,
                {
                    apiEndPoint: API_ENDPOINT_NAMES.DELETE_POST_SCRAP,
                }) : await authenticatedApi.post(
                    url,
                {},
                {
                    apiEndPoint: API_ENDPOINT_NAMES.CREATE_POST_SCRAP,
            });

            setTotalScraps(!hasScrap ? (totalScraps + 1) : (totalScraps - 1));
            setHasScrap(!hasScrap);
        } catch (error) {
            console.error("Error : \n", error.toString());
            console.error(error);
        }
    }

    const handlePostDelete = async () => {
        try {
            const url = `/community/post/${post.id}`;
            await authenticatedApi.delete(
                url,
                {
                    apiEndPoint: API_ENDPOINT_NAMES.DELETE_POST,
            });

            navigate("/community/board");
        } catch (error) {
            console.error("Error : \n", error.toString());
            console.error(error);
        }
    }

    return (
        <div className="post-content">
            <div className="post-detail">
                <div className="post-header">
                    <h3 className="post-title">{post.title}</h3>
                    <div className="post-header-button">
                        {email === post.writer &&
                            <div id="edit-delete-controls" className="edit-delete-controls">
                                <button onClick={() => {
                                    navigate(`/community/post/edit/${post.id}`, {
                                        state: {
                                            title: post.title,
                                            contents: post.contents,
                                        }
                                    })
                                }}>수정
                                </button>
                                <button
                                    className="delete-button"
                                    onClick={handlePostDelete}
                                >삭제
                                </button>
                            </div>
                        }
                    </div>
                </div>

                <div className="post-info">
                    <div className="post-author-date">
                        <span className="post-author">{post.nickname}</span>
                        <span className="post-date">{formatDateTime(post.createdAt)}</span>
                    </div>
                </div>

                <hr/>

                <p className="post-body">
                    {post.contents.split("\n").map((line, index) => (
                        <span key={index}>
                            {line}
                            <br/>
                        </span>
                    ))}
                </p>


                <div className="post-stats">
                    <div className="post-likes">
                        <button
                            id="like-button"
                            className={hasLike ? "like-button active" : "like-button"}
                            data-liked={hasLike}
                            onClick={handleLikeClick}>
                            <span className="heart">좋아요 {totalLikes}</span>
                        </button>
                    </div>
                    <div className="post-scraps">
                        <button
                            id="scrap-button"
                            className={hasScrap ? "scrap-button active" : "scrap-button"}
                            data-scraped={hasScrap}
                            onClick={handleScrapClick}>

                            <span className="scrap">스크랩 {totalScraps}</span>
                            {/*<span id="scrap-count">{totalScraps}</span>*/}
                        </button>
                    </div>
                </div>
            </div>
        </div>


    );
}

PostItem.propTypes = {
    post: PropTypes.shape({
        id: PropTypes.number.isRequired,
        title: PropTypes.string.isRequired,
        contents: PropTypes.string.isRequired,
        createdAt: PropTypes.string.isRequired,
        updatedAt: PropTypes.string.isRequired,
        writer: PropTypes.string.isRequired,
        nickname: PropTypes.string.isRequired,
        totalLike: PropTypes.number.isRequired,
        totalScrap: PropTypes.number.isRequired,
        hasLike: PropTypes.bool.isRequired,
        hasScrap: PropTypes.bool.isRequired,
    }).isRequired,
    email: PropTypes.string.isRequired,
}

export default PostItem;