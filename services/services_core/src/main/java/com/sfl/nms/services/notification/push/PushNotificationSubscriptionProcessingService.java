package com.sfl.nms.services.notification.push;

import com.sfl.nms.services.notification.dto.push.PushNotificationSubscriptionProcessingParameters;
import com.sfl.nms.services.notification.model.push.PushNotificationRecipient;

import javax.annotation.Nonnull;

/**
 * User: Ruben Dilanyan
 * Company: SFL LLC
 * Date: 8/20/15
 * Time: 11:01 AM
 */
public interface PushNotificationSubscriptionProcessingService {

    /**
     * Processes push notification subscription
     *
     * @param parameters
     * @return pushNotificationRecipient
     *
     */
    PushNotificationRecipient processPushNotificationSubscriptionChange(@Nonnull final PushNotificationSubscriptionProcessingParameters parameters);
}
