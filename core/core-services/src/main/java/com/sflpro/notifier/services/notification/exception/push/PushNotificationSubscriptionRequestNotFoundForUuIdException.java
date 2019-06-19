package com.sflpro.notifier.services.notification.exception.push;

import com.sflpro.notifier.db.entities.notification.push.PushNotificationSubscriptionRequest;
import com.sflpro.notifier.services.common.exception.EntityNotFoundForUuIdException;

/**
 * User: Ruben Dilanyan
 * Company: SFL LLC
 * Date: 8/21/15
 * Time: 9:15 AM
 */
public class PushNotificationSubscriptionRequestNotFoundForUuIdException extends EntityNotFoundForUuIdException {

    private static final long serialVersionUID = -8914689959749037244L;

    /* Constructors */
    public PushNotificationSubscriptionRequestNotFoundForUuIdException(final String uuId) {
        super(uuId, PushNotificationSubscriptionRequest.class);
    }
}
