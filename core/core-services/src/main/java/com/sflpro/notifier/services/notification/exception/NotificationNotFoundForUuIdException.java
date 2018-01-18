package com.sflpro.notifier.services.notification.exception;

import com.sflpro.notifier.services.common.exception.EntityNotFoundForUuIdException;
import com.sflpro.notifier.services.common.model.AbstractDomainEntityModel;

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
