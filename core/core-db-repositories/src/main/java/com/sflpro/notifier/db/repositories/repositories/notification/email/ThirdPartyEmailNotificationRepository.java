package com.sflpro.notifier.db.repositories.repositories.notification.email;

import com.sflpro.notifier.db.entities.notification.email.ThirdPartyEmailNotification;
import com.sflpro.notifier.db.repositories.repositories.notification.AbstractNotificationRepository;
import org.springframework.stereotype.Repository;

/**
 * User: Davit Yeghiazaryan
 * Company: SFL LLC
 * Date 6/30/16
 * Time 5:23 PM
 */
@Repository
public interface ThirdPartyEmailNotificationRepository extends AbstractNotificationRepository<ThirdPartyEmailNotification> {
}
