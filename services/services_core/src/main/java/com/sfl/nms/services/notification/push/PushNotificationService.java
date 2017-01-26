package com.sfl.nms.services.notification.push;

import com.sfl.nms.services.notification.dto.push.PushNotificationDto;
import com.sfl.nms.services.notification.AbstractNotificationService;
import com.sfl.nms.services.notification.dto.push.PushNotificationPropertyDto;
import com.sfl.nms.services.notification.model.push.PushNotification;

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
    PushNotification createNotification(@Nonnull final Long pushNotificationRecipientId, @Nonnull final PushNotificationDto pushNotificationDto, @Nonnull final List<PushNotificationPropertyDto> pushNotificationPropertyDTOs);


    /**
     * Create push notifications for active user devices
     *
     * @param userId
     * @param pushNotificationDto
     * @return pushNotifications
     */
    @Nonnull
    List<PushNotification> createNotificationsForUserActiveRecipients(@Nonnull final Long userId, @Nonnull final PushNotificationDto pushNotificationDto, @Nonnull final List<PushNotificationPropertyDto> pushNotificationPropertyDTOs);
}
