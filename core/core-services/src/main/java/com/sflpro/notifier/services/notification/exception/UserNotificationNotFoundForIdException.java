package com.sflpro.notifier.services.notification.exception;

import com.sflpro.notifier.db.entities.notification.UserNotification;
import com.sflpro.notifier.services.common.exception.EntityNotFoundForIdException;

/**
 * User: Ruben Dilanyan
 * Company: SFL LLC
 * Date: 4/3/15
 * Time: 12:37 PM
 */
public class UserNotificationNotFoundForIdException extends EntityNotFoundForIdException {
    private static final long serialVersionUID = 4459047450797572972L;

    /* Constructors */
    public UserNotificationNotFoundForIdException(final Long id) {
        super(id, UserNotification.class);
    }
}
