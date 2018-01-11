package com.sflpro.notifier.services.notification.exception.push;

import com.sflpro.notifier.services.common.exception.ServicesRuntimeException;

/**
 * User: Ruben Dilanyan
 * Company: SFL LLC
 * Date: 8/13/15
 * Time: 10:45 AM
 */
public class PushNotificationSubscriptionNotFoundForUserException extends ServicesRuntimeException {

    private static final long serialVersionUID = 6331583001630717942L;

    /* Properties */
    private final Long userId;

    public PushNotificationSubscriptionNotFoundForUserException(final Long userId) {
        super("No push notification subscription is found for user with id - " + userId);
        this.userId = userId;
    }

    /* Properties getters and setters */
    public Long getUserId() {
        return userId;
    }
}
