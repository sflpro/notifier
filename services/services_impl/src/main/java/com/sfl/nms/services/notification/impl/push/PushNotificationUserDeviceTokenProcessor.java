package com.sfl.nms.services.notification.impl.push;

import com.sfl.nms.services.device.model.mobile.DeviceOperatingSystemType;
import com.sfl.nms.services.notification.model.push.PushNotificationRecipient;

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
     * @return providerSpecificToken
     */
    String registerUserDeviceToken(@Nonnull final String userDeviceToken, @Nonnull final DeviceOperatingSystemType operatingSystemType, @Nonnull final String applicationType, @Nullable final String currentProviderToken);

    /**
     * Creates push notification recipient for provider
     *
     * @param subscriptionId
     * @param recipientRouteToken
     * @param operatingSystemType
     * @param applicationType
     * @return pushNotificationRecipient
     */
    PushNotificationRecipient createPushNotificationRecipient(@Nonnull final Long subscriptionId, @Nonnull final String recipientRouteToken, @Nonnull final DeviceOperatingSystemType operatingSystemType, @Nonnull final String applicationType);
}
