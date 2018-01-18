package com.sflpro.notifier.services.notification.impl.sms;

import com.sflpro.notifier.externalclients.sms.twillio.communicator.TwillioApiCommunicator;
import com.sflpro.notifier.externalclients.sms.twillio.model.request.SendMessageRequest;
import com.sflpro.notifier.externalclients.sms.twillio.model.response.SendMessageResponse;
import com.sflpro.notifier.persistence.utility.PersistenceUtilityService;
import com.sflpro.notifier.services.common.exception.ServicesRuntimeException;
import com.sflpro.notifier.services.notification.model.Notification;
import com.sflpro.notifier.services.notification.model.NotificationProviderType;
import com.sflpro.notifier.services.notification.model.NotificationState;
import com.sflpro.notifier.db.entities.notification.sms.SmsNotification;
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

/**
 * User: Mher Sargsyan
 * Company: SFL LLC
 * Date: 4/10/15
 * Time: 12:52 PM
 */
@Service
public class SmsNotificationProcessorImpl implements SmsNotificationProcessor {

    private static final Logger LOGGER = LoggerFactory.getLogger(SmsNotificationProcessorImpl.class);

    /* Dependencies */
    @Autowired
    private SmsNotificationService smsNotificationService;

    @Autowired
    private PersistenceUtilityService persistenceUtilityService;

    @Autowired
    private TwillioApiCommunicator twillioApiCommunicator;

    /* Properties */
    @Value("#{appProperties['twillio.account.sender.phone']}")
    private String accountSenderNumber;

    /* Constructors */
    public SmsNotificationProcessorImpl() {
    }

    /* Public method overrides */
    @Override
    public void processNotification(@Nonnull final Long notificationId) {
        Assert.notNull(notificationId, "Sms notification id should not be null");
        /* Retrieve sms notification */
        final SmsNotification smsNotification = smsNotificationService.getNotificationById(notificationId);
        assertNotificationStateIsCreated(smsNotification);
        LOGGER.debug("Successfully retrieved sms notification - {}", smsNotification);
        /* Update notification state to PROCESSING */
        updateSmsNotificationState(smsNotification.getId(), NotificationState.PROCESSING);
        /* Start processing external sms service operation */
        try {
            final SmsServiceApiOperationsHandler smsServiceApiOperationsHandler = getSmsServiceApiOperationsHandler(NotificationProviderType.TWILLIO);
            final String smsMessageProviderExternalId = smsServiceApiOperationsHandler.sendMessage(getAccountSenderNumber(), smsNotification.getRecipientMobileNumber(), smsNotification.getContent());
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
        persistenceUtilityService.runInNewTransaction(() -> {
            smsNotificationService.updateProviderExternalUuid(notificationId, providerUuId);
        });
    }

    private void assertNotificationStateIsCreated(final Notification notification) {
        Assert.isTrue(notification.getState().equals(NotificationState.CREATED), "Notification state must be NotificationState.CREATED in order to proceed.");
    }

    private void updateSmsNotificationState(final Long notificationId, final NotificationState notificationState) {
        persistenceUtilityService.runInNewTransaction(() -> {
            smsNotificationService.updateNotificationState(notificationId, notificationState);
        });
    }

    private SmsServiceApiOperationsHandler getSmsServiceApiOperationsHandler(final NotificationProviderType notificationProviderType) {
        switch (notificationProviderType) {
            case TWILLIO: {
                return new TwillioSmsServiceApiOperationsHandler();
            }
            default: {
                LOGGER.debug("Sms messaging provider with type - {} is  not supported, use - {}", notificationProviderType, NotificationProviderType.TWILLIO);
                throw new ServicesRuntimeException("Sms messaging provider with type - " + notificationProviderType + " is not supported");
            }
        }
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

    public TwillioApiCommunicator getTwillioApiCommunicator() {
        return twillioApiCommunicator;
    }

    public void setTwillioApiCommunicator(final TwillioApiCommunicator twillioApiCommunicator) {
        this.twillioApiCommunicator = twillioApiCommunicator;
    }

    public PersistenceUtilityService getPersistenceUtilityService() {
        return persistenceUtilityService;
    }

    public void setPersistenceUtilityService(final PersistenceUtilityService persistenceUtilityService) {
        this.persistenceUtilityService = persistenceUtilityService;
    }

    /* Inner classes */
    private interface SmsServiceApiOperationsHandler {

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

    private class TwillioSmsServiceApiOperationsHandler implements SmsServiceApiOperationsHandler {

        /* Constructors */
        public TwillioSmsServiceApiOperationsHandler() {
        }

        @Nonnull
        @Override
        public String sendMessage(@Nonnull final String senderNumber, @Nonnull final String recipientNumber, @Nonnull final String messageBody) {
            /* Create send message request model */
            final SendMessageRequest sendMessageRequest = new SendMessageRequest(senderNumber, recipientNumber, messageBody);
            LOGGER.debug("Sending sms message with request model - {}", sendMessageRequest);
            final SendMessageResponse sendMessageResponse = getTwillioApiCommunicator().sendMessage(sendMessageRequest);
            LOGGER.debug("Successfully sent sms message, response - {}", sendMessageResponse);
            return sendMessageResponse.getSid();
        }
    }
}
