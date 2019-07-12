package com.sflpro.notifier.queue.producer;

import com.sflpro.notifier.queue.QueueConfigurationDefaults;
import com.sflpro.notifier.queue.producer.connector.AmqpConnectorService;
import com.sflpro.notifier.queue.producer.notification.common.NotificationQueueProducerService;
import com.sflpro.notifier.queue.producer.notification.common.impl.NotificationQueueProducerServiceImpl;
import com.sflpro.notifier.queue.producer.notification.push.PushNotificationSubscriptionRequestQueueProducerService;
import com.sflpro.notifier.queue.producer.notification.push.impl.PushNotificationSubscriptionRequestQueueProducerServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import javax.annotation.PostConstruct;

@Configuration
@ComponentScan("com.sflpro.notifier.queue.producer")
@Import({QueueConfigurationDefaults.class, RabbitProducerConfiguration.class, KafkaProducerConfiguration.class})
public class ProducerConfiguration {

    private static final Logger logger = LoggerFactory.getLogger(ProducerConfiguration.class);

    private @Autowired(required = false)
    AmqpConnectorService amqpConnectorService;

    @PostConstruct
    void init() {
        if (amqpConnectorService == null) {
            logger.warn("The application will not use queue. This kind of setup is more suitable for development.");
        } else {
            logger.info("{} was set up successfully.", notificationQueueProducerService());
            logger.info("{} was set up successfully.", pushNotificationSubscriptionRequestQueueProducerService());
        }
    }

    @Bean
    NotificationQueueProducerService notificationQueueProducerService() {
        logger.debug("Creating notificationQueueProducerService.");
        return new NotificationQueueProducerServiceImpl();
    }

    @Bean
    PushNotificationSubscriptionRequestQueueProducerService pushNotificationSubscriptionRequestQueueProducerService() {
        logger.debug("Creating pushNotificationSubscriptionRequestQueueProducerService.");
        return new PushNotificationSubscriptionRequestQueueProducerServiceImpl();
    }

}
