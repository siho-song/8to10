import ArrowBackIcon from "@mui/icons-material/ArrowBack";
import {useEffect, useState} from "react";
import authenticatedApi from "@/api/AuthenticatedApi.js";
import {API_ENDPOINT_NAMES} from "@/constants/ApiEndPoints.js";
import {useNavigate} from "react-router-dom";
import PropTypes from "prop-types";

const MyBoards = ({onBack, closeModal}) => {
    const [posts, setPosts] = useState([]);
    const navigate = useNavigate();

    useEffect(() => {
        const loadMyPosts = async () => {
            try {
                const url = "/mypage/boards";
                const response = await authenticatedApi.get(
                    url,
                    {apiEndPoint: API_ENDPOINT_NAMES.GET_MY_POSTS,},
                );
                setPosts(response.data.items);
            } catch (error) {
                console.error(error.toString());
                console.error(error);
            }
        };
        loadMyPosts();
    }, []);

    const handlePostClick = (url) => {
        navigate(url);
        closeModal();
    }

    return (
        <div className="mypage-section">
            <div className="section-header">
                <button className="back-button" onClick={onBack}>
                    <ArrowBackIcon/> 뒤로
                </button>
                <h2>내가 쓴 게시글</h2>
            </div>
            <div className="myposts-container">
                {posts.length === 0 ? (
                    <div className="myposts-empty">
                        <p>작성한 글이 없습니다.</p>
                    </div>
                ) : (
                    <ul className="myposts-list">
                        {posts.map((post) => (
                            <button
                                key={post.boardId}
                                className="mypost-link"
                                onClick={() => handlePostClick(`/community/post/${post.boardId}`)}
                            >
                                <h3 className="mypost-title">{post.title}</h3>
                                <div className="mypost-meta">
                                    <span className="mypost-likes">좋아요: {post.totalLike}</span>
                                    <span className="mypost-scraps">스크랩: {post.totalScrap}</span>
                                    <span className="mypost-date">
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

MyBoards.propTypes = {
    onBack: PropTypes.func.isRequired,
    closeModal: PropTypes.func.isRequired,
}

export default MyBoards;