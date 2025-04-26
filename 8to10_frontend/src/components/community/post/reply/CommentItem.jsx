import {useEffect, useRef, useState} from "react";
import PropTypes from "prop-types";

import "@/styles/community/Board.css";

import ReplyItem from "@/components/community/post/reply/ReplyItem.jsx";
import {formatDateTime} from "@/helpers/TimeFormatter.js";
import authenticatedApi from "@/api/AuthenticatedApi.js";
import {API_ENDPOINT_NAMES} from "@/constants/ApiEndPoints.js";
import {useLocation, useNavigate} from "react-router-dom";

function CommentItem({ postId, email, reply, replies, likedReplyIds, onReplySubmit, onCommentDelete, onReplyDelete, focusedCommentId }) {

    const commentRef = useRef(null);
    const location = useLocation();
    const navigate = useNavigate();
    const [comment, setComment] = useState(reply);

    const [replyForm, setReplyForm] = useState({
        postId: postId,
        parentId: reply.id,
        contents: "",
    });

    const [commentEditForm, setCommentEditForm] = useState({
        id: reply.id,
        contents: reply.contents,
    });

    const [showReplyInput, setShowReplyInput] = useState(false);
    const childReplies = replies.filter(r => r.parentId === reply.id);

    const [hasLike, setHasLike] = useState(likedReplyIds.includes(reply.id));
    const [totalLike, setTotalLike] = useState(reply.totalLike);

    const [isCommentEditMode, setIsCommentEditMode] = useState(false);


    const handleReplyInput = (e) => {
        const value = e.target.value;
        setReplyForm({
            ...replyForm,
            contents: value,
        });
    };

    const resetReplyInput = () => {
        setReplyForm({
            ...replyForm,
            contents: '',
        });
    }

    const handleReplySubmit = async() => {
        try {
            const url = "/community/reply/save";
            const response = await authenticatedApi.post(
                url,
                replyForm,
                {
                    apiEndPoint: API_ENDPOINT_NAMES.CREATE_REPLY
                }
            );
            // const data = response.data;
            //
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
            // onReplySubmit(newComment);
            // resetReplyInput();
            // setShowReplyInput(false);
            navigate(0);
        } catch (error) {
            console.error("Error : \n", error.toString());
            console.error(error);
        }
    }

    const handleCommentLikeSubmit = async () => {
        try {
            const url = `/community/reply/${reply.id}/heart`;
            hasLike ? await authenticatedApi.delete(url,
                {
                    apiEndPoint: API_ENDPOINT_NAMES.DELETE_REPLY_LIKE,
                }) : await authenticatedApi.post(
                url,
                {},
                {
                    apiEndPoint: API_ENDPOINT_NAMES.CREATE_REPLY_LIKE,
            });

            setTotalLike(!hasLike ? (totalLike + 1) : (totalLike - 1));
            setHasLike(!hasLike);
        } catch (error) {
            console.error("Error : \n", error.toString());
            console.error(error);
        }
    }

    const handleCommentEditInput = (e) => {
        const value = e.target.value;
        setCommentEditForm((prevData) => ({
            ...prevData,
            contents: value,
        }))
    }

    const resetCommentEditInput = () => {
        setCommentEditForm((prevData) => ({
            ...prevData,
            contents: comment.contents,
        }))
    }

    const handleCommentEditSubmit = async () => {
        try {
            const url = "/community/reply";
            const response = await authenticatedApi.put(
                url,
                commentEditForm,
                {
                    apiEndPoint: API_ENDPOINT_NAMES.EDIT_REPLY,
                },
            );
            const data = response.data;

            setComment(()=>({...data}));
        } catch (error) {
            console.error("Error : \n", error.toString());
            console.error(error);
        }
    }

    const handleCommentDelete = async () => {
        try {
            const url = `/community/reply/${reply.id}`;
            await authenticatedApi.delete(
                url,
                {
                apiEndPoint: API_ENDPOINT_NAMES.DELETE_REPLY,
            });

            onCommentDelete(reply.id);
        } catch (error) {
            console.error("Error : \n", error.toString());
            console.error(error);
        }
    }

    useEffect(() => {
    }, [showReplyInput, comment]);

    useEffect(() => {
        // const focusId = location.state?.relatedEntityId;
        // if (focusId === comment.id) {
        let timeoutId = -1;
        if (focusedCommentId === comment.id) {
            commentRef.current.scrollIntoView({ behavior: "smooth", block: "center" });
            commentRef.current.classList.add("focused");
            const timeout = setTimeout(() => commentRef.current.classList.remove("focused"), 5000);
            timeoutId = timeout
            // navigate(location.pathname, { replace: true, state: null });
        }

        return () => clearTimeout(timeoutId);
    // }, [location])
    }, [focusedCommentId])

    return (
        <div className="comment" id={`comment-${comment.id}`} ref={commentRef}>
            { !isCommentEditMode ? (
                <div className="comment-header" ref={commentRef}>
                    <div className="comment-profile">
                        <span className="comment-author">{comment.nickname}</span>
                        {email === comment.writer &&
                            <div id="edit-delete-controls" className="edit-delete-controls">
                                <button
                                    className="comment-edit-button"
                                    onClick={() => {
                                        setIsCommentEditMode(true);
                                    }}
                                >수정
                                </button>
                                <button
                                    className="comment-delete-button"
                                    onClick={handleCommentDelete}
                                >삭제</button>
                            </div>
                        }
                    </div>
                    <span className="post-date">{formatDateTime(comment.createdAt)}</span>
                    <span className="comment-text">{comment.contents}</span>

                    <div className="comment-actions">
                        <button className="reply-button" onClick={() => {
                            setShowReplyInput(!showReplyInput);
                        }}>
                            대댓글 달기
                        </button>
                        <button className={hasLike ? "comment-like-button active" : "comment-like-button"}
                                data-liked={hasLike}
                                onClick={handleCommentLikeSubmit}
                        >
                            <span className="replyHeart">좋아요 {totalLike}</span>
                        </button>
                    </div>
                </div>

            ) : (
                <div>
                    <form className="create-comment" onSubmit={(e) => {
                        e.preventDefault();
                        handleCommentEditSubmit();
                        setIsCommentEditMode(false);
                    }}>
                        <input
                            type="text"
                            id="comment-input"
                            placeholder="댓글을 입력하세요."
                            name="contents"
                            value={commentEditForm.contents}
                            onChange={handleCommentEditInput}
                        />
                        <button type="submit" id="submit-comment">댓글 수정</button>
                        <button className="hide-reply-section" onClick={() => {
                            setIsCommentEditMode(false);
                            resetCommentEditInput();
                        }}>
                            취소
                        </button>
                    </form>
                </div>
            )}

            {showReplyInput && (
                <form
                    className="reply-section"
                    onSubmit={(e) => {
                        e.preventDefault();
                        handleReplySubmit();
                    }}
                >
                    <input
                        type="text"
                        className="reply-input"
                        placeholder="덧글을 입력하세요"
                        name="contents"
                        value={replyForm.contents}
                        onChange={handleReplyInput}
                    />
                    <button type="submit" className="reply-submit">덧글 등록</button>
                    <button className="hide-reply-section" onClick={() => {
                        setShowReplyInput(false)
                        resetReplyInput();
                    }}>취소
                    </button>
                </form>
            )}

            <div className="replies-container">
                {childReplies.map(childReply => (
                    <ReplyItem
                        key={childReply.id}
                        email={email}
                        reply={childReply}
                        likedReplyIds={likedReplyIds}
                        onReplyDelete={onReplyDelete}
                        focusedCommentId={focusedCommentId}
                    />
                ))}
            </div>
        </div>
    );
}


