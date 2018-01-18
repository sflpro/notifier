package com.sflpro.notifier.services.notification.impl.push;

import com.sflpro.notifier.persistence.utility.PersistenceUtilityService;
import com.sflpro.notifier.services.common.exception.ServicesRuntimeException;
import com.sflpro.notifier.services.notification.exception.NotificationInvalidStateException;
import com.sflpro.notifier.services.notification.impl.push.sns.PushNotificationSnsProviderProcessor;
import com.sflpro.notifier.services.notification.model.NotificationState;
import com.sflpro.notifier.services.notification.model.push.PushNotification;
import com.sflpro.notifier.services.notification.model.push.PushNotificationProviderType;
import com.sflpro.notifier.services.notification.model.push.PushNotificationRecipient;
import com.sflpro.notifier.services.notification.push.PushNotificationProcessor;
import com.sflpro.notifier.services.notification.push.PushNotificationService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.HashSet;

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
    private PushNotificationSnsProviderProcessor pushNotificationSnsProcessor;

    @Autowired
    private PersistenceUtilityService persistenceUtilityService;

    /* Constructors */
    public PushNotificationProcessorImpl() {
        LOGGER.debug("Initializing push notification processing service");
    }

    @Override
    public void processNotification(@Nonnull final Long notificationId) {
        Assert.notNull(notificationId, "Push notification id should not be null");
        LOGGER.debug("Processing push notification for id - {}", notificationId);
        final PushNotification pushNotification = pushNotificationService.getNotificationById(notificationId);
        assertPushNotificationState(pushNotification);
        // Update state to processing
        updatePushNotificationState(notificationId, NotificationState.PROCESSING);
        try {
            // Grab push notification recipient
            final PushNotificationRecipient recipient = pushNotification.getRecipient();
            final PushNotificationProviderType providerType = recipient.getType();
            final PushNotificationProviderProcessor pushNotificationProcessor = getPushNotificationProcessor(providerType);
            // Process push notification
            final String pushNotificationProviderExternalUuid = pushNotificationProcessor.processPushNotification(pushNotification);
            // Check if provider uuid is provided
            if (StringUtils.isNotBlank(pushNotificationProviderExternalUuid)) {
                LOGGER.debug("Updating provider uuid for push notification with id - {}, uuid - {}", pushNotification.getId(), pushNotificationProviderExternalUuid);
                updatePushNotificationExternalUuId(pushNotification.getId(), pushNotificationProviderExternalUuid);
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
    private void updatePushNotificationState(final Long notificationId, final NotificationState notificationState) {
        persistenceUtilityService.runInNewTransaction(() -> {
            pushNotificationService.updateNotificationState(notificationId, notificationState);
        });
    }

    private void updatePushNotificationExternalUuId(final Long notificationId, final String providerUuId) {
        persistenceUtilityService.runInNewTransaction(() -> {
            pushNotificationService.updateProviderExternalUuid(notificationId, providerUuId);
        });
    }

    private void assertPushNotificationState(final PushNotification pushNotification) {
        final NotificationState notificationState = pushNotification.getState();
        if (!(NotificationState.CREATED.equals(notificationState) || NotificationState.FAILED.equals(notificationState))) {
            LOGGER.error("Push notification with id - {} has invalid state - {}", pushNotification.getId(), notificationState);
            throw new NotificationInvalidStateException(pushNotification.getId(), notificationState, new HashSet<>(Arrays.asList(NotificationState.CREATED, NotificationState.FAILED)));
        }
    }

    private PushNotificationProviderProcessor getPushNotificationProcessor(final PushNotificationProviderType providerType) {
        switch (providerType) {
            case SNS:
                return pushNotificationSnsProcessor;
            default: {
                final String message = "Unsupported push notification provider type - " + providerType;
                LOGGER.error(message);
                throw new ServicesRuntimeException(message);
            }
        }
    }

    /* Properties getters and setters */
    public PushNotificationService getPushNotificationService() {
        return pushNotificationService;
    }

    public void setPushNotificationService(final PushNotificationService pushNotificationService) {
        this.pushNotificationService = pushNotificationService;
    }

    public PersistenceUtilityService getPersistenceUtilityService() {
        return persistenceUtilityService;
    }

    public void setPersistenceUtilityService(final PersistenceUtilityService persistenceUtilityService) {
        this.persistenceUtilityService = persistenceUtilityService;
    }

    public PushNotificationSnsProviderProcessor getPushNotificationSnsProcessor() {
        return pushNotificationSnsProcessor;
    }

    public void setPushNotificationSnsProcessor(final PushNotificationSnsProviderProcessor pushNotificationSnsProcessor) {
        this.pushNotificationSnsProcessor = pushNotificationSnsProcessor;
    }
}
