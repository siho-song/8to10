package com.eighttoten;

import com.eighttoten.achievement.AchievementRepository;
import com.eighttoten.achievement.AchievementWithMember;
import com.eighttoten.member.domain.Member;
import com.eighttoten.member.domain.MemberRepository;
import com.eighttoten.notification.domain.FeedbackMessage;
import com.eighttoten.notification.domain.NewNotification;
import com.eighttoten.notification.domain.NotificationMessage;
import com.eighttoten.notification.domain.NotificationType;
import com.eighttoten.notification.domain.repository.NotificationRepository;
import com.eighttoten.notification.event.NotificationEvent;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SchedulingTaskPublisher {
    private static final String ADMIN = "ADMIN";
    private final AchievementRepository achievementRepository;
    private final NotificationRepository notificationRepository;
    private final MemberRepository memberRepository;
    private final ApplicationEventPublisher eventPublisher;

    @Scheduled(cron = "1 0 0 * * *")
    public void notifyFeedBack(){
        List<AchievementWithMember> achievements = achievementRepository.findAllByDateWithMember(
                LocalDate.now().minusDays(1L));

        //여기서 db와의 연결에 문제가 발생했을때의 보상전략이 필요하다.
        for (AchievementWithMember achievement : achievements) {
            Member member = achievement.getMember();
            FeedbackMessage feedbackMessage = FeedbackMessage.selectRandomMessage(
                    member.getMode(),
                    achievement.getAchievementRate()
            );

            NotificationType type = NotificationType.ACHIEVEMENT_FEEDBACK;
            NewNotification newNotification = NewNotification.of(member.getId(), type,
                    feedbackMessage.getMessage(), null, null, ADMIN);

            long savedId = notificationRepository.save(newNotification);
            eventPublisher.publishEvent(new NotificationEvent(
                    savedId,
                    member.getEmail(),
                    null,
                    null,
                    feedbackMessage.getMessage(),
                    ADMIN,
                    type));
        }
    }

    @Scheduled(cron = "0 0 22 * * *")
    public void notifyTodoUpdate(){
        List<Member> members = memberRepository.findAll();
        NotificationType type = NotificationType.TODO_UPDATE;

        for (Member member : members) {
            NewNotification newNotification = NewNotification.of(member.getId(), type,
                    NotificationMessage.TODO_UPDATE.getMessage(), null, null, ADMIN);
            long savedId = notificationRepository.save(newNotification);
            eventPublisher.publishEvent(new NotificationEvent(
                    savedId,
                    member.getEmail(),
                    null,
                    null,
                    NotificationMessage.TODO_UPDATE.getMessage(),
                    ADMIN,
                    type));
        }
    }
}
