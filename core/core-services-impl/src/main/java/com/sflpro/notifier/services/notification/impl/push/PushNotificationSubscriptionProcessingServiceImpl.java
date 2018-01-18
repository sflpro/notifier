package com.sflpro.notifier.services.notification.impl.push;

import com.sflpro.notifier.db.entities.device.UserDevice;
import com.sflpro.notifier.db.entities.user.User;
import com.sflpro.notifier.services.common.exception.ServicesRuntimeException;
import com.sflpro.notifier.services.device.UserDeviceService;
import com.sflpro.notifier.db.entities.device.mobile.DeviceOperatingSystemType;
import com.sflpro.notifier.persistence.repositories.notification.push.PushNotificationRecipientSearchFilter;
import com.sflpro.notifier.services.notification.dto.push.PushNotificationSubscriptionDto;
import com.sflpro.notifier.services.notification.dto.push.PushNotificationSubscriptionProcessingParameters;
import com.sflpro.notifier.services.notification.exception.push.PushNotificationSubscriptionInvalidDeviceUserException;
import com.sflpro.notifier.services.notification.impl.push.sns.PushNotificationUserDeviceTokenSnsProcessor;
import com.sflpro.notifier.db.entities.notification.push.PushNotificationProviderType;
import com.sflpro.notifier.db.entities.notification.push.PushNotificationRecipient;
import com.sflpro.notifier.db.entities.notification.push.PushNotificationRecipientStatus;
import com.sflpro.notifier.db.entities.notification.push.PushNotificationSubscription;
import com.sflpro.notifier.services.notification.push.PushNotificationRecipientSearchParameters;
import com.sflpro.notifier.services.notification.push.PushNotificationRecipientService;
import com.sflpro.notifier.services.notification.push.PushNotificationSubscriptionProcessingService;
import com.sflpro.notifier.services.notification.push.PushNotificationSubscriptionService;
import com.sflpro.notifier.services.user.UserService;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.annotation.Nonnull;
import java.util.List;

/**
 * User: Ruben Dilanyan
 * Company: SFL LLC
 * Date: 8/20/15
 * Time: 11:01 AM
 */
@Service
public class PushNotificationSubscriptionProcessingServiceImpl implements PushNotificationSubscriptionProcessingService {

    private static final Logger LOGGER = LoggerFactory.getLogger(PushNotificationSubscriptionProcessingServiceImpl.class);

    /* Constants */
    private static final PushNotificationProviderType ACTIVE_PUSH_NOTIFICATION_PROVIDER = PushNotificationProviderType.SNS;

    /* Dependencies */
    @Autowired
    private UserDeviceService userMobileDeviceService;

    @Autowired
    private UserService userService;

    @Autowired
    private PushNotificationSubscriptionService pushNotificationSubscriptionService;

    @Autowired
    private PushNotificationUserDeviceTokenSnsProcessor pushNotificationUserDeviceTokenSnsProcessor;

    @Autowired
    private PushNotificationRecipientService pushNotificationRecipientService;

    private PushNotificationProviderType activeProvider;

    /* Constructors */
    public PushNotificationSubscriptionProcessingServiceImpl() {
        LOGGER.debug("Initializing push notification subscription processor service");
        this.activeProvider = ACTIVE_PUSH_NOTIFICATION_PROVIDER;
    }