CommentItem.propTypes = {
    postId: PropTypes.number.isRequired,
    email: PropTypes.string.isRequired,
    reply: PropTypes.shape({
        id: PropTypes.number,
        contents: PropTypes.string.isRequired,
        createdAt: PropTypes.string.isRequired,
        updatedAt: PropTypes.string.isRequired,
        nickname: PropTypes.string.isRequired,
        writer: PropTypes.string.isRequired,
        parentId: PropTypes.number,
        totalLike: PropTypes.number.isRequired,
    }).isRequired,
    replies: PropTypes.arrayOf(
        PropTypes.shape({
            id: PropTypes.number.isRequired,
            contents: PropTypes.string.isRequired,
            createdAt: PropTypes.string.isRequired,
            updatedAt: PropTypes.string.isRequired,
            nickname: PropTypes.string.isRequired,
            writer: PropTypes.string.isRequired,
            parentId: PropTypes.number,
            totalLike: PropTypes.number.isRequired,
        }).isRequired,
    ).isRequired,
    likedReplyIds: PropTypes.arrayOf(PropTypes.number),
    onReplySubmit: PropTypes.func.isRequired,
    onCommentDelete: PropTypes.func.isRequired,
    onReplyDelete: PropTypes.func.isRequired,
    focusedCommentId: PropTypes.number,
}

export default CommentItem;