package com.eighttoten.notification.event;

import com.eighttoten.notification.domain.NotificationType;
import java.util.UUID;
import lombok.Getter;

@Getter
public class NotificationEvent {
    final Long notificationId;
    final String clientEmail;
    final String eventId;
    final Long targetEntityId;
    final Long relatedEntityId;
    final String message;
    final String createdBy;
    final NotificationType notificationType;

    public NotificationEvent(Long notificationId, String clientEmail, Long targetEntityId, Long relatedEntityId,
                             String message,String createdBy,
                             NotificationType notificationType) {
        this.notificationId = notificationId;
        this.clientEmail = clientEmail;
        this.eventId = UUID.randomUUID().toString();
        this.targetEntityId = targetEntityId;
        this.relatedEntityId = relatedEntityId;
        this.message = message;
        this.createdBy = createdBy;
        this.notificationType = notificationType;
    }
}