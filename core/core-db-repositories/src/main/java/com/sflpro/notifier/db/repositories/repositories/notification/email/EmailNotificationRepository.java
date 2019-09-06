package com.sflpro.notifier.db.repositories.repositories.notification.email;

import com.sflpro.notifier.db.entities.notification.email.EmailNotification;
import com.sflpro.notifier.db.repositories.repositories.notification.AbstractNotificationRepository;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * User: Ruben Dilanyan
 * Company: SFL LLC
 * Date: 3/21/15
 * Time: 7:56 PM
 */
@Repository
public interface EmailNotificationRepository extends AbstractNotificationRepository<EmailNotification> {

    @EntityGraph("Notification.ProcessingFlow")
    @Query("select n from EmailNotification n where n.id = :id")
    EmailNotification findByIdForProcessingFlow(@Param("id") final Long id);
}
