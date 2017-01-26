package com.sfl.nms.services.notification.push;

import com.sfl.nms.services.notification.model.push.PushNotificationRecipient;

import javax.annotation.Nonnull;

/**
 * User: Ruben Dilanyan
 * Company: SFL LLC
 * Date: 8/21/15
 * Time: 11:08 AM
 */
public interface PushNotificationSubscriptionRequestProcessingService {

    /**
     * Process push notification subscription request
     *
     * @param requestId
     * @return pushNotificationRecipient
     */
    @Nonnull
    PushNotificationRecipient processPushNotificationSubscriptionRequest(@Nonnull final Long requestId);
}
