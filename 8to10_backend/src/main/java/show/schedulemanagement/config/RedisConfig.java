package show.schedulemanagement.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.data.redis.serializer.RedisSerializer;
import show.schedulemanagement.service.notification.NotificationService;

@Configuration
public class RedisConfig {

    @Value(value = "${spring.data.redis.host}")
    private String redisHost;

    @Value(value = "${spring.data.redis.port}")
    private int redisPort;

    @Bean
    public RedisConnectionFactory redisConnectionFactory(){
        return new LettuceConnectionFactory(redisHost, redisPort);
    }

    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        redisTemplate.setKeySerializer(RedisSerializer.string());
        redisTemplate.setValueSerializer(RedisSerializer.string());
        redisTemplate.setHashKeySerializer(RedisSerializer.string());
        redisTemplate.setHashValueSerializer(RedisSerializer.string());
        return redisTemplate;
    }

    @Bean
    public RedisMessageListenerContainer redisMessageListenerContainer(
            RedisConnectionFactory redisConnectionFactory,
            MessageListenerAdapter notificationListenerAdapter,
            MessageListenerAdapter lastEventIdListenerAdapter
            ){
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(redisConnectionFactory);
        container.addMessageListener(notificationListenerAdapter, notificationTopic());
        container.addMessageListener(lastEventIdListenerAdapter, lastEventIdTopic());
        return container;
    }

    @Bean
    public MessageListenerAdapter notificationListenerAdapter(NotificationService notificationService){
        return new MessageListenerAdapter(notificationService, "handleNotificationEvent");
    }

    @Bean
    public MessageListenerAdapter lastEventIdListenerAdapter(NotificationService notificationService) {
        return new MessageListenerAdapter(notificationService, "handleLastEventId");
    }

    @Bean
    public ChannelTopic notificationTopic(){
        return new ChannelTopic("notificationChannel");
    }

    @Bean
    public ChannelTopic lastEventIdTopic(){
        return new ChannelTopic("lastEventIdChannel");
    }
}