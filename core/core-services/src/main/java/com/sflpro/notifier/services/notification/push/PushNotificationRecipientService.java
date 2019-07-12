package com.sflpro.notifier.services.notification.push;

import com.sflpro.notifier.db.entities.notification.push.PushNotificationRecipient;
import com.sflpro.notifier.services.notification.dto.push.PushNotificationRecipientDto;

import javax.annotation.Nonnull;
import java.util.List;

/**
 * User: Ruben Dilanyan
 * Company: SFL LLC
 * Date: 8/13/15
 * Time: 1:11 PM
 */
public interface PushNotificationRecipientService extends AbstractPushNotificationRecipientService<PushNotificationRecipient> {

    /**
     * Gets push notification recipients for search parameters
     *
     * @param searchParameters
     * @param startFrom
     * @param maxCount
     * @return recipients
     */
    @Nonnull
    List<PushNotificationRecipient> getPushNotificationRecipientsForSearchParameters(@Nonnull final PushNotificationRecipientSearchParameters searchParameters, @Nonnull final Long startFrom, @Nonnull final Integer maxCount);

    /**
     * Gets push notifications recipients count for search parameters
     *
     * @param searchParameters
     * @return recipientsCount
     */
    @Nonnull
    Long getPushNotificationRecipientsCountForSearchParameters(@Nonnull final PushNotificationRecipientSearchParameters searchParameters);

    /**
     * Creates new push notification recipient for provided subscription and recipient DTO
     *
     * @param subscriptionId
     * @param recipientDto
     * @return pushNotificationSnsRecipient
     */
    @Nonnull
    PushNotificationRecipient createPushNotificationRecipient(@Nonnull final Long subscriptionId, @Nonnull final PushNotificationRecipientDto recipientDto);
}
