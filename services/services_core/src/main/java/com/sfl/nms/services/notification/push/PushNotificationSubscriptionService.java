package com.sfl.nms.services.notification.push;

import com.sfl.nms.services.notification.dto.push.PushNotificationSubscriptionDto;
import com.sfl.nms.services.notification.model.push.PushNotificationSubscription;

import javax.annotation.Nonnull;

/**
 * User: Ruben Dilanyan
 * Company: SFL LLC
 * Date: 8/13/15
 * Time: 10:18 AM
 */
public interface PushNotificationSubscriptionService {


    /**
     * Checks if push notification subscription exists for user
     *
     * @param userId
     * @return exists
     */
    @Nonnull
    boolean checkIfPushNotificationSubscriptionExistsForUser(@Nonnull final Long userId);

    /**
     * Gets push notification subscription for user
     *
     * @param userId
     * @return pushNotificationSubscription
     */
    @Nonnull
    PushNotificationSubscription getPushNotificationSubscriptionForUser(@Nonnull final Long userId);

    /**
     * Creates new push notification subscription for user
     *
     * @param userId
     * @param subscriptionDto
     * @return pushNotificationSubscription
     */
    @Nonnull
    PushNotificationSubscription createPushNotificationSubscription(@Nonnull final Long userId, @Nonnull final PushNotificationSubscriptionDto subscriptionDto);

    /**
     * Gets push notification subscription for id
     *
     * @param subscriptionId
     * @return pushNotificationSubscription
     */
    @Nonnull
    PushNotificationSubscription getPushNotificationSubscriptionById(@Nonnull final Long subscriptionId);
}
