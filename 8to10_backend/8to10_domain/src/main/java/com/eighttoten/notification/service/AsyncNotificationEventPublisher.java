package com.eighttoten.notification.service;

import com.eighttoten.community.event.reply.ReplyAddEvent;
import com.eighttoten.notification.domain.NewNotification;
import com.eighttoten.notification.domain.NotificationMessage;
import com.eighttoten.notification.domain.NotificationType;
import com.eighttoten.notification.domain.repository.NotificationRepository;
import com.eighttoten.notification.event.NotificationEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AsyncNotificationEventPublisher {
    private final NotificationRepository notificationRepository;
    private final ApplicationEventPublisher eventPublisher;

    @Async
    public void notifyReplyAddEvent(ReplyAddEvent replyAddEvent) {
        String postWriter = replyAddEvent.getPostWriter();
        String parentReplyWriter = replyAddEvent.getParentReplyWriter();
        String replyWriter = replyAddEvent.getReplyWriter();
        Long memberId;
        NotificationEvent alarm;
        //대댓글 알림
        if(replyAddEvent.getParentReplyId() != null && !replyWriter.equals(parentReplyWriter)){
            memberId = replyAddEvent.getParentReplyId();
            NewNotification newNotification = NewNotification.of(memberId, NotificationType.NESTED_REPLY_ADD,
                    NotificationMessage.NESTED_REPLY_ADD.getMessage(), replyAddEvent.getParentReplyId(),
                    replyAddEvent.getReplyId(), replyAddEvent.getReplyWriter());

            long savedId = notificationRepository.save(newNotification);
            alarm = new NotificationEvent(
                    savedId,
                    parentReplyWriter,
                    replyAddEvent.getPostId(),
                    replyAddEvent.getReplyId(),
                    NotificationMessage.NESTED_REPLY_ADD.getMessage(),
                    replyWriter,
                    NotificationType.NESTED_REPLY_ADD);
            eventPublisher.publishEvent(alarm);
        }

        //댓글 알림
        if (!postWriter.equals(replyWriter)) {
            memberId = replyAddEvent.getPostId();
            NewNotification newNotification = NewNotification.of(memberId, NotificationType.REPLY_ADD,
                    NotificationMessage.REPLY_ADD.getMessage(), replyAddEvent.getPostId(),
                    replyAddEvent.getReplyId(), replyAddEvent.getReplyWriter());
            long savedId = notificationRepository.save(newNotification);

            alarm = new NotificationEvent(
                    savedId,
                    postWriter,
                    replyAddEvent.getPostId(),
                    replyAddEvent.getReplyId(),
                    NotificationMessage.REPLY_ADD.getMessage(),
                    replyWriter,
                    NotificationType.REPLY_ADD);
            eventPublisher.publishEvent(alarm);
        }
    }
}