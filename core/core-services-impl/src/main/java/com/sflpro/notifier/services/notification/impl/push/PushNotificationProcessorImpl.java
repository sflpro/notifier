package com.sflpro.notifier.services.notification.impl.push;

import com.sflpro.notifier.db.entities.device.mobile.DeviceOperatingSystemType;
import com.sflpro.notifier.db.entities.notification.NotificationState;
import com.sflpro.notifier.db.entities.notification.push.PushNotification;
import com.sflpro.notifier.db.entities.notification.push.PushNotificationProviderType;
import com.sflpro.notifier.db.entities.notification.push.PushNotificationRecipient;
import com.sflpro.notifier.services.common.exception.ServicesRuntimeException;
import com.sflpro.notifier.services.notification.exception.NotificationInvalidStateException;
import com.sflpro.notifier.services.notification.push.PushNotificationProcessor;
import com.sflpro.notifier.services.notification.push.PushNotificationService;
import com.sflpro.notifier.spi.push.PlatformType;
import com.sflpro.notifier.spi.push.PushMessage;
import com.sflpro.notifier.spi.push.PushMessageSender;
import com.sflpro.notifier.spi.push.PushMessageSendingResult;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;

import static java.lang.String.format;

/**
 * User: Ruben Dilanyan
 * Company: SFL LLC
 * Date: 8/17/15
 * Time: 2:54 PM
 */
@Service
public class PushNotificationProcessorImpl implements PushNotificationProcessor {

    private static final Logger LOGGER = LoggerFactory.getLogger(PushNotificationProcessorImpl.class);


    /* Dependencies */
    @Autowired
    private PushNotificationService pushNotificationService;

    @Autowired
    private PushMessageServiceProvider pushMessageServiceProvider;

    /* Constructors */
    PushNotificationProcessorImpl() {
        LOGGER.debug("Initializing push notification processing service");
    }

    @Override
    public void processNotification(@Nonnull final Long notificationId, @Nonnull final Map<String, String> secureProperties) {
        Assert.notNull(notificationId, "Push notification id should not be null");
        Assert.notNull(secureProperties, "Secure properties map should not be null");
        LOGGER.debug("Processing push notification for id - {}", notificationId);
        final PushNotification pushNotification = pushNotificationService.getPushNotificationForProcessing(notificationId);
        assertPushNotificationState(pushNotification);
        // Update state to processing
        updatePushNotificationState(notificationId, NotificationState.PROCESSING);
        try {
            // Process push notification
            final String pushNotificationProviderExternalUuid = send(pushNotification);
            // Check if provider uuid is provided
            if (StringUtils.isNotBlank(pushNotificationProviderExternalUuid)) {
                LOGGER.debug("Updating provider uuid for push notification with id - {}, uuid - {}", notificationId, pushNotificationProviderExternalUuid);
                updatePushNotificationExternalUuId(notificationId, pushNotificationProviderExternalUuid);
            }
            // Mark push notification as processed
            updatePushNotificationState(notificationId, NotificationState.SENT);
        } catch (final Exception ex) {
            final String message = "Error occurred while processing push notification with id - " + notificationId;
            LOGGER.error(message, ex);
            updatePushNotificationState(notificationId, NotificationState.FAILED);
            throw new ServicesRuntimeException(message, ex);
        }
    }

    /* Utility methods */
    private String send(final PushNotification pushNotification) {
        Assert.notNull(pushNotification, "Push notification should not be null");
        final PushNotificationRecipient recipient = pushNotification.getRecipient();
        final PushMessageSender pushMessageSender = getPushMessageSender(recipient.getType());
        final PushMessageSendingResult pushMessageSendingResult = pushMessageSender.send(PushMessage.of(
                recipient.getDestinationRouteToken(),
                pushNotification.getSubject(),
                pushNotification.getContent(),
                platformType(recipient.getDeviceOperatingSystemType()),
                createPushNotificationAttributes(pushNotification)
        ));
        LOGGER.debug("Sending SNS push notification - {} , recipient - {}", pushNotification, recipient);
        return pushMessageSendingResult.messageId();
    }

    private PushMessageSender getPushMessageSender(final PushNotificationProviderType providerType) {
        return pushMessageServiceProvider.lookupPushMessageSender(providerType)
                .orElseThrow(() -> new IllegalStateException(format("No push message sender was registered for type '%s'.", providerType)));
    }

    private Map<String, String> createPushNotificationAttributes(final PushNotification pushNotification) {
        final Map<String, String> attributes = new LinkedHashMap<>();
        pushNotification.getProperties().forEach(pushNotificationProperty -> {
            // Add to the map of attributes
            attributes.put(pushNotificationProperty.getPropertyKey(), pushNotificationProperty.getPropertyValue());
        });
        return attributes;
    }

    private PlatformType platformType(final DeviceOperatingSystemType operatingSystemType) {
        switch (operatingSystemType) {
            case IOS:
                return PlatformType.APNS;
            case ANDROID:
                return PlatformType.GCM;
            default: {
                final String message = "Unsupported operating system type - " + operatingSystemType;
                throw new ServicesRuntimeException(message);
            }
        }
    }


    private void updatePushNotificationState(final Long notificationId, final NotificationState notificationState) {
        pushNotificationService.updateNotificationState(notificationId, notificationState);
    }

    private void updatePushNotificationExternalUuId(final Long notificationId, final String providerUuId) {
        pushNotificationService.updateProviderExternalUuid(notificationId, providerUuId);
    }

    private void assertPushNotificationState(final PushNotification pushNotification) {
        final NotificationState notificationState = pushNotification.getState();
        if (!(NotificationState.CREATED.equals(notificationState) || NotificationState.FAILED.equals(notificationState))) {
            LOGGER.error("Push notification with id - {} has invalid state - {}", pushNotification.getId(), notificationState);
            throw new NotificationInvalidStateException(pushNotification.getId(), notificationState, new HashSet<>(Arrays.asList(NotificationState.CREATED, NotificationState.FAILED)));
        }
    }
}
