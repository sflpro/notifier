package com.sflpro.notifier.services.user.exception;

import com.sflpro.notifier.services.common.exception.ServicesRuntimeException;

/**
 * User: Ruben Dilanyan
 * Company: SFL LLC
 * Date: 1/9/16
 * Time: 7:19 PM
 */
public class UserAlreadyExistsForUuIdException extends ServicesRuntimeException {

    /* Properties */
    private final String uuId;

    private final Long userId;

    /* Constructors */
    public UserAlreadyExistsForUuIdException(final String uuId, final Long existingUserId) {
        super("User with id - " + existingUserId + " already has uuid - " + uuId);
        this.uuId = uuId;
        this.userId = existingUserId;
    }

    /* Properties getters and setters */
    public String getUuId() {
        return uuId;
    }

    public Long getExistingUserId() {
        return userId;
    }
}
