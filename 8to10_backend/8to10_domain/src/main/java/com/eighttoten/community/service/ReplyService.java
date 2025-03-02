package com.eighttoten.community.service;

import static com.eighttoten.exception.ExceptionCode.NOT_FOUND_REPLY;

import com.eighttoten.community.domain.reply.NewReply;
import com.eighttoten.community.domain.reply.Reply;
import com.eighttoten.community.domain.reply.ReplyWithPost;
import com.eighttoten.community.domain.reply.UpdateReply;
import com.eighttoten.community.domain.reply.repository.ReplyHeartRepository;
import com.eighttoten.community.domain.reply.repository.ReplyRepository;
import com.eighttoten.community.event.reply.ReplyAddEvent;
import com.eighttoten.exception.BadRequestException;
import com.eighttoten.exception.ExceptionCode;
import com.eighttoten.exception.NotFoundEntityException;
import com.eighttoten.member.domain.Member;
import com.eighttoten.notification.service.AsyncNotificationEventPublisher;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReplyService{
    private final ReplyRepository replyRepository;
    private final ReplyHeartRepository replyHeartRepository;
    private final AsyncNotificationEventPublisher eventPublisher;

    @Transactional
    public void addReply(NewReply newReply) {
        String parentReplyWriter = null;
        Long parentId = newReply.getParentId();

        if(parentId != null){
            ReplyWithPost parentReply = replyRepository.findByIdWithPost(parentId)
                    .orElseThrow(() -> new NotFoundEntityException(NOT_FOUND_REPLY));

            if (parentReply.getPost().checkIsEqualId(newReply.getPostId()) && parentReply.getParentId() == null) {
                parentReplyWriter = parentReply.getCreatedBy();
            } else {
                throw new BadRequestException(ExceptionCode.INVALID_REPLY_SAVE);
            }
        }

        long savedId = replyRepository.save(newReply);
        ReplyWithPost replyWithPost = replyRepository.findByIdWithPost(savedId)
                .orElseThrow(() -> new NotFoundEntityException(NOT_FOUND_REPLY));

        eventPublisher.notifyReplyAddEvent(ReplyAddEvent.from(replyWithPost, parentReplyWriter));
    }

    @Transactional
    public void update(Member member, UpdateReply updateReply) {
        Reply reply = replyRepository.findById(updateReply.getId())
                .orElseThrow(() -> new NotFoundEntityException(NOT_FOUND_REPLY));

        member.checkIsSameEmail(reply.getCreatedBy());
        reply.update(updateReply);
        replyRepository.update(reply);
    }

    @Transactional
    public void deleteById(Member member, Long id) {
        Reply reply = replyRepository.findById(id).orElseThrow(() -> new NotFoundEntityException(NOT_FOUND_REPLY));

        member.checkIsSameEmail(reply.getCreatedBy());

        if (reply.getParentId() != null) { //자식 댓글
            replyHeartRepository.deleteAllByReplyId(id);
            replyRepository.deleteById(id);
            return;
        }

        //부모 댓글
        List<Long> replyIds = new ArrayList<>();

        replyIds.add(reply.getId());
        replyIds.addAll(replyRepository.findAllByParentId(reply.getId())
                .stream().map(Reply::getId).toList());

        replyHeartRepository.deleteAllByReplyIds(replyIds);
        replyRepository.deleteByReplyIds(replyIds);
    }
}