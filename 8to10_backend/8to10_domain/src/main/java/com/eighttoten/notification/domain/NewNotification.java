package com.eighttoten.notification.domain;

import com.eighttoten.notification.event.NotificationEvent;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class NewNotification {
    private Long memberId;
    private NotificationType notificationType;
    private String message;
    private String targetUrl;
    private Long relatedEntityId;
    private String createdBy;

    public static NewNotification from(Long memberId, NotificationEvent event) {
        NewNotification newNotification = new NewNotification();
        newNotification.memberId = memberId;
        newNotification.notificationType = event.getNotificationType();
        newNotification.message = event.getMessage();
        newNotification.setTargetUrl(event.getNotificationType().getBaseTargetUrl(),event.getTargetEntityId());
        newNotification.relatedEntityId = event.getRelatedEntityId();
        newNotification.createdBy = event.getCreatedBy();
        return newNotification;
    }

    private void setTargetUrl(String baseTargetUrl, Long targetEntityId){
        this.targetUrl = baseTargetUrl + "/" + targetEntityId;
    }
}
