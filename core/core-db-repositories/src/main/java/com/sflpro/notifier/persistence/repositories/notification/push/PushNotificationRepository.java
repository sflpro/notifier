package com.sflpro.notifier.persistence.repositories.notification.push;

import com.sflpro.notifier.persistence.repositories.notification.AbstractNotificationRepository;
import com.sflpro.notifier.db.entities.notification.push.PushNotification;
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
