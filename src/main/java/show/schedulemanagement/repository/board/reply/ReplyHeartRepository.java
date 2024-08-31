package show.schedulemanagement.repository.board.reply;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import show.schedulemanagement.domain.board.Board;
import show.schedulemanagement.domain.board.BoardHeart;
import show.schedulemanagement.domain.board.reply.Reply;
import show.schedulemanagement.domain.board.reply.ReplyHeart;
import show.schedulemanagement.domain.member.Member;

public interface ReplyHeartRepository extends JpaRepository<ReplyHeart, Long> {

    @Modifying
    @Query("delete from ReplyHeart rh where rh.reply in :replies")
    void deleteByReplies(@Param(value = "replies") List<Reply> replies);

    @Modifying
    @Query("delete from ReplyHeart rh where rh.reply = :reply")
    void deleteByReply(@Param(value = "reply") Reply reply);

    boolean existsReplyHeartByMemberAndReply(Member member, Reply reply);
    Optional<ReplyHeart> findByMemberAndReply(Member member, Reply reply);
}