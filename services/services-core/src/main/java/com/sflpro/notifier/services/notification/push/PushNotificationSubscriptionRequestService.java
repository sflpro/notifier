package com.sflpro.notifier.services.notification.push;

import com.sflpro.notifier.services.notification.dto.push.PushNotificationSubscriptionRequestDto;
import com.sflpro.notifier.services.notification.model.push.PushNotificationSubscriptionRequest;
import com.sflpro.notifier.services.notification.model.push.PushNotificationSubscriptionRequestState;

import javax.annotation.Nonnull;

/**
 * User: Ruben Dilanyan
 * Company: SFL LLC
 * Date: 8/21/15
 * Time: 9:18 AM
 */
public interface PushNotificationSubscriptionRequestService {

    /**
     * Creates new push notification subscription request
     *
     * @param userId
     * @param userMobileDeviceId
     * @param requestDto
     * @return pushNotificationSubscriptionRequest
     */
    @Nonnull
    PushNotificationSubscriptionRequest createPushNotificationSubscriptionRequest(@Nonnull final Long userId, @Nonnull final Long userMobileDeviceId, @Nonnull final PushNotificationSubscriptionRequestDto requestDto);

    /**
     * Gets push notification subscription request for id
     *
     * @param requestId
     * @return pushNotificationSubscriptionRequest
     */
    @Nonnull
    PushNotificationSubscriptionRequest getPushNotificationSubscriptionRequestById(@Nonnull final Long requestId);


    /**
     * Updates push notification subscription request state
     *
     * @param requestId
     * @param state
     * @return pushNotificationSubscriptionRequest
     */
    @Nonnull
    PushNotificationSubscriptionRequest updatePushNotificationSubscriptionRequestState(@Nonnull final Long requestId, @Nonnull final PushNotificationSubscriptionRequestState state);

    /**
     * Gets push notification subscription request by uuid
     *
     * @param uuId
     * @return pushNotificationSubscriptionRequest
     */
    @Nonnull
    PushNotificationSubscriptionRequest getPushNotificationSubscriptionRequestByUuId(@Nonnull final String uuId);

    /**
     * Checks if push notification subscription recipient exists for uuId
     *
     * @param uuId
     * @return exists
     */
    @Nonnull
    boolean checkIfPushNotificationSubscriptionRecipientExistsForUuId(@Nonnull final String uuId);

    /**
     * Updates recipient for push notification subscription request
     *
     * @param requestId
     * @param recipientId
     * @return pushNotificationSubscriptionRequest
     */
    @Nonnull
    PushNotificationSubscriptionRequest updatePushNotificationSubscriptionRequestRecipient(@Nonnull final Long requestId, @Nonnull final Long recipientId);
}
