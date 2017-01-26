package com.sfl.nms.services.notification.exception;

import com.sfl.nms.services.common.exception.EntityNotFoundForIdException;
import com.sfl.nms.services.notification.model.UserNotification;

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
