package com.sfl.nms.services.notification;

import javax.annotation.Nonnull;

/**
 * User: Ruben Dilanyan
 * Company: SFL LLC
 * Date: 8/24/15
 * Time: 1:07 PM
 */
public interface NotificationProcessingService {

    /**
     * Process notification
     *
     * @param notificationId
     */
    void processNotification(@Nonnull final Long notificationId);
}
