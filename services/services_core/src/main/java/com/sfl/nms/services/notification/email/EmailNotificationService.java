package com.sfl.nms.services.notification.email;

import com.sfl.nms.services.notification.dto.email.EmailNotificationDto;
import com.sfl.nms.services.notification.AbstractNotificationService;
import com.sfl.nms.services.notification.model.email.EmailNotification;

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
}
