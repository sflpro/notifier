package com.sflpro.notifier.services.notification.exception.push;

import com.sflpro.notifier.services.common.exception.ServicesRuntimeException;

/**
 * User: Ruben Dilanyan
 * Company: SFL LLC
 * Date: 8/14/15
 * Time: 6:00 PM
 */
public class PushNotificationSubscriptionRequestInvalidRecipientUserException extends ServicesRuntimeException {

    private static final long serialVersionUID = -1279686839729319646L;

    /* Properties */
    private final Long requestId;

    private final Long requestUserId;

    private final Long recipientId;

    private final Long recipientUserId;

    /* Constructors */
    public PushNotificationSubscriptionRequestInvalidRecipientUserException(final Long requestId, final Long requestUserId, final Long recipientId, final Long recipientUserId) {
        super("Push notification subscription request with id - " + requestId + " belongs to user with id - " + requestUserId + " where as recipient with id - " + recipientId + " belongs to user with id - " + recipientUserId);
        this.requestId = requestId;
        this.requestUserId = requestUserId;
        this.recipientId = recipientId;
        this.recipientUserId = recipientUserId;
    }

    /* Properties getters and setters */
    public Long getRequestUserId() {
        return requestUserId;
    }

    public Long getRecipientId() {
        return recipientId;
    }

    public Long getRequestId() {
        return requestId;
    }

    public Long getRecipientUserId() {
        return recipientUserId;
    }
}
