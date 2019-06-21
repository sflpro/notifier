package com.sflpro.notifier.services.notification.push;

import com.sflpro.notifier.db.entities.notification.push.PushNotification;
import com.sflpro.notifier.services.notification.AbstractNotificationService;
import com.sflpro.notifier.services.notification.dto.NotificationPropertyDto;
import com.sflpro.notifier.services.notification.dto.push.PushNotificationDto;

import javax.annotation.Nonnull;
import java.util.List;

/**
 * User: Ruben Dilanyan
 * Company: SFL LLC
 * Date: 8/14/15
 * Time: 11:42 AM
 */
public interface PushNotificationService extends AbstractNotificationService<PushNotification> {

    /**
     * Creates new push notification for provided recipient and DTO
     *
     * @param pushNotificationRecipientId
     * @param pushNotificationDto
     * @param pushNotificationPropertyDTOs
     * @return pushNotification
     */
    @Nonnull
    PushNotification createNotification(@Nonnull final Long pushNotificationRecipientId, @Nonnull final PushNotificationDto pushNotificationDto, @Nonnull final List<NotificationPropertyDto> pushNotificationPropertyDTOs);


    /**
     * Create push notifications for active user devices
     *
     * @param userId
     * @param pushNotificationDto
     * @return pushNotifications
     */
    @Nonnull
    List<PushNotification> createNotificationsForUserActiveRecipients(@Nonnull final Long userId, @Nonnull final PushNotificationDto pushNotificationDto, @Nonnull final List<NotificationPropertyDto> pushNotificationPropertyDTOs);
}
