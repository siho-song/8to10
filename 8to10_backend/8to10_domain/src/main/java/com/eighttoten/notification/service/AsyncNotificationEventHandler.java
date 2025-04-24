package com.eighttoten.notification.service;

import static com.eighttoten.exception.ExceptionCode.INVALID_REDIS_MESSAGE;

import com.eighttoten.exception.InternalException;
import com.eighttoten.notification.event.NotificationEvent;
import com.eighttoten.support.MessagePublisher;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class AsyncNotificationEventHandler {
    private final MessagePublisher messagePublisher;
    private final ObjectMapper objectMapper;

    @Async
    @EventListener
    public void handleNotificationEvent(NotificationEvent event){
        try {
            String channelMessage = objectMapper.writeValueAsString(event);
            messagePublisher.send(channelMessage);
        } catch (JsonProcessingException e) {
            log.error(e.getMessage(), e);
            throw new InternalException(INVALID_REDIS_MESSAGE);
        }
    }
}
