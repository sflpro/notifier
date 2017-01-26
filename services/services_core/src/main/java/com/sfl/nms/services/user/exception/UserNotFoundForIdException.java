package com.sfl.nms.services.user.exception;

import com.sfl.nms.services.user.model.User;
import com.sfl.nms.services.common.exception.EntityNotFoundForIdException;

/**
 * User: Ruben Dilanyan
 * Company: SFL LLC
 * Date: 11/19/14
 * Time: 10:09 AM
 */
public class UserNotFoundForIdException extends EntityNotFoundForIdException {
    private static final long serialVersionUID = 947713076446052984L;

    public UserNotFoundForIdException(final Long id) {
        super(id, User.class);
    }
}
