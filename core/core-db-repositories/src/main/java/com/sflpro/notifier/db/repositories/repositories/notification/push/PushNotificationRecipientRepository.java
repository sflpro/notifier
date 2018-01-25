package com.sflpro.notifier.db.repositories.repositories.notification.push;

import com.sflpro.notifier.db.entities.notification.push.PushNotificationRecipient;
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
