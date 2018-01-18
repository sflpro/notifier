package com.sflpro.notifier.persistence.repositories.notification.push;

import com.sflpro.notifier.services.notification.dto.push.PushNotificationRecipientSearchParameters;
import com.sflpro.notifier.services.notification.model.push.PushNotificationRecipient;

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
    Long getPushNotificationRecipientsCount(@Nonnull final PushNotificationRecipientSearchParameters parameters);

    /**
     * Loads push notification recipients for search parameters
     *
     * @param parameters
     * @param maxCount
     * @return recipients
     */
    @Nonnull
    List<PushNotificationRecipient> findPushNotificationRecipients(@Nonnull final PushNotificationRecipientSearchParameters parameters, final long startFrom, final int maxCount);
}
