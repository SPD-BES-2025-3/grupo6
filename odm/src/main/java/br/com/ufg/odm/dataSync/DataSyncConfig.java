package br.com.ufg.odm.dataSync;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;

@Configuration
@RequiredArgsConstructor
public class DataSyncConfig {

    private static final String SYNC_CHANNEL = "data-sync-channel";

    private final DataSyncSubscriber dataSyncSubscriber;

    @Bean
    public RedisMessageListenerContainer redisMessageListenerContainer(RedisConnectionFactory connectionFactory) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.addMessageListener(new MessageListenerAdapter(dataSyncSubscriber), channelTopic());
        return container;
    }

    @Bean
    public ChannelTopic channelTopic() {
        return new ChannelTopic(SYNC_CHANNEL);
    }
}
