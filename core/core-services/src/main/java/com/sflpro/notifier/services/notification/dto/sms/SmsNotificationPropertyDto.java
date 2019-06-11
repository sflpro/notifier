package com.sflpro.notifier.services.notification.dto.sms;

import com.sflpro.notifier.db.entities.notification.sms.SmsNotificationProperty;
import com.sflpro.notifier.services.notification.dto.AbstractNotificationPropertyModelDto;

/**
 * User: Davit Yeghiazaryan
 * Company: SFL LLC
 * Date 6/30/16
 * Time 6:17 PM
 */
public class SmsNotificationPropertyDto extends AbstractNotificationPropertyModelDto<SmsNotificationProperty> {
    private static final long serialVersionUID = 2424322882785024812L;

    /* Constructors */
    public SmsNotificationPropertyDto() {
    }

    public SmsNotificationPropertyDto(final String propertyKey, final String propertyValue) {
        super(propertyKey, propertyValue);
    }
}
