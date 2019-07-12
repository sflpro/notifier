package com.sflpro.notifier.services.notification.email;

import com.sflpro.notifier.db.entities.notification.email.EmailNotification;
import com.sflpro.notifier.services.notification.AbstractNotificationService;
import com.sflpro.notifier.services.notification.dto.email.EmailNotificationDto;

import javax.annotation.Nonnull;

/**
 * User: Ruben Dilanyan
 * Company: SFL LLC
 * Date: 3/21/15
 * Time: 7:53 PM
 */
public interface EmailNotificationService extends AbstractNotificationService<EmailNotification> {

    /**
     * Creates new email notification
     *
     * @param emailNotificationDto
     * @return emailNotification
     */
    @Nonnull
    EmailNotification createEmailNotification(@Nonnull final EmailNotificationDto emailNotificationDto);

    /**
     * Returns the notification to be processed
     *
     * @param notificationId
     * @return notification
     */
    EmailNotification getEmailNotificationForProcessing(final Long notificationId);
}
