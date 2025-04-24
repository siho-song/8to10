package com.eighttoten.notification.service;

import static com.eighttoten.exception.ExceptionCode.INVALID_REDIS_MESSAGE;

import com.eighttoten.exception.ExceptionCode;
import com.eighttoten.exception.InternalException;
import com.eighttoten.exception.NotFoundEntityException;
import com.eighttoten.member.domain.Member;
import com.eighttoten.member.domain.MemberRepository;
import com.eighttoten.notification.domain.NewNotification;
import com.eighttoten.notification.domain.Notification;
import com.eighttoten.notification.domain.NotificationSendInfo;
import com.eighttoten.notification.domain.repository.NotificationRepository;
import com.eighttoten.notification.event.NotificationEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RequiredArgsConstructor
@Component
@Slf4j
public class RedisMessageSubscriber {
    private static final String NOTIFICATION_EVENT_NAME = "notification";

    private final SseEmitterService sseEmitterService;
    private final MemberRepository memberRepository;
    private final NotificationRepository notificationRepository;
    private final ObjectMapper objectMapper;

    /**
     * 레디스 채널에 publish된 메시지를 subscribe한다.
     * @param message
     */
    public void handleNotificationEvent(String message) {
        NotificationEvent event = convertToObject(message, NotificationEvent.class);
        Member member = memberRepository.findByEmail(event.getClientEmail())
                .orElseThrow(() -> new NotFoundEntityException(ExceptionCode.NOT_FOUND_MEMBER));

        NewNotification newNotification = NewNotification.from(member.getId(), event);
        long savedId = 0L;
        if (newNotification.getNotificationType().getIsNeededSave()) {
            savedId = notificationRepository.save(newNotification);
        }

        Notification notification = notificationRepository.findById(savedId)
                .orElseThrow(() -> new NotFoundEntityException(ExceptionCode.NOT_FOUND_NOTIFICATION));

        List<SseEmitter> emitters = sseEmitterService.findAllStartWithByMemberEmail(member.getEmail());
        if (!emitters.isEmpty()) {
            emitters.forEach(emitter -> sseEmitterService.sendToClient(
                    emitter,
                    sseEmitterService.generateUniqueClientId(member.getEmail(), LocalDateTime.now()),
                    NOTIFICATION_EVENT_NAME,
                    convertToJson(NotificationSendInfo.from(notification)))
            );
        }
    }

    private String convertToJson(Object obj) {
        try {
            return objectMapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            log.error(e.getMessage(), e);
            throw new InternalException(INVALID_REDIS_MESSAGE);
        }
    }

    private <T> T convertToObject(String message, Class<T> classType) {
        try {
            return objectMapper.readValue(message.getBytes(), classType);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
            throw new InternalException(INVALID_REDIS_MESSAGE);
        }
    }
}