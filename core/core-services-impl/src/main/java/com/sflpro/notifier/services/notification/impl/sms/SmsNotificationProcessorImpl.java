package com.sflpro.notifier.services.notification.impl.sms;

import com.sflpro.notifier.db.entities.notification.Notification;
import com.sflpro.notifier.db.entities.notification.NotificationProviderType;
import com.sflpro.notifier.db.entities.notification.NotificationState;
import com.sflpro.notifier.db.entities.notification.sms.SmsNotification;
import com.sflpro.notifier.db.repositories.utility.PersistenceUtilityService;
import com.sflpro.notifier.services.common.exception.ServicesRuntimeException;
import com.sflpro.notifier.sms.SmsMessage;
import com.sflpro.notifier.sms.SmsMessageSendingResult;
import com.sflpro.notifier.sms.SmsSender;
import com.sflpro.notifier.services.notification.sms.SmsNotificationProcessor;
import com.sflpro.notifier.services.notification.sms.SmsNotificationService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.annotation.Nonnull;
import java.util.Map;

import static java.lang.String.format;

/**
 * User: Mher Sargsyan
 * Company: SFL LLC
 * Date: 4/10/15
 * Time: 12:52 PM
 */
@Service
class SmsNotificationProcessorImpl implements SmsNotificationProcessor {

    private static final Logger LOGGER = LoggerFactory.getLogger(SmsNotificationProcessorImpl.class);

    /* Dependencies */
    @Autowired
    private SmsNotificationService smsNotificationService;

    @Autowired
    private PersistenceUtilityService persistenceUtilityService;

    @Autowired
    private SmsSenderProvider smsSenderProvider;

    /* Properties */
    @Value("${sms.account.sender.phone}")
    private String accountSenderNumber;

    /* Constructors */
    SmsNotificationProcessorImpl() {
        super();
    }

    /* Public method overrides */
    @Override
    public void processNotification(@Nonnull final Long notificationId, @Nonnull final Map<String, String> secureProperties) {
        Assert.notNull(notificationId, "Sms notification id should not be null");
        Assert.notNull(secureProperties, "Secure properties map should not be null");
        /* Retrieve sms notification */
        final SmsNotification smsNotification = smsNotificationService.getNotificationById(notificationId);
        assertNotificationStateIsCreated(smsNotification);
        LOGGER.debug("Successfully retrieved sms notification - {}", smsNotification);
        /* Update notification state to PROCESSING */
        updateSmsNotificationState(smsNotification.getId(), NotificationState.PROCESSING);
        /* Start processing external sms service operation */
        try {
            final MessageSender sender = getSmsServiceApiOperationsHandlerr(smsNotification.getProviderType());
            final String smsMessageProviderExternalId = sender.sendMessage(
                    getAccountSenderNumber(),
                    smsNotification.getRecipientMobileNumber(),
                    smsNotification.getContent()
            );
            LOGGER.debug("Successfully sent sms message to recipient - {}, with body - {}", smsNotification.getRecipientMobileNumber(), smsNotification.getContent());
            /* Update message external id if it is provided */
            if (StringUtils.isNotBlank(smsMessageProviderExternalId)) {
                updateSmsNotificationExternalUuId(smsNotification.getId(), smsMessageProviderExternalId);
            }
            /* Update state of notification to NotificationState.SENT */
            updateSmsNotificationState(smsNotification.getId(), NotificationState.SENT);
        } catch (final Exception e) {
            final String message = "Error occurred while sending sms message to recipient - " + smsNotification.getRecipientMobileNumber();
            LOGGER.error(message, e);
            /* Update state of notification to NotificationState.FAILED */
            updateSmsNotificationState(smsNotification.getId(), NotificationState.FAILED);
            throw new ServicesRuntimeException(message, e);
        }
    }

    /* Utility methods */
    private void updateSmsNotificationExternalUuId(final Long notificationId, final String providerUuId) {
        smsNotificationService.updateProviderExternalUuid(notificationId, providerUuId);
    }

    private void assertNotificationStateIsCreated(final Notification notification) {
        Assert.isTrue(notification.getState().equals(NotificationState.CREATED), "Notification state must be NotificationState.CREATED in order to proceed.");
    }

    private void updateSmsNotificationState(final Long notificationId, final NotificationState notificationState) {
        smsNotificationService.updateNotificationState(notificationId, notificationState);
    }

    private MessageSender getSmsServiceApiOperationsHandlerr(final NotificationProviderType notificationProviderType) {
        final String providerType = notificationProviderType.name().toLowerCase();
        return smsSenderProvider
                .lookupSenderFor(NotificationProviderType.TWILLIO.name().toLowerCase())
                .map(MessageSenderAdpater::new)
                .orElseThrow(() -> new IllegalStateException(format("Sms messaging provider with type - '%s' is not supported", providerType)));
    }

    /* Properties getters and setters */
    public String getAccountSenderNumber() {
        return accountSenderNumber;
    }

    public void setAccountSenderNumber(final String accountSenderNumber) {
        this.accountSenderNumber = accountSenderNumber;
    }

    /* Dependencies Getters and setters */
    public SmsNotificationService getSmsNotificationService() {
        return smsNotificationService;
    }

    public void setSmsNotificationService(final SmsNotificationService smsNotificationService) {
        this.smsNotificationService = smsNotificationService;
    }

    public PersistenceUtilityService getPersistenceUtilityService() {
        return persistenceUtilityService;
    }

    public void setPersistenceUtilityService(final PersistenceUtilityService persistenceUtilityService) {
        this.persistenceUtilityService = persistenceUtilityService;
    }

    /* Inner classes */
    private interface MessageSender {

        /**
         * Handle sms message sending operation through external service
         *
         * @param senderNumber
         * @param recipientNumber
         * @param messageBody
         * @return messageExternalId
         */
        @Nonnull
        String sendMessage(@Nonnull final String senderNumber, @Nonnull final String recipientNumber, @Nonnull final String messageBody);
    }

    private static final class MessageSenderAdpater implements MessageSender {

        private final SmsSender smsSender;

        /* Constructors */
        MessageSenderAdpater(final SmsSender smsSender) {
            super();
            this.smsSender = smsSender;
        }

        @Nonnull
        @Override
        public String sendMessage(@Nonnull final String senderNumber, @Nonnull final String recipientNumber, @Nonnull final String messageBody) {
            /* Create send message request model */
            final SmsMessage message = SmsMessage.of(senderNumber, recipientNumber, messageBody);
            LOGGER.debug("Sending sms message - {}", message);
            final SmsMessageSendingResult sendingResult = smsSender.send(message);
            LOGGER.debug("Successfully sent sms message, response - {}", sendingResult);
            return sendingResult.sid();
        }
    }
}
