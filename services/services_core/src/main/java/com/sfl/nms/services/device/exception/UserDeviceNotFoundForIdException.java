package com.sfl.nms.services.device.exception;

import com.sfl.nms.services.common.exception.EntityNotFoundForIdException;
import com.sfl.nms.services.device.model.UserDevice;

/**
 * User: Ruben Dilanyan
 * Company: SFL LLC
 * Date: 1/10/16
 * Time: 1:56 PM
 */
public class UserDeviceNotFoundForIdException extends EntityNotFoundForIdException {

    /* Constructors */
    public UserDeviceNotFoundForIdException(final Long id) {
        super(id, UserDevice.class);
    }
}
