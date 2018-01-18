package com.sflpro.notifier.services.notification;

import com.sflpro.notifier.db.entities.notification.UserNotification;
import com.sflpro.notifier.services.notification.dto.UserNotificationDto;

import javax.annotation.Nonnull;
import java.util.List;

/**
 * User: Ruben Dilanyan
 * Company: SFL LLC
 * Date: 4/2/15
 * Time: 6:59 PM
 */
public interface UserNotificationService {

    /**
     * Creates new user notification
     *
     * @param userId
     * @param notificationId
     * @param userNotificationDto
     * @return userNotification
     */
    @Nonnull
    UserNotification createUserNotification(@Nonnull final Long userId, @Nonnull final Long notificationId, @Nonnull final UserNotificationDto userNotificationDto);

    /**
     * Gets user notification for id
     *
     * @param userNotificationId
     * @return userNotification
     */
    @Nonnull
    UserNotification getUserNotificationById(@Nonnull final Long userNotificationId);

    /**
     * Gets user notifications for user
     *
     * @param userId
     * @return userNotifications
     */
    @Nonnull
    List<UserNotification> getUserNotificationsByUserId(@Nonnull final Long userId);
}
