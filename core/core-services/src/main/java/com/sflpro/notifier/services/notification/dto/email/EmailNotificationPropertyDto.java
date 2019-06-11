package com.sflpro.notifier.services.notification.dto.email;

import com.sflpro.notifier.db.entities.notification.email.EmailNotificationProperty;
import com.sflpro.notifier.services.notification.dto.AbstractNotificationPropertyModelDto;

/**
 * User: Davit Yeghiazaryan
 * Company: SFL LLC
 * Date 6/30/16
 * Time 6:17 PM
 */
public class EmailNotificationPropertyDto extends AbstractNotificationPropertyModelDto<EmailNotificationProperty> {
    private static final long serialVersionUID = 2424322882785024812L;

    /* Constructors */
    public EmailNotificationPropertyDto() {
    }

    public EmailNotificationPropertyDto(final String propertyKey, final String propertyValue) {
        super(propertyKey, propertyValue);
    }
}
