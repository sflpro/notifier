package com.sfl.nms.services.notification.exception;

import com.sfl.nms.services.common.exception.ServicesRuntimeException;
import com.sfl.nms.services.notification.model.NotificationType;

/**
 * User: Ruben Dilanyan
 * Company: SFL LLC
 * Date: 8/24/15
 * Time: 1:20 PM
 */
public class UnsupportedNotificationTypeException extends ServicesRuntimeException {

    private static final long serialVersionUID = 6841511059561290537L;

    /* Properties */
    private final NotificationType type;

    /* Constructors */
    public UnsupportedNotificationTypeException(final NotificationType type) {
        super("Unsupported notification type - " + type);
        this.type = type;
    }

    /* Properties getters and setters */
    public NotificationType getType() {
        return type;
    }
}
