package com.sflpro.notifier.services.notification.email;

import com.sflpro.notifier.db.entities.notification.email.EmailNotification;
import com.sflpro.notifier.services.notification.AbstractNotificationService;
import com.sflpro.notifier.services.notification.dto.email.EmailNotificationDto;
import com.sflpro.notifier.services.notification.dto.email.EmailNotificationPropertyDto;

import javax.annotation.Nonnull;
import java.util.List;

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
    EmailNotification createEmailNotification(@Nonnull final EmailNotificationDto emailNotificationDto,
                                              @Nonnull final List<EmailNotificationPropertyDto> emailNotificationPropertyDtos);
}
