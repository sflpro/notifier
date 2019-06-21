package com.sflpro.notifier.db.repositories.repositories.notification.push;

import com.sflpro.notifier.db.entities.notification.push.PushNotificationRecipient;

import javax.annotation.Nonnull;
import java.util.List;

/**
 * User: Ruben Dilanyan
 * Company: SFL LLC
 * Date: 8/13/15
 * Time: 1:09 PM
 */
public interface PushNotificationRecipientRepositoryCustom {

    /**
     * Gets count of push notification recipients for search parameters
     *
     * @param parameters
     * @return recipientsCount
     */
    @Nonnull
    Long getPushNotificationRecipientsCount(@Nonnull final PushNotificationRecipientSearchFilter parameters);

    /**
     * Loads push notification recipients for search parameters
     *
     * @param parameters
     * @param maxCount
     * @return recipients
     */
    @Nonnull
    List<PushNotificationRecipient> findPushNotificationRecipients(@Nonnull final PushNotificationRecipientSearchFilter parameters, final long startFrom, final int maxCount);
}