    @Override
    public PushNotificationRecipient processPushNotificationSubscriptionChange(@Nonnull final PushNotificationSubscriptionProcessingParameters parameters) {
        Assert.notNull(parameters, "Push notification subscription processing parameters should not be null");
        Assert.notNull(parameters.getUserId(), "User id should not be null");
        Assert.notNull(parameters.getUserMobileDeviceId(), "User device id should not be null");
        Assert.notNull(parameters.getDeviceToken(), "Device token should not be null");
        Assert.notNull(parameters.getApplicationType(), "Application type should not be null");
        LOGGER.debug("Processing push notification subscription for parameters - {}", parameters);
        final User user = userService.getUserById(parameters.getUserId());
        final UserDevice userDevice = userMobileDeviceService.getUserDeviceById(parameters.getUserMobileDeviceId());
        assertUserDeviceAndUser(user, userDevice);
        final DeviceOperatingSystemType operatingSystemType = userDevice.getOsType();
        final String applicationType = parameters.getApplicationType();
        // Retrieve push notification subscription for user
        final PushNotificationSubscription subscription = getOrCreateSubscriptionForUser(user);
        // Determine current token data to be used
        final Pair<PushNotificationProviderType, String> currentTokenData = determineCurrentNotificationData(parameters.getCurrentPushNotificationProviderType(), parameters.getCurrentProviderToken(), activeProvider);
        final String oldProviderToken = currentTokenData.getValue();
        // Get user device token processor for provider type
        final PushNotificationUserDeviceTokenProcessor deviceTokenProcessor = getPushNotificationUserDeviceTokenProcessorForProviderType(activeProvider);
        // Register user device token with provider
        final String newlyRegisteredProviderToken = deviceTokenProcessor.registerUserDeviceToken(parameters.getDeviceToken(), operatingSystemType, applicationType, oldProviderToken);
        // Disable all push notification recipients with same token
        disableAllRecipientsWithProviderTokenExceptCurrentSubscriptionIfProvided(subscription, newlyRegisteredProviderToken, activeProvider, operatingSystemType, applicationType);
        // Enable or create push notification recipient
        PushNotificationRecipient recipient = getOrCreateRecipientForProviderToken(subscription, newlyRegisteredProviderToken, activeProvider, operatingSystemType, applicationType);
        // Update push notification status
        recipient = updatePushNotificationRecipientStatus(recipient, parameters.isSubscribe());
        // Update user device for recipient
        recipient = pushNotificationRecipientService.updatePushNotificationRecipientUserDevice(recipient.getId(), userDevice.getId());
        // Verify and if required disable olf provider token
        verifyAndDisableOldProviderTokenIfRequired(oldProviderToken, newlyRegisteredProviderToken, activeProvider, operatingSystemType, applicationType);
        LOGGER.debug("Successfully finalized push notification subscription processing for parameters - {}, processing result recipient - {}", parameters, recipient);
        return recipient;
    }

    /* Utility methods */
    private PushNotificationRecipient updatePushNotificationRecipientStatus(final PushNotificationRecipient recipient, final boolean subscribe) {
        PushNotificationRecipient result = recipient;
        if (subscribe) {
            // Enable push notification if required
            result = enablePushNotificationRecipientIfRequired(recipient);
        } else {
            // Enable push notification if required
            result = disablePushNotificationRecipientIfRequired(recipient);
        }
        return result;
    }

    private PushNotificationRecipient enablePushNotificationRecipientIfRequired(final PushNotificationRecipient recipient) {
        PushNotificationRecipient result = recipient;
        // Make sure that recipient is enabled
        if (!PushNotificationRecipientStatus.ENABLED.equals(result.getStatus())) {
            result = pushNotificationRecipientService.updatePushNotificationRecipientStatus(recipient.getId(), PushNotificationRecipientStatus.ENABLED);
        }
        return result;
    }

    private PushNotificationRecipient disablePushNotificationRecipientIfRequired(final PushNotificationRecipient recipient) {
        PushNotificationRecipient result = recipient;
        // Make sure that recipient is enabled
        if (PushNotificationRecipientStatus.ENABLED.equals(result.getStatus())) {
            result = pushNotificationRecipientService.updatePushNotificationRecipientStatus(recipient.getId(), PushNotificationRecipientStatus.DISABLED);
        }
        return result;
    }

    private void verifyAndDisableOldProviderTokenIfRequired(final String oldProviderToken, final String newPushNotificationProviderToken, final PushNotificationProviderType activeProvider, final DeviceOperatingSystemType operatingSystemType, final String applicationType) {
        if (oldProviderToken != null && !newPushNotificationProviderToken.equalsIgnoreCase(oldProviderToken)) {
            LOGGER.debug("Disabling all recipients with old push notification provider token - {} since it is replaced with new provider token - {} for provider - {} and mobile OS - {}", oldProviderToken, newPushNotificationProviderToken, activeProvider, operatingSystemType);
            disableAllRecipientsWithProviderTokenExceptCurrentSubscriptionIfProvided(null, oldProviderToken, activeProvider, operatingSystemType, applicationType);
        }
    }

