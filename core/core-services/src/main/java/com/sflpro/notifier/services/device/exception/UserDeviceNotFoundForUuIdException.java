package com.sflpro.notifier.services.device.exception;

import com.sflpro.notifier.services.common.exception.ServicesRuntimeException;

/**
 * User: Ruben Dilanyan
 * Company: SFL LLC
 * Date: 1/10/16
 * Time: 3:06 PM
 */
public class UserDeviceNotFoundForUuIdException extends ServicesRuntimeException {

    private static final long serialVersionUID = 550146903255509982L;

    /* Properties */
    private final Long userId;

    private final String uuId;

    /* Constructors */
    public UserDeviceNotFoundForUuIdException(final Long userId, final String uuId) {
        super("No user device was found for user with id - " + userId + " and device uuid - " + uuId);
        this.userId = userId;
        this.uuId = uuId;
    }

    /* Properties getters and setters */
    public Long getUserId() {
        return userId;
    }

    public String getUuId() {
        return uuId;
    }
}
