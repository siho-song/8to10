import PropTypes from "prop-types";
import CommentItem from "@/components/community/post/reply/CommentItem.jsx";

function CommentSection({ postId, email, replies, likedReplyIds, onReplySubmit, onCommentDelete, onReplyDelete, focusedCommentId }) {

    return (
        <div id="comment-container">
            {replies.filter(reply => !reply.parentId)
                    .map(reply => (
                        <CommentItem
                            key={reply.id}
                            email={email}
                            postId={postId}
                            reply={reply}
                            replies={replies}
                            likedReplyIds={likedReplyIds}
                            onReplySubmit={onReplySubmit}
                            onCommentDelete={onCommentDelete}
                            onReplyDelete={onReplyDelete}
                            focusedCommentId={focusedCommentId}
                        />
                    ))}
        </div>
    );
}


CommentSection.propTypes = {
    postId: PropTypes.number.isRequired,
    email: PropTypes.string.isRequired,
    replies: PropTypes.arrayOf(
        PropTypes.shape({
            id: PropTypes.number,
            contents: PropTypes.string,
            createdAt: PropTypes.string,
            updatedAt: PropTypes.string,
            nickname: PropTypes.string,
            writer: PropTypes.string,
            parentId: PropTypes.number,
            totalLike: PropTypes.number,
         })
    ),
    likedReplyIds: PropTypes.arrayOf(PropTypes.number),
    onReplySubmit: PropTypes.func.isRequired,
    onCommentDelete: PropTypes.func.isRequired,
    onReplyDelete: PropTypes.func.isRequired,
    focusedCommentId: PropTypes.number,
}

export default CommentSection;