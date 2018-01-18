package com.sflpro.notifier.persistence.repositories.notification.email;

import com.sflpro.notifier.persistence.repositories.notification.AbstractNotificationRepository;
import com.sflpro.notifier.services.notification.model.email.ThirdPartyEmailNotification;
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
