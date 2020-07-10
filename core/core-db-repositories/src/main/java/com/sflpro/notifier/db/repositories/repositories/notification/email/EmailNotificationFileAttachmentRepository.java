package com.sflpro.notifier.db.repositories.repositories.notification.email;

import com.sflpro.notifier.db.entities.notification.email.EmailNotificationFileAttachment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * User: Arthur Hakobyan
 * Company: SFL LLC
 * Date: 05/29/2020
 */

@Repository
public interface EmailNotificationFileAttachmentRepository extends JpaRepository<EmailNotificationFileAttachment, Long> {

}