    private PushNotificationRecipient getOrCreateRecipientForProviderToken(final PushNotificationSubscription currentSubscription, final String pushNotificationProviderToken, final PushNotificationProviderType activeProvider, final DeviceOperatingSystemType operatingSystemType, final String applicationType) {
        // Push notification recipient to be returned
        PushNotificationRecipient recipient;
        // Create recipients search parameters
        final PushNotificationRecipientSearchParameters parameters = new PushNotificationRecipientSearchParameters();
        parameters.setProviderType(activeProvider);
        parameters.setDeviceOperatingSystemType(operatingSystemType);
        parameters.setDestinationRouteToken(pushNotificationProviderToken);
        parameters.setSubscriptionId(currentSubscription.getId());
        parameters.setApplicationType(applicationType);
        // Execute search, there might be only one recipient with type,token,subscription combination
        final List<PushNotificationRecipient> recipients = pushNotificationRecipientService.getPushNotificationRecipientsForSearchParameters(parameters, Long.valueOf(0), Integer.valueOf(1));
        if (recipients.size() != 0) {
            // Retrieve recipient and update state if required
            recipient = recipients.get(0);
            LOGGER.debug("Successfully retrieved push notification recipient with id - {} for subscription with id - {}, push notification provider token - {}, mobile device operating system type - {}. Recipient - {}", recipient.getId(), currentSubscription.getId(), pushNotificationProviderToken, operatingSystemType, recipient);
        } else {
            LOGGER.debug("No push notification recipient exists for subscription with id - {}, push notification provider token - {}, mobile device operating system type - {}. Creating new one", currentSubscription.getId(), pushNotificationProviderToken, operatingSystemType);
            // Create new push notification recipient
            final PushNotificationUserDeviceTokenProcessor deviceTokenProcessor = getPushNotificationUserDeviceTokenProcessorForProviderType(activeProvider);
            recipient = deviceTokenProcessor.createPushNotificationRecipient(currentSubscription.getId(), pushNotificationProviderToken, operatingSystemType, applicationType);
        }
        LOGGER.debug("Successfully retrieved/created push notification recipient with id - {} for subscription with id - {}, push notification provider token - {}, mobile device operating system type - {}. Recipient - {}", recipient.getId(), currentSubscription.getId(), pushNotificationProviderToken, operatingSystemType, recipient);
        return recipient;
    }

    private void disableAllRecipientsWithProviderTokenExceptCurrentSubscriptionIfProvided(final PushNotificationSubscription currentSubscription, final String pushNotificationProviderToken, final PushNotificationProviderType activeProvider, final DeviceOperatingSystemType operatingSystemType, final String applicationType) {
        // Create recipients search parameters
        final PushNotificationRecipientSearchParameters parameters = new PushNotificationRecipientSearchParameters();
        parameters.setProviderType(activeProvider);
        parameters.setDeviceOperatingSystemType(operatingSystemType);
        parameters.setStatus(PushNotificationRecipientStatus.ENABLED);
        parameters.setDestinationRouteToken(pushNotificationProviderToken);
        parameters.setApplicationType(applicationType);
        // Execute search
        final List<PushNotificationRecipient> recipients = pushNotificationRecipientService.getPushNotificationRecipientsForSearchParameters(parameters, Long.valueOf(0), Integer.MAX_VALUE);
        recipients.forEach(recipient -> {
            if (currentSubscription == null || !currentSubscription.getId().equals(recipient.getSubscription().getId())) {
                pushNotificationRecipientService.updatePushNotificationRecipientStatus(recipient.getId(), PushNotificationRecipientStatus.DISABLED);
            }
        });
    }

    private PushNotificationUserDeviceTokenProcessor getPushNotificationUserDeviceTokenProcessorForProviderType(final PushNotificationProviderType providerType) {
        switch (providerType) {
            case SNS:
                return pushNotificationUserDeviceTokenSnsProcessor;
            default: {
                final String message = "Unsupported push notification provider type - " + providerType;
                LOGGER.error(message);
                throw new ServicesRuntimeException(message);
            }
        }
    }

