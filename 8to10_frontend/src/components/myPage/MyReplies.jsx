import ArrowBackIcon from "@mui/icons-material/ArrowBack";
import {useEffect, useState} from "react";
import {useNavigate} from "react-router-dom";
import PropTypes from "prop-types";
import {API_ENDPOINT_NAMES} from "@/constants/ApiEndPoints.js";
import authenticatedApi from "@/api/AuthenticatedApi.js";

const MyReplies = ({onBack, closeModal}) => {
    const [replies, setReplies] = useState([]);
    const navigate = useNavigate();

    useEffect(() => {
        const loadMyReplies = async () => {
            try {
                const url = "/mypage/replies";
                const response = await authenticatedApi.get(
                    url,
                    {apiEndPoint: API_ENDPOINT_NAMES.GET_MY_REPLIES,},
                );
                setReplies(response.data.items);
            } catch (error) {
                console.error(error.toString());
                console.error(error);
            }
        };
        loadMyReplies();
    }, []);

    const handleReplyClick = (url, relatedEntityId) => {
        closeModal();
        navigate(url, {state:{relatedEntityId: relatedEntityId}});
    };

    return (
        <div className="mypage-section">
            <div className="section-header">
                <button className="back-button" onClick={onBack}>
                    <ArrowBackIcon/> 뒤로
                </button>
                <h2>내가 쓴 댓글</h2>
            </div>
            <div className="myreplies-container">
                {replies.length === 0 ? (
                    <div className="myreplies-empty">
                        <p>작성한 댓글이 없습니다.</p>
                    </div>
                ) : (
                    <ul className="myreplies-list">
                        {replies.map((reply) => (
                            <button
                                key={reply.replyId}
                                className="myreply-link"
                                onClick={() => handleReplyClick(`/community/post/${reply.boardId}`, reply.replyId)}
                            >
                                <div className="myreply-content">
                                    <p className="myreply-contents">
                                        {reply.contents.length > 50
                                            ? reply.contents.slice(0, 50) + "..."
                                            : reply.contents}
                                    </p>
                                    <div className="myreply-meta">
                                        <span className="myreply-likes">좋아요: {reply.totalLike}</span>
                                        <span className="myreply-date">
                                            {new Date(reply.createdAt).toLocaleDateString()}
                                        </span>
                                    </div>
                                </div>
                            </button>
                        ))}
                    </ul>
                )}
            </div>
        </div>
    );
};

MyReplies.propTypes = {
    onBack: PropTypes.func.isRequired,
    closeModal: PropTypes.func.isRequired,
}

export default MyReplies;