package com.sfl.nms.queue.producer.notification.push;

import javax.annotation.Nonnull;

/**
 * User: Ruben Dilanyan
 * Company: SFL LLC
 * Date: 8/22/15
 * Time: 8:01 PM
 */
public interface PushNotificationSubscriptionRequestQueueProducerService {


    /**
     * Process push notification subscription request
     *
     * @param requestId
     *
     */
    void processPushNotificationSubscriptionRequest(@Nonnull final Long requestId);
}
