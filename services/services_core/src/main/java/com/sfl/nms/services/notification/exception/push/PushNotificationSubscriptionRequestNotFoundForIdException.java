package com.sfl.nms.services.notification.exception.push;

import com.sfl.nms.services.common.exception.EntityNotFoundForIdException;
import com.sfl.nms.services.notification.model.push.PushNotificationSubscriptionRequest;

/**
 * User: Ruben Dilanyan
 * Company: SFL LLC
 * Date: 8/21/15
 * Time: 9:15 AM
 */
public class PushNotificationSubscriptionRequestNotFoundForIdException extends EntityNotFoundForIdException {

    private static final long serialVersionUID = -8914689959749037244L;

    /* Constructors */
    public PushNotificationSubscriptionRequestNotFoundForIdException(final Long id) {
        super(id, PushNotificationSubscriptionRequest.class);
    }
}
