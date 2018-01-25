package com.sflpro.notifier.services.notification.exception.push;

import com.sflpro.notifier.services.common.exception.EntityNotFoundForIdException;
import com.sflpro.notifier.db.entities.notification.push.PushNotificationRecipient;

/**
 * User: Ruben Dilanyan
 * Company: SFL LLC
 * Date: 8/13/15
 * Time: 1:43 PM
 */
public class PushNotificationRecipientNotFoundForIdException extends EntityNotFoundForIdException {

    private static final long serialVersionUID = 2957810414639971590L;

    /* Constructors */
    public PushNotificationRecipientNotFoundForIdException(final Long id, final Class<? extends PushNotificationRecipient> recipientClass) {
        super(id, recipientClass);
    }
}
