package com.sflpro.notifier.services.notification.impl.email;

import com.sflpro.notifier.db.entities.notification.Notification;
import com.sflpro.notifier.db.entities.notification.NotificationState;
import com.sflpro.notifier.db.entities.notification.email.EmailNotification;
import com.sflpro.notifier.db.repositories.utility.PersistenceUtilityService;
import com.sflpro.notifier.services.common.exception.ServicesRuntimeException;
import com.sflpro.notifier.services.notification.email.EmailNotificationProcessor;
import com.sflpro.notifier.services.notification.email.EmailNotificationService;
import com.sflpro.notifier.services.notification.impl.email.mandrill.EmailNotificationMandrillProviderProcessor;
import com.sflpro.notifier.services.notification.impl.email.smtp.EmailNotificationSmtpProviderProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import javax.annotation.Nonnull;

/**
 * User: Ruben Dilanyan
 * Company: SFL LLC
 * Date: 1/11/16
 * Time: 11:08 AM
 */
@Component
public class EmailNotificationProcessorImpl implements EmailNotificationProcessor {

    private static final Logger LOGGER = LoggerFactory.getLogger(EmailNotificationProcessorImpl.class);

    /* Dependencies */
    @Autowired
    private EmailNotificationService emailNotificationService;

    @Autowired
    private PersistenceUtilityService persistenceUtilityService;

    @Autowired
    private EmailNotificationSmtpProviderProcessor emailNotificationSmtpProviderProcessor;

    @Autowired(required = false)
    private EmailNotificationMandrillProviderProcessor emailNotificationMandrillProviderProcessor;

    /* Constructors */
    public EmailNotificationProcessorImpl() {
        super();
    }


    @Override
    public void processNotification(@Nonnull final Long notificationId) {
        Assert.notNull(notificationId, "Email notification id should not be null");
        LOGGER.debug("Sending email notification for id - {}", notificationId);
        /* Retrieve email notification */
        final EmailNotification emailNotification = emailNotificationService.getNotificationById(notificationId);
        assertNotificationStateIsCreated(emailNotification);
        LOGGER.debug("Successfully retrieved email notification - {}", emailNotification);
        /* Update notification state to PROCESSING */
        updateEmailNotificationState(emailNotification.getId(), NotificationState.PROCESSING);
        try {
            final EmailNotificationProviderProcessor emailNotificationProviderProcessor = getEmailNotificationProviderProcessor(emailNotification);
            final boolean success = emailNotificationProviderProcessor.processEmailNotification(emailNotification);
            /* Update state of notification to NotificationState.SENT */
            updateEmailNotificationState(emailNotification.getId(), success ? NotificationState.SENT : NotificationState.FAILED);
        } catch (final Exception ex) {
            final String message = "Error occurred while sending email notification with id - " + emailNotification.getId();
            LOGGER.error(message, ex);
            /* Update state of notification to NotificationState.FAILED */
            updateEmailNotificationState(emailNotification.getId(), NotificationState.FAILED);
            throw new ServicesRuntimeException(message, ex);
        }
    }

    /* Utility methods */
    private EmailNotificationProviderProcessor getEmailNotificationProviderProcessor(final EmailNotification notification) {
        switch (notification.getProviderType()) {
            case SMTP_SERVER:
                return emailNotificationSmtpProviderProcessor;
            case MANDRILL:
                return emailNotificationMandrillProviderProcessor;
            default: {
                LOGGER.error("Unknown email notification provider type - {}", notification.getProviderType());
                throw new ServicesRuntimeException("Unknown email notification provider type - " + notification.getProviderType());
            }
        }
    }

    private void assertNotificationStateIsCreated(final Notification notification) {
        Assert.isTrue(notification.getState().equals(NotificationState.CREATED), "Notification state must be NotificationState.CREATED in order to proceed.");
    }

    private void updateEmailNotificationState(final Long notificationId, final NotificationState notificationState) {
        emailNotificationService.updateNotificationState(notificationId, notificationState);
    }
}
