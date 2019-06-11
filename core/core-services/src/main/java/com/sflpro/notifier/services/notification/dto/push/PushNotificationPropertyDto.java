package com.sflpro.notifier.services.notification.dto.push;

import com.sflpro.notifier.db.entities.notification.push.PushNotificationProperty;
import com.sflpro.notifier.services.notification.dto.AbstractNotificationPropertyModelDto;

/**
 * User: Ruben Dilanyan
 * Company: SFL LLC
 * Date: 9/10/15
 * Time: 11:32 AM
 */
public class PushNotificationPropertyDto extends AbstractNotificationPropertyModelDto<PushNotificationProperty> {

    private static final long serialVersionUID = -4441423891899607937L;

    /* Constructors */
    public PushNotificationPropertyDto() {
    }

    public PushNotificationPropertyDto(final String propertyKey, final String propertyValue) {
        super(propertyKey, propertyValue);
    }
}
