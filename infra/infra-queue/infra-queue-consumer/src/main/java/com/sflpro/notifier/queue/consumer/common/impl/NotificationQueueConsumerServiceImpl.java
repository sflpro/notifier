package com.sflpro.notifier.queue.consumer.common.impl;

import com.sflpro.notifier.queue.consumer.common.NotificationQueueConsumerService;
import com.sflpro.notifier.services.notification.NotificationProcessingService;
import com.sflpro.notifier.services.notification.event.sms.StartSendingNotificationEvent;
import com.sflpro.notifier.services.notification.event.sms.StartSendingNotificationEventListenerAdapter;
import com.sflpro.notifier.services.system.event.ApplicationEventDistributionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.annotation.Nonnull;
import java.util.Map;

/**
 * User: Mher Sargsyan
 * Company: SFL LLC
 * Date: 4/10/15
 * Time: 7:04 PM
 */
@Service
@Lazy(false)
public class NotificationQueueConsumerServiceImpl implements NotificationQueueConsumerService, InitializingBean {

    private static final Logger LOGGER = LoggerFactory.getLogger(NotificationQueueConsumerServiceImpl.class);

    /* Dependencies */
    @Autowired
    private NotificationProcessingService notificationProcessingService;

    @Autowired
    private ApplicationEventDistributionService applicationEventDistributionService;

    /* Constructors */
    public NotificationQueueConsumerServiceImpl() {
        LOGGER.debug("Initializing sms notification queue consumer service");
    }

    @Override
    public void afterPropertiesSet() {
        applicationEventDistributionService.subscribe(new StartSendingNotificationEventListener());
    }

    /* Public methods */
    @Override
    public void processNotification(@Nonnull final Long notificationId, @Nonnull final Map<String, String> secureProperties) {
        Assert.notNull(notificationId, "Notification id should not be null");
        Assert.notNull(secureProperties, "Secure properties map should not be null");
        LOGGER.debug("Processing notification with id - {}", notificationId);
        notificationProcessingService.processNotification(notificationId, secureProperties);
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
            NotificationQueueConsumerServiceImpl.this.processNotification(event.getNotificationId(), event.getSecureProperties());
        }
    }
}
