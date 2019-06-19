package com.sflpro.notifier.services.notification.impl.email;

import com.sflpro.notifier.db.entities.notification.Notification;
import com.sflpro.notifier.db.entities.notification.NotificationProviderType;
import com.sflpro.notifier.db.entities.notification.NotificationState;
import com.sflpro.notifier.db.entities.notification.email.EmailNotification;
import com.sflpro.notifier.db.entities.notification.email.EmailNotificationProperty;
import com.sflpro.notifier.db.repositories.utility.PersistenceUtilityService;
import com.sflpro.notifier.email.SimpleEmailMessage;
import com.sflpro.notifier.email.SimpleEmailSender;
import com.sflpro.notifier.email.TemplatedEmailMessage;
import com.sflpro.notifier.email.TemplatedEmailSender;
import com.sflpro.notifier.services.common.exception.ServicesRuntimeException;
import com.sflpro.notifier.services.notification.email.EmailNotificationProcessor;
import com.sflpro.notifier.services.notification.email.EmailNotificationService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import javax.annotation.Nonnull;
import java.util.Map;
import java.util.stream.Collectors;

import static java.lang.String.format;


/**
 * User: Ruben Dilanyan
 * Company: SFL LLC
 * Date: 1/11/16
 * Time: 11:08 AM
 */
class EmailNotificationProcessorImpl implements EmailNotificationProcessor {

    private static final Logger LOGGER = LoggerFactory.getLogger(EmailNotificationProcessorImpl.class);

    /* Dependencies */
    private final EmailNotificationService emailNotificationService;
    private final PersistenceUtilityService persistenceUtilityService;
    private final EmailSenderProvider emailSenderProvider;;
    /* Constructors */
    EmailNotificationProcessorImpl(final EmailNotificationService emailNotificationService,
                                   final EmailSenderProvider emailSenderProvider, final PersistenceUtilityService persistenceUtilityService) {
        this.emailNotificationService = emailNotificationService;
        this.persistenceUtilityService = persistenceUtilityService;
        this.emailSenderProvider = emailSenderProvider;
    }

    @Override
    public void processNotification(@Nonnull final Long notificationId, @Nonnull final Map<String, String> secureProperties) {
        Assert.notNull(notificationId, "Email notification id should not be null");
        Assert.notNull(secureProperties, "Secure properties map should not be null");
        LOGGER.debug("Sending email notification for id - {}", notificationId);
        /* Retrieve email notification */
        final EmailNotification emailNotification = persistenceUtilityService.initializeAndUnProxy(
                emailNotificationService.getNotificationById(notificationId)
        );
        assertNotificationStateIsCreated(emailNotification);
        LOGGER.debug("Successfully retrieved email notification - {}", emailNotification);
        /* Update notification state to PROCESSING */
        updateEmailNotificationState(emailNotification.getId(), NotificationState.PROCESSING);
        try {
            /* Start processing external email service operation */
            processMessage(emailNotification);
        } catch (final Exception ex) {
            final String message = "Error occurred while sending email notification with id - " + emailNotification.getId();
            LOGGER.error(message, ex);
            /* Update state of notification to NotificationState.FAILED */
            updateEmailNotificationState(emailNotification.getId(), NotificationState.FAILED);
            throw new ServicesRuntimeException(message, ex);
        }
    }

    /* Utility methods */
    private SimpleEmailSender getSimpleEmailSender(final NotificationProviderType providerType) {
        final String providerTypeName = providerType.name().toLowerCase();
        return emailSenderProvider.lookupSimpleEmailSenderFor(providerTypeName)
                .orElseThrow(() -> new IllegalStateException(format("No any email sender was registered for provider '%s", providerTypeName)));
    }

    private TemplatedEmailSender getTemplatedEmailSender(final NotificationProviderType providerType) {
        final String providerTypeName = providerType.name().toLowerCase();
        return emailSenderProvider.lookupTemplatedEmailSenderFor(providerTypeName)
                .orElseThrow(() -> new IllegalStateException(format("No any email sender was registered for provider '%s", providerTypeName)));
    }

    private void processMessage(final EmailNotification emailNotification) {
        if (StringUtils.isNoneBlank(emailNotification.getTemplateName())) {
            getTemplatedEmailSender(emailNotification.getProviderType()).send(TemplatedEmailMessage.of(
                    emailNotification.getSenderEmail(),
                    emailNotification.getRecipientEmail(),
                    emailNotification.getTemplateName(),
                    variablesFor(emailNotification)
            ));
        } else {
            Assert.hasText("Null or emty text was passed as an argument for parameter 'subject'.", emailNotification.getSubject());
            Assert.hasText("Null or emty text was passed as an argument for parameter 'content'.", emailNotification.getContent());
            getSimpleEmailSender(emailNotification.getProviderType()).send(SimpleEmailMessage.of(
                    emailNotification.getSenderEmail(),
                    emailNotification.getRecipientEmail(),
                    emailNotification.getSubject(),
                    emailNotification.getContent()
            ));
        }
        LOGGER.debug("Successfully sent email message for notification with id - {}", emailNotification.getId());
        updateEmailNotificationState(emailNotification.getId(), NotificationState.SENT);
    }

    private Map<String, Object> variablesFor(final EmailNotification emailNotification) {
        return emailNotification.getProperties().stream()
                .collect(Collectors.toMap(
                        EmailNotificationProperty::getPropertyKey, EmailNotificationProperty::getPropertyValue)
                );
    }

    private static void assertNotificationStateIsCreated(final Notification notification) {
        Assert.isTrue(notification.getState().equals(NotificationState.CREATED), "Notification state must be NotificationState.CREATED in order to proceed.");
    }


    private void updateEmailNotificationState(final Long notificationId, final NotificationState notificationState) {
        emailNotificationService.updateNotificationState(notificationId, notificationState);

    }

}
