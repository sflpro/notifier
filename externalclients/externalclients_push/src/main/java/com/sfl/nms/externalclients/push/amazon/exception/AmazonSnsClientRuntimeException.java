package com.sfl.nms.externalclients.push.amazon.exception;

import com.sfl.nms.externalclients.push.common.exception.ExternalPushNotificationClientRuntimeException;

/**
 * User: Ruben Dilanyan
 * Company: SFL LLC
 * Date: 4/9/15
 * Time: 7:42 PM
 */
public class AmazonSnsClientRuntimeException extends ExternalPushNotificationClientRuntimeException {

    private static final long serialVersionUID = 3157858197834455252L;

    /* Constructors */
    public AmazonSnsClientRuntimeException(final String message) {
        super(message);
    }

    public AmazonSnsClientRuntimeException(final String message, final Exception originalException) {
        super(message, originalException);
    }
}
