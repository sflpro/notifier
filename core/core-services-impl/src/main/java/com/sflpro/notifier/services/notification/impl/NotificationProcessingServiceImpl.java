package com.sflpro.notifier.services.notification.impl;

import com.sflpro.notifier.db.entities.notification.Notification;
import com.sflpro.notifier.db.entities.notification.NotificationType;
import com.sflpro.notifier.services.notification.NotificationProcessingService;
import com.sflpro.notifier.services.notification.NotificationProcessor;
import com.sflpro.notifier.services.notification.NotificationService;
import com.sflpro.notifier.services.notification.email.EmailNotificationProcessor;
import com.sflpro.notifier.services.notification.exception.UnsupportedNotificationTypeException;
import com.sflpro.notifier.services.notification.push.PushNotificationProcessor;
import com.sflpro.notifier.services.notification.sms.SmsNotificationProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.annotation.Nonnull;
import java.util.Map;

/**
 * User: Ruben Dilanyan
 * Company: SFL LLC
 * Date: 8/24/15
 * Time: 1:07 PM
 */
@Service
public class NotificationProcessingServiceImpl implements NotificationProcessingService {

    private static final Logger LOGGER = LoggerFactory.getLogger(NotificationProcessingServiceImpl.class);

    /* Dependencies */
    @Autowired
    private PushNotificationProcessor pushNotificationProcessor;

    @Autowired
    private SmsNotificationProcessor smsNotificationProcessor;

    @Autowired
    private EmailNotificationProcessor emailNotificationProcessor;

    @Autowired
    private NotificationService notificationService;

    /* Constructors */
    public NotificationProcessingServiceImpl() {
        LOGGER.debug("Initializing notification processing service");
    }

    @Override
    public void processNotification(@Nonnull final Long notificationId, @Nonnull final Map<String, String> secureProperties) {
        Assert.notNull(notificationId, "Notification id should not be null");
        Assert.notNull(secureProperties, "Secure properties map should not be null");
        LOGGER.debug("Processing notification with id - {}", notificationId);
        final Notification notification = notificationService.getNotificationById(notificationId);
        final NotificationProcessor notificationProcessor = getNotificationProcessorForType(notification.getType());
        // Process notification
        notificationProcessor.processNotification(notification.getId(), secureProperties);
        LOGGER.debug("Successfully processed notification with id - {}, notification - {}", notificationId, notification);
    }

    /* Utility methods */
    private NotificationProcessor getNotificationProcessorForType(final NotificationType type) {
        switch (type) {
            case PUSH:
                return pushNotificationProcessor;
            case SMS:
                return smsNotificationProcessor;
            case EMAIL:
                return emailNotificationProcessor;
            default: {
                LOGGER.error("Unsupported notification type - {}", type);
                throw new UnsupportedNotificationTypeException(type);
            }
        }
    }

    /* Properties getters and setters */
    public PushNotificationProcessor getPushNotificationProcessor() {
        return pushNotificationProcessor;
    }

    public void setPushNotificationProcessor(final PushNotificationProcessor pushNotificationProcessor) {
        this.pushNotificationProcessor = pushNotificationProcessor;
    }

    public SmsNotificationProcessor getSmsNotificationProcessor() {
        return smsNotificationProcessor;
    }

    public void setSmsNotificationProcessor(final SmsNotificationProcessor smsNotificationProcessor) {
        this.smsNotificationProcessor = smsNotificationProcessor;
    }

    public NotificationService getNotificationService() {
        return notificationService;
    }

    public void setNotificationService(final NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    public EmailNotificationProcessor getEmailNotificationProcessor() {
        return emailNotificationProcessor;
    }

    public void setEmailNotificationProcessor(final EmailNotificationProcessor emailNotificationProcessor) {
        this.emailNotificationProcessor = emailNotificationProcessor;
    }
}
