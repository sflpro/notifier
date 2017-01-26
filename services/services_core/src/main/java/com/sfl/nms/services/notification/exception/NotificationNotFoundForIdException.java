package com.sfl.nms.services.notification.exception;

import com.sfl.nms.services.common.exception.EntityNotFoundForIdException;
import com.sfl.nms.services.common.model.AbstractDomainEntityModel;

/**
 * User: Ruben Dilanyan
 * Company: SFL LLC
 * Date: 3/21/15
 * Time: 7:45 PM
 */
public class NotificationNotFoundForIdException extends EntityNotFoundForIdException {
    private static final long serialVersionUID = -2029389887734858316L;

    /* Constructors */
    public NotificationNotFoundForIdException(final Long id, final Class<? extends AbstractDomainEntityModel> notificationClass) {
        super(id, notificationClass);
    }
}
