package com.sfl.nms.queue.consumer.notification.common;

import javax.annotation.Nonnull;

/**
 * User: Mher Sargsyan
 * Company: SFL LLC
 * Date: 4/10/15
 * Time: 6:49 PM
 */
public interface NotificationQueueConsumerService {

    /**
     * Processes notification
     *
     * @param notificationId
     */
    void processNotification(@Nonnull final Long notificationId);
}
