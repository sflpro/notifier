package com.sfl.nms.persistence.repositories.notification.push.sns;

import com.sfl.nms.persistence.repositories.notification.push.AbstractPushNotificationRecipientRepository;
import com.sfl.nms.services.notification.model.push.sns.PushNotificationSnsRecipient;
import org.springframework.stereotype.Repository;

/**
 * User: Ruben Dilanyan
 * Company: SFL LLC
 * Date: 8/13/15
 * Time: 1:09 PM
 */
@Repository
public interface PushNotificationSnsRecipientRepository extends AbstractPushNotificationRecipientRepository<PushNotificationSnsRecipient> {
}
