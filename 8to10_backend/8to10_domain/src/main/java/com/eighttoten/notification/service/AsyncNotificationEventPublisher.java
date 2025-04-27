package com.eighttoten.notification.service;

import com.eighttoten.community.event.reply.ReplyAddEvent;
import com.eighttoten.notification.domain.NotificationMessage;
import com.eighttoten.notification.domain.NotificationType;
import com.eighttoten.notification.event.NotificationEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AsyncNotificationEventPublisher {
    private final ApplicationEventPublisher eventPublisher;

    @Async
    public void notifyReplyAddEvent(ReplyAddEvent replyAddEvent) {
        String postWriter = replyAddEvent.getPostWriter();
        String parentReplyWriter = replyAddEvent.getParentReplyWriter();
        String replyWriter = replyAddEvent.getReplyWriter();

        //대댓글 알림
        if(replyAddEvent.getParentReplyId() != null && !replyWriter.equals(parentReplyWriter)){
            eventPublisher.publishEvent(new NotificationEvent(
                    parentReplyWriter,
                    replyAddEvent.getPostId(),
                    replyAddEvent.getReplyId(),
                    NotificationMessage.NESTED_REPLY_ADD.getMessage(),
                    replyWriter,
                    NotificationType.NESTED_REPLY_ADD));
        }

        //댓글 알림
        if (!postWriter.equals(replyWriter)) {
            eventPublisher.publishEvent(new NotificationEvent(
                    postWriter,
                    replyAddEvent.getPostId(),
                    replyAddEvent.getReplyId(),
                    NotificationMessage.REPLY_ADD.getMessage(),
                    replyWriter,
                    NotificationType.REPLY_ADD));
        }
    }
}