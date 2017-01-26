package com.sfl.nms.persistence.repositories.notification.email;

import com.sfl.nms.persistence.repositories.notification.AbstractNotificationRepository;
import com.sfl.nms.services.notification.model.email.EmailNotification;
import org.springframework.stereotype.Repository;

/**
 * User: Ruben Dilanyan
 * Company: SFL LLC
 * Date: 3/21/15
 * Time: 7:56 PM
 */
@Repository
public interface EmailNotificationRepository extends AbstractNotificationRepository<EmailNotification> {
}
