package com.eighttoten.notification;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import com.eighttoten.notification.domain.NewNotification;
import com.eighttoten.notification.domain.Notification;
import com.eighttoten.notification.domain.NotificationType;
import com.eighttoten.notification.domain.repository.NotificationRepository;
import com.eighttoten.notification.event.NotificationEvent;
import com.eighttoten.notification.repository.NotificationRepositoryImpl;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.AuditorAware;

@DataJpaTest
@DisplayName("알람 레포지토리 테스트")
@Import(NotificationRepositoryImpl.class)
public class NotificationRepositoryTest {
    @MockBean
    AuditorAware<String> auditorAware;

    @Autowired
    NotificationRepository notificationRepository;

    @Test
    @DisplayName("새로운 알람을 저장한다.")
    void save(){
        //given
        NewNotification newNotification = NewNotification.of(1L, NotificationType.REPLY_ADD,"test",1L,1L,"test@example.com");
        when(auditorAware.getCurrentAuditor()).thenReturn(Optional.of("test"));

        //when
        long savedId = notificationRepository.save(newNotification);

        //then
        assertThat(notificationRepository.findById(savedId)).isNotEmpty();
    }

    @Test
    @DisplayName("알람을 업데이트한다.")
    void update(){
        //given
        Long notificationId = 1L;
        boolean newReadStatus = true;
        Notification notification = notificationRepository.findById(notificationId).orElseThrow();
        notification.updateReadStatus(newReadStatus);

        //when
        notificationRepository.update(notification);

        //then
        assertThat(notificationRepository.findById(notificationId).orElseThrow().isReadStatus()).isTrue();
    }

    @Test
    @DisplayName("알람을 id로 삭제한다.")
    void deleteById(){
        //given
        Long notificationId = 2L;

        //when
        notificationRepository.deleteById(notificationId);

        //then
        assertThat(notificationRepository.findById(notificationId)).isEmpty();

    }

    @Test
    @DisplayName("알람을 id로 조회한다.")
    void findById(){
        //given
        Long notificationId = 1L;

        //when,then
        assertThat(notificationRepository.findById(notificationId)).isNotEmpty();

    }

    @Test
    @DisplayName("특정날짜 이후의 알람들을 멤버의 id로 조회한다.")
    void findAllByMemberIdAfterStart(){
        //given
        Long memberId = 1L;
        LocalDateTime start = LocalDateTime.of(2024, 1, 1, 0, 0);
        //when
        List<Notification> notifications = notificationRepository.findAllByMemberIdAfterStart(memberId, start);

        //then
        assertThat(notifications).hasSize(3);
    }
}
