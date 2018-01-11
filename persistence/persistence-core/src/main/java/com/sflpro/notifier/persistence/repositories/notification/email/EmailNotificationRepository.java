package com.sflpro.notifier.persistence.repositories.notification.email;

import com.sflpro.notifier.persistence.repositories.notification.AbstractNotificationRepository;
import com.sflpro.notifier.services.notification.model.email.EmailNotification;
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
