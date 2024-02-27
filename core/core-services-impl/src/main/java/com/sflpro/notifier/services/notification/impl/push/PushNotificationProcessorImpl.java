package com.sflpro.notifier.services.notification.impl.push;

import com.sflpro.notifier.db.entities.device.mobile.DeviceOperatingSystemType;
import com.sflpro.notifier.db.entities.notification.NotificationState;
import com.sflpro.notifier.db.entities.notification.email.NotificationProperty;
import com.sflpro.notifier.db.entities.notification.push.PushNotification;
import com.sflpro.notifier.db.entities.notification.push.PushNotificationProviderType;
import com.sflpro.notifier.db.entities.notification.push.PushNotificationRecipient;
import com.sflpro.notifier.db.entities.notification.push.PushNotificationRecipientStatus;
import com.sflpro.notifier.services.common.exception.ServicesRuntimeException;
import com.sflpro.notifier.services.notification.exception.NotificationInvalidStateException;
import com.sflpro.notifier.services.notification.push.PushNotificationProcessor;
import com.sflpro.notifier.services.notification.push.PushNotificationRecipientService;
import com.sflpro.notifier.services.notification.push.PushNotificationService;
import com.sflpro.notifier.spi.exception.PushNotificationInvalidRouteTokenException;
import com.sflpro.notifier.spi.push.PlatformType;
import com.sflpro.notifier.spi.push.PushMessage;
import com.sflpro.notifier.spi.push.PushMessageSender;
import com.sflpro.notifier.spi.push.PushMessageSendingResult;
import com.sflpro.notifier.spi.template.TemplateContent;
import com.sflpro.notifier.spi.template.TemplateContentResolver;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.annotation.Nonnull;
import java.util.*;
import java.util.stream.Collectors;

import static java.lang.String.format;

/**
 * User: Ruben Dilanyan
 * Company: SFL LLC
 * Date: 8/17/15
 * Time: 2:54 PM
 */
@Service
public class PushNotificationProcessorImpl implements PushNotificationProcessor {

    //region Logger

    private static final Logger logger = LoggerFactory.getLogger(PushNotificationProcessorImpl.class);

    //endregion

    //region Injections

    @Autowired
    private PushNotificationService pushNotificationService;

    @Autowired
    private PushMessageServiceProvider pushMessageServiceProvider;

    @Autowired
    private TemplateContentResolver templateContentResolver;

    @Autowired
    private PushNotificationRecipientService pushNotificationRecipientService;

    PushNotificationProcessorImpl() {
        logger.debug("Initializing push notification processing service");
    }

    //endregion

    @Override
    public void processNotification(@Nonnull final Long notificationId, @Nonnull final Map<String, String> secureProperties) {
        Assert.notNull(notificationId, "Push notification id should not be null");
        Assert.notNull(secureProperties, "Secure properties map should not be null");
        logger.debug("Processing push notification for id - {}", notificationId);
        final PushNotification pushNotification = pushNotificationService.getPushNotificationForProcessing(notificationId);
        assertPushNotificationState(pushNotification);
        // Update state to processing
        updatePushNotificationState(notificationId, NotificationState.PROCESSING);
        try {
            // Process push notification
            final String pushNotificationProviderExternalUuid = send(pushNotification, secureProperties);
            // Check if provider uuid is provided
            if (StringUtils.isNotBlank(pushNotificationProviderExternalUuid)) {
                logger.debug("Updating provider uuid for push notification with id - {}, uuid - {}", notificationId, pushNotificationProviderExternalUuid);
                updatePushNotificationExternalUuId(notificationId, pushNotificationProviderExternalUuid);
            }
            // Mark push notification as processed
            updatePushNotificationState(notificationId, NotificationState.SENT);
        }
        catch (final PushNotificationInvalidRouteTokenException ex) {
            logger.warn("Failed to process push notification with id - {}, because of invalid token of recipient with id - {}", notificationId, pushNotification.getRecipient().getId());
            updatePushNotificationState(notificationId, NotificationState.FAILED);
            pushNotificationRecipientService.updatePushNotificationRecipientStatus(
                    pushNotification.getRecipient().getId(),
                    PushNotificationRecipientStatus.DISABLED
            );
        }
        catch (final Exception ex) {
            final String message = "Error occurred while processing push notification with id - " + notificationId;
            updatePushNotificationState(notificationId, NotificationState.FAILED);
            throw new ServicesRuntimeException(message, ex);
        }
    }

