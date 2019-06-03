package com.sflpro.notifier.queue.consumer;

import com.sflpro.notifier.queue.QueueConfigurationDefaults;
import com.sflpro.notifier.queue.amqp.RabbitConfiguration;
import com.sflpro.notifier.queue.amqp.rpc.RPCQueueMessageHandler;
import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

/**
 * Company: SFL LLC
 * Created on 08/02/2018
 *
 * @author Davit Harutyunyan
 */
@Configuration
@ConditionalOnProperty(name = "notifier.queue.engine", havingValue = "rabbit")
@Import({RabbitConfiguration.class, QueueConfigurationDefaults.class})
public class RabbitConsumerConfiguration {

    @Value("${notifier.queue.topic}")
    private String topic;

    @Value("${notifier.rabbitmq.concurrentConsumers}")
    private int concurrentConsumers;

    @Value("${amqp.maxConcurrentConsumers}")
    private int maxConcurrentConsumers;

    @Value("${amqp.prefetchCount}")
    private int prefetchCount;

    @Bean
    public Queue queue() {
        return new Queue(topic, true);
    }

    @Lazy(false)
    @Bean(destroyMethod = "stop", initMethod = "start")
    public SimpleMessageListenerContainer messageListenerContainer(final ConnectionFactory connectionFactory,
                                                                   final AmqpAdmin amqpAdmin,
                                                                   @Qualifier("amqpTaskExecutor") final ThreadPoolTaskExecutor taskExecutor,
                                                                   final RPCQueueMessageHandler messageHandler) {
        SimpleMessageListenerContainer messageListenerContainer = new SimpleMessageListenerContainer(connectionFactory);
        messageListenerContainer.setAmqpAdmin(amqpAdmin);
        messageListenerContainer.setMessageListener(messageHandler::handleMessage);
        messageListenerContainer.setQueues(queue());
        messageListenerContainer.setTaskExecutor(taskExecutor);
        messageListenerContainer.setConcurrentConsumers(concurrentConsumers);
        messageListenerContainer.setMaxConcurrentConsumers(maxConcurrentConsumers);
        messageListenerContainer.setPrefetchCount(prefetchCount);

        return messageListenerContainer;
    }
}
