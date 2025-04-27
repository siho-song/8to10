package com.eighttoten.notification;

import static jakarta.persistence.FetchType.LAZY;
import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

import com.eighttoten.BaseEntity;
import com.eighttoten.BaseTimeEntity;
import com.eighttoten.member.MemberEntity;
import com.eighttoten.notification.domain.NewNotification;
import com.eighttoten.notification.domain.Notification;
import com.eighttoten.notification.domain.NotificationType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = PROTECTED)
@Table(name = "notification")
public class NotificationEntity extends BaseTimeEntity {
    @Id @GeneratedValue(strategy = IDENTITY)
    @Column(name = "notification_id")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private MemberEntity memberEntity;

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private NotificationType notificationType;

    @Column(columnDefinition = "TEXT")
    private String message;

    private String targetUrl;

    private String createdBy;

    private String updatedBy;

    private Long relatedEntityId;

    private boolean isRead;

    public Notification toNotification() {
        return new Notification(id, notificationType,
                message, targetUrl, relatedEntityId,
                isRead, createdBy);
    }

    public static NotificationEntity from(NewNotification newNotification, MemberEntity memberEntity) {
        NotificationEntity notificationEntity = new NotificationEntity();
        notificationEntity.memberEntity = memberEntity;
        notificationEntity.notificationType = newNotification.getNotificationType();
        notificationEntity.message = newNotification.getMessage();
        notificationEntity.targetUrl = newNotification.getTargetUrl();
        notificationEntity.relatedEntityId = newNotification.getRelatedEntityId();
        notificationEntity.createdBy = newNotification.getCreatedBy();
        notificationEntity.updatedBy = newNotification.getCreatedBy();
        return notificationEntity;
    }

    public void update(Notification notification){
        this.isRead = notification.isReadStatus();
    }
}