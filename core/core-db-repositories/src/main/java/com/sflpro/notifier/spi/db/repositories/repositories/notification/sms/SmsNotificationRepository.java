package com.sflpro.notifier.db.repositories.repositories.notification.sms;

import com.sflpro.notifier.db.entities.notification.sms.SmsNotification;
import com.sflpro.notifier.db.repositories.repositories.notification.AbstractNotificationRepository;
import org.springframework.stereotype.Repository;

/**
 * User: Ruben Dilanyan
 * Company: SFL LLC
 * Date: 3/21/15
 * Time: 7:55 PM
 */
@Repository
public interface SmsNotificationRepository extends AbstractNotificationRepository<SmsNotification> {
}
