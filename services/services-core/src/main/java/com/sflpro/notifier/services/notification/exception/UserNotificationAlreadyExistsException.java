package com.sflpro.notifier.services.notification.exception;

import com.sflpro.notifier.services.common.exception.ServicesRuntimeException;

/**
 * User: Ruben Dilanyan
 * Company: SFL LLC
 * Date: 4/3/15
 * Time: 12:19 PM
 */
public class UserNotificationAlreadyExistsException extends ServicesRuntimeException {

    private static final long serialVersionUID = -6976873959871893213L;

    /* Properties */
    private final Long userNotificationId;

    private final Long notificationId;

    public UserNotificationAlreadyExistsException(final Long userNotificationId, final Long notificationId) {
        super("User notification already exists for notification with id - " + notificationId + ". User notification id - " + userNotificationId);
        this.userNotificationId = userNotificationId;
        this.notificationId = notificationId;
    }

    /* Properties getters and setters */
    public Long getUserNotificationId() {
        return userNotificationId;
    }

    public Long getNotificationId() {
        return notificationId;
    }
}
