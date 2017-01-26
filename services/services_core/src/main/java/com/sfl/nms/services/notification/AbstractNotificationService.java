package com.sfl.nms.services.notification;

import com.sfl.nms.services.notification.model.Notification;
import com.sfl.nms.services.notification.model.NotificationState;

import javax.annotation.Nonnull;

/**
 * User: Ruben Dilanyan
 * Company: SFL LLC
 * Date: 3/21/15
 * Time: 7:52 PM
 */
public interface AbstractNotificationService<T extends Notification> {


    /**
     * Returns notification by id
     *
     * @param notificationId
     * @return notification
     */
    @Nonnull
    T getNotificationById(@Nonnull final Long notificationId);

    /**
     * Update notification state
     *
     * @param notificationId
     * @param notificationState
     * @return notification
     */
    @Nonnull
    T updateNotificationState(@Nonnull final Long notificationId, @Nonnull NotificationState notificationState);

    /**
     * Update provider external uuid
     *
     * @param notificationId
     * @param providerExternalUuid
     * @return notification
     */
    @Nonnull
    T updateProviderExternalUuid(@Nonnull final Long notificationId, @Nonnull final String providerExternalUuid);
}