    private Pair<PushNotificationProviderType, String> determineCurrentNotificationData(final PushNotificationProviderType currentProviderType, final String currentProviderToken, final PushNotificationProviderType activeProvider) {
        PushNotificationProviderType currentProviderTypeToBeUsed = null;
        String currentProviderTokenToBeUsed = null;
        // Check if current token data is defined and if current provider matches with configured provider
        if (currentProviderType != null && currentProviderToken != null && currentProviderType.equals(activeProvider)) {
            // Data can be reused
            currentProviderTypeToBeUsed = currentProviderType;
            currentProviderTokenToBeUsed = currentProviderToken;
        }
        // Return result
        return new ImmutablePair<>(currentProviderTypeToBeUsed, currentProviderTokenToBeUsed);
    }

    private PushNotificationSubscription getOrCreateSubscriptionForUser(final User user) {
        PushNotificationSubscription subscription = null;
        final boolean subscriptionExists = pushNotificationSubscriptionService.checkIfPushNotificationSubscriptionExistsForUser(user.getId());
        if (subscriptionExists) {
            LOGGER.debug("Push notification subscription already exists for user with id - {}, retrieving it", user.getId());
            subscription = pushNotificationSubscriptionService.getPushNotificationSubscriptionForUser(user.getId());
        } else {
            LOGGER.debug("No push notification subscription exists for user with id - {}, creating it", user.getId());
            subscription = pushNotificationSubscriptionService.createPushNotificationSubscription(user.getId(), new PushNotificationSubscriptionDto());
        }
        LOGGER.debug("Using push notification subscription with id - {} for user with id - {}", subscription.getId(), user.getId());
        return subscription;
    }

    private void assertUserDeviceAndUser(final User user, final UserDevice userDevice) {
        final Long userId = user.getId();
        final Long deviceUserId = userDevice.getUser().getId();
        if (!userId.equals(deviceUserId)) {
            LOGGER.error("User device with id - {} belongs to user with id - {}, where as current user id is - {}", userDevice.getId(), deviceUserId, userId);
            throw new PushNotificationSubscriptionInvalidDeviceUserException(userId, userDevice.getId(), deviceUserId);
        }
    }

    /* Properties getters and setters */
    public UserDeviceService getUserMobileDeviceService() {
        return userMobileDeviceService;
    }

    public void setUserMobileDeviceService(final UserDeviceService userMobileDeviceService) {
        this.userMobileDeviceService = userMobileDeviceService;
    }

    public UserService getUserService() {
        return userService;
    }

    public void setUserService(final UserService userService) {
        this.userService = userService;
    }

    public PushNotificationSubscriptionService getPushNotificationSubscriptionService() {
        return pushNotificationSubscriptionService;
    }

    public void setPushNotificationSubscriptionService(final PushNotificationSubscriptionService pushNotificationSubscriptionService) {
        this.pushNotificationSubscriptionService = pushNotificationSubscriptionService;
    }

    public PushNotificationUserDeviceTokenSnsProcessor getPushNotificationUserDeviceTokenSnsProcessor() {
        return pushNotificationUserDeviceTokenSnsProcessor;
    }

    public void setPushNotificationUserDeviceTokenSnsProcessor(final PushNotificationUserDeviceTokenSnsProcessor pushNotificationUserDeviceTokenSnsProcessor) {
        this.pushNotificationUserDeviceTokenSnsProcessor = pushNotificationUserDeviceTokenSnsProcessor;
    }

    public PushNotificationRecipientService getPushNotificationRecipientService() {
        return pushNotificationRecipientService;
    }

    public void setPushNotificationRecipientService(final PushNotificationRecipientService pushNotificationRecipientService) {
        this.pushNotificationRecipientService = pushNotificationRecipientService;
    }

    public PushNotificationProviderType getActiveProvider() {
        return activeProvider;
    }

    public void setActiveProvider(final PushNotificationProviderType activeProvider) {
        this.activeProvider = activeProvider;
    }
}
