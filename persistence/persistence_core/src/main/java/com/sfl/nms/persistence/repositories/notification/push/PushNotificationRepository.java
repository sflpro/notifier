package com.sfl.nms.persistence.repositories.notification.push;

import com.sfl.nms.persistence.repositories.notification.AbstractNotificationRepository;
import com.sfl.nms.services.notification.model.push.PushNotification;
import org.springframework.stereotype.Repository;

/**
 * User: Ruben Dilanyan
 * Company: SFL LLC
 * Date: 8/14/15
 * Time: 11:48 AM
 */
@Repository
public interface PushNotificationRepository extends AbstractNotificationRepository<PushNotification> {
}
