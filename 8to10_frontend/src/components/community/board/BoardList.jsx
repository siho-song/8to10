import { useNavigate } from 'react-router-dom';

import PropTypes from "prop-types";

const BoardList = ({ posts }) => {
    const navigate = useNavigate();

    if (!posts || posts.length === 0) {
        return <p>게시글이 없습니다.</p>;
    }

    return (
        <div className="board-list">
            {posts.map(post => (
                <div key={post.id} className="post" onClick={() => navigate(`/community/post/${post.id}`)}>
                    <h3>{post.title}</h3>
                    <div>
                        <p>{post.nickname}</p>
                        <p>좋아요: {post.totalLike} 스크랩: {post.totalScrap}</p>
                        <p>{new Date(post.createdAt).toLocaleDateString()}</p>
                    </div>
                </div>
            ))}
        </div>
    );
};

BoardList.propTypes = {
    posts: PropTypes.arrayOf(
        PropTypes.shape({
            id: PropTypes.number.isRequired,
            nickname: PropTypes.string.isRequired,
            createdAt: PropTypes.string.isRequired,
            updatedAt: PropTypes.string.isRequired,
            writer: PropTypes.string.isRequired,
            title: PropTypes.string.isRequired,
            contents: PropTypes.string.isRequired,
            totalLike: PropTypes.number.isRequired,
            totalScrap: PropTypes.number.isRequired,
        })
    )

};

export default BoardList;