    //region Utility methods

    private String send(final PushNotification pushNotification, final Map<String, String> secureProperties) {
        Assert.notNull(pushNotification, "Push notification should not be null");
        final PushNotificationRecipient recipient = pushNotification.getRecipient();
        final PushMessageSender pushMessageSender = getPushMessageSender(recipient.getType());
        final String templateName = pushNotification.getTemplateName();
        final PushMessage pushMessage;
        if (StringUtils.isBlank(templateName)) {
            //process without template
            pushMessage = buildPushMessage(pushNotification, recipient);
        } else {
            //process with template
            pushMessage = buildPushMessageForTemplate(pushNotification, recipient, secureProperties);
        }
        final PushMessageSendingResult pushMessageSendingResult = pushMessageSender.send(pushMessage);
        logger.debug("Sending SNS push notification id - {} , recipient - {}", pushNotification.getId(), recipient);
        return pushMessageSendingResult.messageId();
    }

    private PushMessage buildPushMessage(final PushNotification pushNotification, final PushNotificationRecipient recipient) {
        return PushMessage.of(
                recipient.getDestinationRouteToken(),
                pushNotification.getSubject(),
                pushNotification.getContent(),
                platformType(recipient.getDeviceOperatingSystemType()),
                createPushNotificationAttributes(pushNotification)
        );
    }

    private PushMessage buildPushMessageForTemplate(final PushNotification pushNotification, final PushNotificationRecipient recipient, final Map<String, String> secureProperties) {
        final TemplateContent templateContent;
        final String templateName = pushNotification.getTemplateName();
        final Locale locale = pushNotification.getLocale();
        final Map<String, String> variables = variablesFor(pushNotification, secureProperties);
        if (locale == null) {
            templateContent = templateContentResolver.resolve(templateName, variables);
        } else {
            templateContent = templateContentResolver.resolve(templateName, variables, locale);
        }
        return PushMessage.of(
                recipient.getDestinationRouteToken(),
                templateContent.subject(),
                templateContent.body(),
                platformType(recipient.getDeviceOperatingSystemType()),
                createPushNotificationAttributes(pushNotification)
        );
    }

    private Map<String, String> variablesFor(final PushNotification pushNotification, final Map<String, String> secureProperties) {
        final Map<String, String> variables = pushNotification.getProperties().stream().collect(Collectors.toMap(NotificationProperty::getPropertyKey, NotificationProperty::getPropertyValue));
        variables.putAll(secureProperties);
        return variables;
    }

    private PushMessageSender getPushMessageSender(final PushNotificationProviderType providerType) {
        return pushMessageServiceProvider.lookupPushMessageSender(providerType).orElseThrow(() -> new IllegalStateException(format("No push message sender was registered for type '%s'.", providerType)));
    }

    private static PlatformType platformType(final DeviceOperatingSystemType operatingSystemType) {
        switch (operatingSystemType) {
            case IOS: {
                return PlatformType.APNS;
            }
            case ANDROID: {
                return PlatformType.GCM;
            }
            default: {
                throw new ServicesRuntimeException("Unsupported operating system type - " + operatingSystemType);
            }
        }
    }

    private static Map<String, String> createPushNotificationAttributes(final PushNotification pushNotification) {
        final Map<String, String> attributes = new LinkedHashMap<>();
        pushNotification.getProperties().forEach(pushNotificationProperty -> {
            // Add to the map of attributes
            attributes.put(pushNotificationProperty.getPropertyKey(), pushNotificationProperty.getPropertyValue());
        });
        return attributes;
    }

    private void updatePushNotificationState(final Long notificationId, final NotificationState notificationState) {
        pushNotificationService.updateNotificationState(notificationId, notificationState);
    }

    private void updatePushNotificationExternalUuId(final Long notificationId, final String providerUuId) {
        pushNotificationService.updateProviderExternalUuid(notificationId, providerUuId);
    }

    private static void assertPushNotificationState(final PushNotification pushNotification) {
        final NotificationState notificationState = pushNotification.getState();
        if (!(NotificationState.CREATED.equals(notificationState) || NotificationState.FAILED.equals(notificationState))) {
            logger.error("Push notification with id - {} has invalid state - {}", pushNotification.getId(), notificationState);
            throw new NotificationInvalidStateException(pushNotification.getId(), notificationState, new HashSet<>(Arrays.asList(NotificationState.CREATED, NotificationState.FAILED)));
        }
    }

    //endregion
}
