package com.sfl.nms.queue.producer.notification.common;

import javax.annotation.Nonnull;

/**
 * User: Mher Sargsyan
 * Company: SFL LLC
 * Date: 4/10/15
 * Time: 7:49 PM
 */
public interface NotificationQueueProducerService {

    /**
     * Processes event to start sending notification
     *
     * @param notificationId
     */
    void processStartSendingNotificationEvent(@Nonnull final Long notificationId);
}
