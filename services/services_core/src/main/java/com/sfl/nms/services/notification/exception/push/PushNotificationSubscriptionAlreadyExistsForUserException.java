package com.sfl.nms.services.notification.exception.push;

import com.sfl.nms.services.common.exception.ServicesRuntimeException;

/**
 * User: Ruben Dilanyan
 * Company: SFL LLC
 * Date: 8/13/15
 * Time: 10:45 AM
 */
public class PushNotificationSubscriptionAlreadyExistsForUserException extends ServicesRuntimeException {

    private static final long serialVersionUID = 6331583001630717942L;

    /* Properties */
    private final Long userId;

    private final Long existingSubscriptionId;

    public PushNotificationSubscriptionAlreadyExistsForUserException(final Long userId, final Long existingSubscriptionId) {
        super("User with id - " + userId + " already have subscription with id - " + existingSubscriptionId);
        this.userId = userId;
        this.existingSubscriptionId = existingSubscriptionId;
    }

    /* Properties getters and setters */
    public Long getUserId() {
        return userId;
    }

    public Long getExistingSubscriptionId() {
        return existingSubscriptionId;
    }
}
