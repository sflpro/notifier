package com.sflpro.notifier.services.user.exception;

import com.sflpro.notifier.services.common.exception.EntityNotFoundForIdException;
import com.sflpro.notifier.services.user.model.User;

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
