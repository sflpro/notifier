package com.sfl.nms.services.device.exception;

import com.sfl.nms.services.common.exception.ServicesRuntimeException;

/**
 * User: Ruben Dilanyan
 * Company: SFL LLC
 * Date: 1/10/16
 * Time: 2:35 PM
 */
public class UserDeviceAlreadyExistsException extends ServicesRuntimeException {

    private static final long serialVersionUID = -5510366675754689345L;

    /* Properties */
    private final Long userId;

    private final String userDeviceUuId;

    private final Long existingDeviceId;

    /* Constructors */
    public UserDeviceAlreadyExistsException(final Long userId, final String userDeviceUuId, final Long existingDeviceId) {
        super("User device with id - " + existingDeviceId + " already exists for uuid - " + userDeviceUuId + " and user - " + userId);
        this.userId = userId;
        this.userDeviceUuId = userDeviceUuId;
        this.existingDeviceId = existingDeviceId;
    }

    /* Properties getters and setters */
    public Long getUserId() {
        return userId;
    }

    public String getUserDeviceUuId() {
        return userDeviceUuId;
    }

    public Long getExistingDeviceId() {
        return existingDeviceId;
    }
}
