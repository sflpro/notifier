package com.sflpro.notifier.persistence.repositories.notification.push;

import com.sflpro.notifier.services.notification.model.push.PushNotificationRecipient;
import org.springframework.stereotype.Repository;

/**
 * User: Ruben Dilanyan
 * Company: SFL LLC
 * Date: 8/13/15
 * Time: 1:09 PM
 */
@Repository
public interface PushNotificationRecipientRepository extends AbstractPushNotificationRecipientRepository<PushNotificationRecipient>, PushNotificationRecipientRepositoryCustom {
}
