package com.sfl.nms.persistence.repositories.notification.sms;

import com.sfl.nms.persistence.repositories.notification.AbstractNotificationRepository;
import com.sfl.nms.services.notification.model.sms.SmsNotification;
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
