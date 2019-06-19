package com.sflpro.notifier.services.notification.exception.push;

import com.sflpro.notifier.db.entities.notification.push.PushNotificationSubscriptionRequestState;
import com.sflpro.notifier.services.common.exception.ServicesRuntimeException;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * User: Ruben Dilanyan
 * Company: SFL LLC
 * Date: 8/21/15
 * Time: 5:33 PM
 */
public class PushNotificationSubscriptionRequestInvalidStateException extends ServicesRuntimeException {

    private static final long serialVersionUID = 174880027479826448L;

    /* Properties */
    private final Long requestId;

    private final PushNotificationSubscriptionRequestState currentState;

    private final Set<PushNotificationSubscriptionRequestState> requiredStates;

    /* Constructors */
    public PushNotificationSubscriptionRequestInvalidStateException(final Long requestId, final PushNotificationSubscriptionRequestState currentState, final Set<PushNotificationSubscriptionRequestState> requiredStates) {
        super("Push notification with id - " + requestId + " has state - " + currentState + " where as required state is - " + requiredStates);
        this.requestId = requestId;
        this.currentState = currentState;
        this.requiredStates = Collections.unmodifiableSet(new HashSet<>(requiredStates));
    }

    /* Properties getters and setters */
    public Long getRequestId() {
        return requestId;
    }

    public PushNotificationSubscriptionRequestState getCurrentState() {
        return currentState;
    }

    public Set<PushNotificationSubscriptionRequestState> getRequiredStates() {
        return requiredStates;
    }
}
