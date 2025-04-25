import {useEffect, useState} from 'react';
import {useLocation, useNavigate, useParams} from 'react-router-dom';
import PostItem from "@/components/community/post/PostItem.jsx";
import CreateComment from "@/components/community/post/reply/CreateComment.jsx";
import CommentSection from "@/components/community/post/reply/CommentSection.jsx";
import {useAuth} from "@/context/auth/UseAuth.jsx";
import authenticatedApi from "@/api/AuthenticatedApi.js";
import {API_ENDPOINT_NAMES} from "@/constants/ApiEndPoints.js";

const PostView = () => {
    const location = useLocation();
    const navigate = useNavigate();
    const { email } = useAuth();
    const [post, setPost] = useState(location.state?.post || null);
    const [responseReplies, setResponseReplies] = useState([]);
    const [responseLikedReplyIds, setResponseLikedReplyIds] = useState([]);
    const [focusedCommentId, setFocusedCommentId] = useState(null);

    const { id } = useParams();

    const addNewComment = (newComment) => {
        setResponseReplies((prevData) => [...prevData, newComment]);
        setFocusedCommentId(newComment.id);
    }
    useEffect(() => {
        const loadPostData = async () => {
            try {
                const url = `/community/post/${id}`;
                const response = await authenticatedApi.get(
                    url,
                    {
                        apiEndPoint: API_ENDPOINT_NAMES.GET_POST,
                });
                const data = response.data;

                const { replies, likedReplyIds, ...restOfPost } = data;

                setPost(restOfPost);
                setResponseReplies(replies);
                setResponseLikedReplyIds(likedReplyIds);

            } catch (error) {
                console.error("Error : \n", error.toString());
                console.error(error);
            }
        }

        if (!location.state?.post) {
            loadPostData();
        } else {
            navigate(location.pathname, { replace: true, state: null });
        }

        if (location.state?.relatedEntityId) {
            setFocusedCommentId(location.state.relatedEntityId);
            navigate(location.pathname, { replace: true, state: null });
        }
    },[id]);

    if (!post) {
        return ("게시물을 불러오는 중입니다.");
    }

    const deleteReplyById = (replyId) => {
        setResponseReplies((prevReplies) =>
            prevReplies.filter((reply) => reply.id !== replyId)
        );
    };

    const deleteCommentById = (replyId) => {
        setResponseReplies((prevComments) =>
            prevComments.filter((reply) => reply.id !== replyId && reply.parentId !== replyId)
        );
    };

    return (
        <div className="post-container">
            <div className="post-main-content">
                <div className="board-header">
                    <div className="board-header-top">
                        <h1 id="board-title">커뮤니티</h1>
                        <button
                            id="board-header-top"
                            onClick={() => {
                                navigate("/community/board")
                            }}>글 목록
                        </button>
                    </div>
                </div>

                <PostItem
                    post={post}
                    email={email}
                />
                <CreateComment
                    id={post.id}
                    onCommentSubmit={addNewComment}
                />
                <CommentSection
                    postId={post.id}
                    email={email}
                    replies={responseReplies}
                    likedReplyIds={responseLikedReplyIds}
                    onReplySubmit={addNewComment}
                    onCommentDelete={deleteCommentById}
                    onReplyDelete={deleteReplyById}
                    focusedCommentId={focusedCommentId}
                />
            </div>

        </div>
    );
};

export default PostView;
