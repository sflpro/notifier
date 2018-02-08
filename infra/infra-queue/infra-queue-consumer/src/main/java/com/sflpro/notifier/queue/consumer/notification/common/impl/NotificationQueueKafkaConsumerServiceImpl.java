package com.sflpro.notifier.queue.consumer.notification.common.impl;

import com.sflpro.notifier.queue.consumer.notification.common.NotificationQueueConsumerService;
import com.sflpro.notifier.services.notification.NotificationProcessingService;
import com.sflpro.notifier.services.notification.event.sms.StartSendingNotificationEvent;
import com.sflpro.notifier.services.notification.event.sms.StartSendingNotificationEventListenerAdapter;
import com.sflpro.notifier.services.system.event.ApplicationEventDistributionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.annotation.Nonnull;

/**
 * Company: SFL LLC
 * Created on 08/02/2018
 *
 * @author Davit Harutyunyan
 */
@Service("kafka")
@Lazy(false)
public class NotificationQueueKafkaConsumerServiceImpl implements NotificationQueueConsumerService, InitializingBean {

    private static final Logger LOGGER = LoggerFactory.getLogger(NotificationQueueKafkaConsumerServiceImpl.class);

    /* Dependencies */
    @Autowired
    private NotificationProcessingService notificationProcessingService;

    @Autowired
    private ApplicationEventDistributionService applicationEventDistributionService;

    /* Constructors */
    public NotificationQueueKafkaConsumerServiceImpl() {
        LOGGER.debug("Initializing sms notification queue consumer service");
    }

    @Override
    public void afterPropertiesSet() {
        applicationEventDistributionService.subscribe(new StartSendingNotificationEventListener());
    }

    /* Public methods */
    @KafkaListener(topics = "${kafka.topic.names}")
    @Override
    public void processNotification(@Nonnull final Long notificationId) {
        Assert.notNull(notificationId, "Notification id should not be null");
        LOGGER.debug("Processing notification with id - {}", notificationId);
        notificationProcessingService.processNotification(notificationId);
        LOGGER.debug("Successfully processed notification with id - {}", notificationId);
    }

    /* Properties getters and setters */
    public NotificationProcessingService getNotificationProcessingService() {
        return notificationProcessingService;
    }

    public void setNotificationProcessingService(final NotificationProcessingService notificationProcessingService) {
        this.notificationProcessingService = notificationProcessingService;
    }

    /* Inner classes */
    private class StartSendingNotificationEventListener extends StartSendingNotificationEventListenerAdapter {

        /* Constructors */
        public StartSendingNotificationEventListener() {
            super();
        }

        @Override
        protected void processStartSendingNotificationEvent(final StartSendingNotificationEvent event) {
            NotificationQueueKafkaConsumerServiceImpl.this.processNotification(event.getNotificationId());
        }
    }
}
