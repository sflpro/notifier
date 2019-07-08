package com.sflpro.notifier.services.notification.impl.push;

import com.sflpro.notifier.db.entities.device.mobile.DeviceOperatingSystemType;
import com.sflpro.notifier.db.entities.notification.push.PushNotificationProviderType;
import com.sflpro.notifier.db.entities.notification.push.PushNotificationRecipient;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * User: Ruben Dilanyan
 * Company: SFL LLC
 * Date: 8/18/15
 * Time: 10:06 AM
 */
public interface PushNotificationUserDeviceTokenProcessor {

    /**
     * Registers token with push notification provider
     *
     * @param userDeviceToken
     * @param operatingSystemType
     * @param applicationType
     * @param currentProviderToken
     * @param pushNotificationProviderType
     * @return providerSpecificToken
     */
    String registerUserDeviceToken(@Nonnull final String userDeviceToken, @Nonnull final DeviceOperatingSystemType operatingSystemType, @Nonnull final String applicationType, @Nullable final String currentProviderToken, final PushNotificationProviderType pushNotificationProviderType);

    /**
     * Creates push notification recipient for provider
     *
     * @param subscriptionId
     * @param recipientRouteToken
     * @param operatingSystemType
     * @param applicationType
     * @param providerType
     * @return pushNotificationRecipient
     */
    PushNotificationRecipient createPushNotificationRecipient(@Nonnull final Long subscriptionId, @Nonnull final String recipientRouteToken, @Nonnull final DeviceOperatingSystemType operatingSystemType, @Nonnull final String applicationType, final PushNotificationProviderType providerType);
}
