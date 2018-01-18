package com.sflpro.notifier.services.notification.push;

import com.sflpro.notifier.services.notification.model.push.PushNotificationRecipient;
import com.sflpro.notifier.services.notification.model.push.PushNotificationRecipientStatus;

import javax.annotation.Nonnull;

/**
 * User: Ruben Dilanyan
 * Company: SFL LLC
 * Date: 8/13/15
 * Time: 1:10 PM
 */
public interface AbstractPushNotificationRecipientService<T extends PushNotificationRecipient> {

    /**
     * Gets push notification recipient by id
     *
     * @param recipientId
     * @return pushNotificationRecipient
     */
    @Nonnull
    T getPushNotificationRecipientById(@Nonnull final Long recipientId);

    /**
     * Associates user user device with push notification recipient
     *
     * @param recipientId
     * @param userDeviceId
     * @return pushNotificationRecipient
     */
    @Nonnull
    T updatePushNotificationRecipientUserDevice(@Nonnull final Long recipientId, @Nonnull final Long userDeviceId);

    /**
     * Updates push notification recipient status
     *
     * @param recipientId
     * @param status
     * @return pushNotificationRecipient
     */
    @Nonnull
    T updatePushNotificationRecipientStatus(@Nonnull final Long recipientId, @Nonnull final PushNotificationRecipientStatus status);
}
