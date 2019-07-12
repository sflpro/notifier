package com.sflpro.notifier.db.repositories.repositories.notification.push;

import com.sflpro.notifier.db.entities.notification.push.PushNotification;
import com.sflpro.notifier.db.repositories.repositories.notification.AbstractNotificationRepository;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * User: Ruben Dilanyan
 * Company: SFL LLC
 * Date: 8/14/15
 * Time: 11:48 AM
 */
@Repository
public interface PushNotificationRepository extends AbstractNotificationRepository<PushNotification> {

    @EntityGraph("PushNotification.ProcessingFlow")
    @Query("select p from PushNotification p where p.id = :id")
    PushNotification findByIdForProcessingFlow(@Param("id") final Long id);
}
