package com.sfl.nms.services.notification.exception;

import com.sfl.nms.services.common.exception.EntityNotFoundForUuIdException;
import com.sfl.nms.services.common.model.AbstractDomainEntityModel;

/**
 * User: Ruben Dilanyan
 * Company: SFL LLC
 * Date: 3/21/15
 * Time: 7:45 PM
 */
public class NotificationNotFoundForUuIdException extends EntityNotFoundForUuIdException {
    private static final long serialVersionUID = -2029389887734858316L;

    /* Constructors */
    public NotificationNotFoundForUuIdException(final String uuId, final Class<? extends AbstractDomainEntityModel> notificationClass) {
        super(uuId, notificationClass);
    }
}
