package com.sfl.nms.persistence.repositories.notification;

import com.sfl.nms.services.notification.model.Notification;
import org.springframework.stereotype.Repository;

/**
 * User: Ruben Dilanyan
 * Company: SFL LLC
 * Date: 3/21/15
 * Time: 7:55 PM
 */
@Repository
public interface NotificationRepository extends AbstractNotificationRepository<Notification> {
}
