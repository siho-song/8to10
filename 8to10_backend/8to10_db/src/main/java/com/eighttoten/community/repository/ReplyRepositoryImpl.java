package com.eighttoten.community.repository;

import com.eighttoten.community.PostEntity;
import com.eighttoten.community.ReplyEntity;
import com.eighttoten.community.domain.reply.NewReply;
import com.eighttoten.community.domain.reply.Reply;
import com.eighttoten.community.domain.reply.ReplyWithPost;
import com.eighttoten.community.domain.reply.repository.ReplyRepository;
import com.eighttoten.community.repository.post.PostJpaRepository;
import com.eighttoten.exception.ExceptionCode;
import com.eighttoten.exception.NotFoundEntityException;
import com.eighttoten.member.MemberEntity;
import com.eighttoten.member.repository.MemberJpaRepository;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ReplyRepositoryImpl implements ReplyRepository {
    private final ReplyJpaRepository replyRepository;
    private final MemberJpaRepository memberRepository;
    private final PostJpaRepository postRepository;

    @Override
    public long save(NewReply newReply) {
        MemberEntity memberEntity = memberRepository.findById(newReply.getMemberId())
                .orElseThrow(() -> new NotFoundEntityException(ExceptionCode.NOT_FOUND_MEMBER));

        PostEntity postEntity = postRepository.findById(newReply.getPostId())
                .orElseThrow(() -> new NotFoundEntityException(ExceptionCode.NOT_FOUND_POST));

        return replyRepository.save(ReplyEntity.from(newReply, memberEntity, postEntity)).getId();
    }

    @Override
    public void update(Reply reply) {
        ReplyEntity entity = replyRepository.findById(reply.getId())
                .orElseThrow(() -> new NotFoundEntityException(ExceptionCode.NOT_FOUND_REPLY));
        entity.update(reply);
    }

    @Override
    public void deleteById(Long id) {
        replyRepository.deleteById(id);
    }

    @Override
    public void deleteByReplyIds(List<Long> ids) {
        replyRepository.deleteAllByReplyIds(ids);
    }

    @Override
    public Optional<Reply> findById(Long id) {
        return replyRepository.findById(id).map(ReplyEntity::toReply);
    }

    @Override
    public Optional<ReplyWithPost> findByIdWithPost(Long id) {
        return replyRepository.findById(id).map(ReplyEntity::toReplyWithPost);
    }

    @Override
    public List<Reply> findAllByPostId(Long postId) {
        return replyRepository.findAllByPostEntityId(postId).stream().map(ReplyEntity::toReply).toList();
    }

    @Override
    public List<Reply> findAllByMemberId(Long memberId) {
        return replyRepository.findAllByMemberEntityId(memberId).stream().map(ReplyEntity::toReply).toList();
    }

    @Override
    public List<Reply> findAllByParentId(Long parentReplyId) {
        List<ReplyEntity> entities = replyRepository.findAllByParentReplyId(parentReplyId);
        return entities.stream().map(ReplyEntity::toReply).toList();
    }
}