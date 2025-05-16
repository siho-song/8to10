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
        //알람전송이 실패한다면 보상전략이 필요하다. 실패 한다면 db에 저장하거나 별도로 실패 상태를 저장해야한다.
        //여기까지 왔다는건 db 문제는 없다는 것임.
        //알람전송이 실패하는 케이스는 서버 내부오류,
        //만약 kafka 큐로 전환하면?
        try {
            String channelMessage = objectMapper.writeValueAsString(event);
            messagePublisher.send(channelMessage);
        } catch (JsonProcessingException e) {
            log.error(e.getMessage(), e);
            throw new InternalException(INVALID_REDIS_MESSAGE);
        }
    }
}