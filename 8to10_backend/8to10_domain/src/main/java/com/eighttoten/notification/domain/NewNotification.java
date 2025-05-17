package com.eighttoten.notification.domain;

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

    public static NewNotification of(Long memberId, NotificationType type, String message, Long targetEntityId,
                                     Long relatedEntityId, String createdBy) {
        NewNotification newNotification = new NewNotification();
        newNotification.memberId = memberId;
        newNotification.notificationType = type;
        newNotification.message = message;
        newNotification.setTargetUrl(type.getBaseTargetUrl(),targetEntityId);
        newNotification.relatedEntityId = relatedEntityId;
        newNotification.createdBy = createdBy;
        return newNotification;
    }

    private void setTargetUrl(String baseTargetUrl, Long targetEntityId){
        this.targetUrl = baseTargetUrl + "/" + targetEntityId;
    }
}
