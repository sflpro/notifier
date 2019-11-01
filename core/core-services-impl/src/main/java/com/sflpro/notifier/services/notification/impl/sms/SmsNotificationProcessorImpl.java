package com.sflpro.notifier.services.notification.impl.sms;

import com.sflpro.notifier.db.entities.notification.Notification;
import com.sflpro.notifier.db.entities.notification.NotificationProviderType;
import com.sflpro.notifier.db.entities.notification.NotificationState;
import com.sflpro.notifier.db.entities.notification.email.NotificationProperty;
import com.sflpro.notifier.db.entities.notification.sms.SmsNotification;
import com.sflpro.notifier.services.common.exception.ServicesRuntimeException;
import com.sflpro.notifier.services.notification.sms.SmsNotificationProcessor;
import com.sflpro.notifier.services.notification.sms.SmsNotificationService;
import com.sflpro.notifier.spi.sms.*;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.annotation.Nonnull;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.lang.String.format;

/**
 * User: Mher Sargsyan
 * Company: SFL LLC
 * Date: 4/10/15
 * Time: 12:52 PM
 */
@Service
class SmsNotificationProcessorImpl implements SmsNotificationProcessor {

    private static final Logger logger = LoggerFactory.getLogger(SmsNotificationProcessorImpl.class);

    /* Dependencies */
    private final SmsNotificationService smsNotificationService;
    private final SmsSenderProvider smsSenderProvider;

    /* Properties */
    private final String senderName;

    /* Constructors */
    SmsNotificationProcessorImpl(final SmsNotificationService smsNotificationService,
                                 final SmsSenderProvider smsSenderProvider,
                                 @Value("${sms.sender:}") final String senderName) {
        super();
        this.smsNotificationService = smsNotificationService;
        this.smsSenderProvider = smsSenderProvider;
        this.senderName = senderName;
        if (StringUtils.isBlank(senderName)) {
            logger.warn("Nothing was configured for sms sender name. If you are not going to send any sms, then this bean shouldn't be initialized at all.");
        }
    }

    /* Public method overrides */
    @Override
    public void processNotification(@Nonnull final Long notificationId, @Nonnull final Map<String, String> secureProperties) {
        Assert.notNull(notificationId, "Sms notification id should not be null");
        Assert.notNull(secureProperties, "Secure properties map should not be null");
        /* Retrieve sms notification */
        final SmsNotification smsNotification =
                smsNotificationService.getSmsNotificationForProcessing(notificationId);
        assertNotificationStateIsCreated(smsNotification);
        logger.debug("Successfully retrieved sms notification - {}", smsNotification);
        /* Update notification state to PROCESSING */
        updateSmsNotificationState(smsNotification.getId(), NotificationState.PROCESSING);
        /* Start processing external sms service operation */
        try {
            processSending(smsNotification, secureProperties);
        } catch (final Exception e) {
            final String message = "Error occurred while sending sms message to recipient - " + smsNotification.getRecipientMobileNumber();
            logger.error(message, e);
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

    private void processSending(final SmsNotification smsNotification, final Map<String, String> secureProperties) {
        final String smsMessageProviderExternalId = send(smsNotification, secureProperties);
        logger.debug("Successfully sent sms message to recipient - {}, with body - {}", smsNotification.getRecipientMobileNumber(), smsNotification.getContent());
        /* Update message external id if it is provided */
        if (StringUtils.isNotBlank(smsMessageProviderExternalId)) {
            updateSmsNotificationExternalUuId(smsNotification.getId(), smsMessageProviderExternalId);
        }
        /* Update state of notification to NotificationState.SENT */
        updateSmsNotificationState(smsNotification.getId(), NotificationState.SENT);
    }

    private String send(final SmsNotification smsNotification, final Map<String, String> secureProperties) {
        if (StringUtils.isNoneBlank(smsNotification.getTemplateName())) {
            return getSmsSender(smsSenderProvider::lookupTemplatedSmsMessageSenderFor,
                    smsNotification.getProviderType()).sendMessage(
                    templatedSmsMessage(smsNotification, secureProperties)
            );
        } else {
            return getSmsSender(smsSenderProvider::lookupSimpleSmsMessageSenderFor,
                    smsNotification.getProviderType()).sendMessage(
                    SimpleSmsMessage.of(
                            smsNotification.getId(),
                            senderName,
                            smsNotification.getRecipientMobileNumber(),
                            smsNotification.getContent()
                    )
            );
        }

    }

    private TemplatedSmsMessage templatedSmsMessage(final SmsNotification smsNotification, final Map<String, String> secureProperties) {
        if (smsNotification.getLocale() == null) {
            return TemplatedSmsMessage.of(
                    smsNotification.getId(),
                    senderName,
                    smsNotification.getRecipientMobileNumber(),
                    smsNotification.getTemplateName(),
                    variables(smsNotification, secureProperties)
            );
        } else {
            return TemplatedSmsMessage.of(
                    smsNotification.getId(),
                    senderName,
                    smsNotification.getRecipientMobileNumber(),
                    smsNotification.getTemplateName(),
                    variables(smsNotification, secureProperties),
                    smsNotification.getLocale()
            );
        }
    }

    private Map<String, String> variables(final SmsNotification smsNotification, final Map<String, String> secureProperties) {
        final Map<String, String> variables = smsNotification.getProperties().stream()
                .collect(Collectors.toMap(NotificationProperty::getPropertyKey,
                        NotificationProperty::getPropertyValue));
        variables.putAll(secureProperties);
        return variables;
    }

    private <M extends SmsMessage> MessageSender<M> getSmsSender(final Function<String, Optional<? extends SmsSender<M>>> senderProvider,
                                                                 final NotificationProviderType notificationProviderType) {
        final String providerType = notificationProviderType.name().toLowerCase();
        return senderProvider.apply(providerType)
                .map(MessageSenderAdpater::new)
                .orElseThrow(() -> new IllegalStateException(format("Sms messaging provider with type - '%s' is not supported", providerType)));
    }

    /* Inner classes */
    private interface MessageSender<M extends SmsMessage> {
        @Nonnull
        String sendMessage(@Nonnull final M message);
    }

    private static final class MessageSenderAdpater<M extends SmsMessage> implements MessageSender<M> {

        private final SmsSender<M> smsSender;

        /* Constructors */
        MessageSenderAdpater(final SmsSender<M> smsSender) {
            super();
            this.smsSender = smsSender;
        }

        @Nonnull
        @Override
        public String sendMessage(@Nonnull final M message) {
            /* Send message request model */
            logger.debug("Sending sms message - {}", message);
            final SmsMessageSendingResult sendingResult = smsSender.send(message);
            logger.debug("Successfully sent sms message, response - {}", sendingResult);
            return sendingResult.sid();
        }
    }
}
