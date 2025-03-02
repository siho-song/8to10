package com.eighttoten.community.service;

import com.eighttoten.community.domain.post.Post;
import com.eighttoten.community.domain.post.repository.PostRepository;
import com.eighttoten.community.domain.reply.Reply;
import com.eighttoten.community.domain.reply.repository.ReplyRepository;
import com.eighttoten.community.event.post.PostStatsUpdateEvent;
import com.eighttoten.community.event.reply.ReplyStatsUpdateEvent;
import com.eighttoten.exception.ExceptionCode;
import com.eighttoten.exception.NotFoundEntityException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class CommunityEventHandler {
    private final PostRepository postRepository;
    private final ReplyRepository replyRepository;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handlePostStatsUpdateEvent(PostStatsUpdateEvent event) {
        Post post = postRepository.findById(event.getPostId())
                .orElseThrow(() -> new NotFoundEntityException(ExceptionCode.NOT_FOUND_POST));

        event.execute(post);
        postRepository.update(post);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleReplyStatsUpdateEvent(ReplyStatsUpdateEvent event){
        Reply reply = replyRepository.findById(event.getReplyId())
                .orElseThrow(() -> new NotFoundEntityException(ExceptionCode.NOT_FOUND_REPLY));

        event.execute(reply);
        replyRepository.update(reply);
    }
}
