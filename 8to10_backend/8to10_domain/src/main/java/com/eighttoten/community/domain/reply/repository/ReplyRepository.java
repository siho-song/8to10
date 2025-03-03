package com.eighttoten.community.domain.reply.repository;

import com.eighttoten.community.domain.reply.NewReply;
import com.eighttoten.community.domain.reply.Reply;
import com.eighttoten.community.domain.reply.ReplyWithPost;
import java.util.List;
import java.util.Optional;

public interface ReplyRepository {
    long save(NewReply newReply);
    void update(Reply reply);
    void deleteById(Long id);
    void deleteByReplyIds(List<Long> ids);
    Optional<Reply> findById(Long id);
    Optional<ReplyWithPost> findByIdWithPost(Long id);
    List<Reply> findAllByPostId(Long postId);
    List<Reply> findAllByMemberId(Long memberId);
    List<Reply> findAllByParentId(Long parentReplyId);
}