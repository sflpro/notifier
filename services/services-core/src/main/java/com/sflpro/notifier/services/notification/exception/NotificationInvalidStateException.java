package com.sflpro.notifier.services.notification.exception;

import com.sflpro.notifier.services.common.exception.ServicesRuntimeException;
import com.sflpro.notifier.services.notification.model.NotificationState;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * User: Ruben Dilanyan
 * Company: SFL LLC
 * Date: 8/17/15
 * Time: 3:14 PM
 */
public class NotificationInvalidStateException extends ServicesRuntimeException {

    private static final long serialVersionUID = -2235560603906232087L;

    /* Properties */
    private final Long notificationId;

    private final NotificationState notificationState;

    private final Set<NotificationState> expectedStates;

    public NotificationInvalidStateException(final Long notificationId, final NotificationState notificationState, final Set<NotificationState> expectedStates) {
        super("Notification with id - " + notificationId + " has state - " + notificationState + " where as expected states are - " + expectedStates);
        this.notificationId = notificationId;
        this.notificationState = notificationState;
        this.expectedStates = Collections.unmodifiableSet(new HashSet<>(expectedStates));
    }

    /* Properties getters and setters */
    public Long getNotificationId() {
        return notificationId;
    }

    public NotificationState getNotificationState() {
        return notificationState;
    }

    public Set<NotificationState> getExpectedStates() {
        return expectedStates;
    }
}
