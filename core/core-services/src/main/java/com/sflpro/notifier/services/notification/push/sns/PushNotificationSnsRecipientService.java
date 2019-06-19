package com.sflpro.notifier.services.notification.push.sns;

import com.sflpro.notifier.db.entities.notification.push.sns.PushNotificationSnsRecipient;
import com.sflpro.notifier.services.notification.dto.push.sns.PushNotificationSnsRecipientDto;
import com.sflpro.notifier.services.notification.push.AbstractPushNotificationRecipientService;

import javax.annotation.Nonnull;

/**
 * User: Ruben Dilanyan
 * Company: SFL LLC
 * Date: 8/13/15
 * Time: 1:11 PM
 */
public interface PushNotificationSnsRecipientService extends AbstractPushNotificationRecipientService<PushNotificationSnsRecipient> {

    /**
     * Creates new push notification SNS recipient for provided subscription and SNS recipient DTO
     *
     * @param subscriptionId
     * @param recipientDto
     * @return pushNotificationSnsRecipient
     */
    @Nonnull
    PushNotificationSnsRecipient createPushNotificationRecipient(@Nonnull final Long subscriptionId, @Nonnull final PushNotificationSnsRecipientDto recipientDto);
}
