package com.sflpro.notifier.queue.producer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sflpro.notifier.queue.QueueConfigurationDefaults;
import com.sflpro.notifier.queue.amqp.RabbitConfiguration;
import com.sflpro.notifier.queue.amqp.queues.UniquelyNamedConfigurableQueue;
import com.sflpro.notifier.queue.producer.connector.AmqpConnectorService;
import com.sflpro.notifier.queue.producer.connector.impl.RabbitConnectorServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import javax.annotation.PostConstruct;

/**
 * Company: SFL LLC
 * Created on 08/02/2018
 *
 * @author Davit Harutyunyan
 */
@Configuration
@Import({QueueConfigurationDefaults.class, RabbitConfiguration.class})
@ConditionalOnProperty(name = "notifier.queue.engine", havingValue = "rabbit")
public class RabbitProducerConfiguration {

    private static final Logger logger = LoggerFactory.getLogger(RabbitProducerConfiguration.class);

    private final static String AMQP_RESPONSE_QUEUE_NAME = "notifier-responses-";

    @Value("${notifier.queue.topic}")
    private String queueName;

    @Value("${notifier.rabbitmq.replyTimeout}")
    private long replyTimeout;

    @Value("${notifier.rabbitmq.concurrentConsumers}")
    private int concurrentConsumers;

    @Value("${amqp.maxConcurrentConsumers}")
    private int maxConcurrentConsumers;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private @Qualifier("responseQueue")
    Queue responseQueue;

    @PostConstruct
    void init() {
        final String responseQueueName = responseQueue.getName();
        rabbitTemplate.setRoutingKey(queueName);
        rabbitTemplate.setReplyAddress(responseQueueName);
        rabbitTemplate.setReplyTimeout(replyTimeout);
        logger.debug("Configured rabbit producer to work with {}, {}", queueName, responseQueueName);
    }

    @Bean
    Queue responseQueue() {
        logger.debug("Creating queue {}", AMQP_RESPONSE_QUEUE_NAME);
        return new UniquelyNamedConfigurableQueue(AMQP_RESPONSE_QUEUE_NAME, false, false, true);
    }

    @Bean
    @ConditionalOnMissingBean
    public RabbitTemplate rabbitTemplate(final ConnectionFactory connectionFactory) {
        logger.debug("No rabbit template was found, creating the default on.");
        return new RabbitTemplate(connectionFactory);
    }

    @Bean
    @Lazy(false)
    public AmqpConnectorService connectorService(final RabbitTemplate rabbitTemplate,
                                                 @Qualifier("amqpObjectMapper") final ObjectMapper objectMapper) {
        logger.debug("Creating rabbit based component of type AmqpConnectorService.");
        return new RabbitConnectorServiceImpl(rabbitTemplate, objectMapper);
    }

    @Bean(destroyMethod = "stop")
    public SimpleMessageListenerContainer messageListenerContainer(final ConnectionFactory connectionFactory,
                                                                   final AmqpAdmin amqpAdmin,
                                                                   final RabbitTemplate rabbitTemplate,
                                                                   @Qualifier("amqpTaskExecutor") final ThreadPoolTaskExecutor taskExecutor) {
        logger.debug("Creating MessageListenerContainer for response queue.");
        final SimpleMessageListenerContainer messageListenerContainer = new SimpleMessageListenerContainer(connectionFactory);
        messageListenerContainer.setAmqpAdmin(amqpAdmin);
        messageListenerContainer.setMessageListener(rabbitTemplate);
        messageListenerContainer.setQueues(responseQueue());
        messageListenerContainer.setTaskExecutor(taskExecutor);
        messageListenerContainer.setConcurrentConsumers(concurrentConsumers);
        messageListenerContainer.setMaxConcurrentConsumers(maxConcurrentConsumers);
        return messageListenerContainer;
    }
}
