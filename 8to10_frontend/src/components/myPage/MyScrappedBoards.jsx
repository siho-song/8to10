import {useEffect, useState} from "react";
import authenticatedApi from "@/api/AuthenticatedApi.js";
import {API_ENDPOINT_NAMES} from "@/constants/ApiEndPoints.js";
import {useNavigate} from "react-router-dom";
import PropTypes from "prop-types";
import ArrowBackIcon from "@mui/icons-material/ArrowBack";

const MyScrappedBoards = ({onBack, closeModal}) => {
    const [scrappedBoards, setScrappedBoards] = useState([]);
    const navigate = useNavigate();

    useEffect(() => {
        const loadScrappedBoards = async () => {
            try {
                const url = "/mypage/scrapped-posts";
                const response = await authenticatedApi.get(
                    url,
                    {apiEndPoint: API_ENDPOINT_NAMES.GET_MY_SCRAPPED_BOARDS,},
                );
                setScrappedBoards(response.data.items);
            } catch (error) {
                console.error(error.toString());
                console.error(error);
            }
        };
        loadScrappedBoards();
    }, []);

    const handlePostClick = (url) => {
        closeModal();
        navigate(url);
    };

    return (
        <div className="mypage-section">
            <div className="section-header">
                <button className="back-button" onClick={onBack}>
                    <ArrowBackIcon/> 뒤로
                </button>
                <h2>스크랩한 게시물</h2>
            </div>
            <div className="scrapped-posts-container">
                {scrappedBoards.length === 0 ? (
                    <div className="scrapped-posts-empty">
                        <p>스크랩한 게시물이 없습니다.</p>
                    </div>
                ) : (
                    <ul className="scrapped-posts-list">
                        {scrappedBoards.map((post) => (
                            <button
                                key={post.boardId}
                                className="scrapped-post-link"
                                onClick={() => handlePostClick(`/community/post/${post.boardId}`)}
                            >
                                <h3 className="scrapped-post-title">{post.title}</h3>
                                <div className="scrapped-post-meta">
                                    <span className="scrapped-post-likes">좋아요: {post.totalLike}</span>
                                    <span className="scrapped-post-date">
                                        {new Date(post.createdAt).toLocaleDateString()}
                                    </span>
                                </div>
                            </button>
                        ))}
                    </ul>
                )}
            </div>
        </div>
    );
};

MyScrappedBoards.propTypes = {
    onBack: PropTypes.func.isRequired,
    closeModal: PropTypes.func.isRequired,
};

export default MyScrappedBoards;
