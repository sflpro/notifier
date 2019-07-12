package com.sflpro.notifier.queue.producer.notification;

import com.sflpro.notifier.queue.producer.connector.AmqpConnectorService;
import com.sflpro.notifier.queue.producer.notification.common.NotificationQueueProducerService;
import com.sflpro.notifier.queue.producer.notification.common.impl.NotificationQueueProducerServiceImpl;
import com.sflpro.notifier.queue.producer.notification.push.PushNotificationSubscriptionRequestQueueProducerService;
import com.sflpro.notifier.queue.producer.notification.push.impl.PushNotificationSubscriptionRequestQueueProducerServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

/**
 * Created by Hayk Mkrtchyan.
 * Date: 7/12/19
 * Time: 3:58 PM
 */
@Configuration
 public class NotificationQueueProducerConfiguration {

    private static final Logger logger = LoggerFactory.getLogger(NotificationQueueProducerConfiguration.class);

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
