package com.sflpro.notifier.externalclients.push.common.exception;

/**
 * User: Ruben Dilanyan
 * Company: SFL LLC
 * Date: 4/9/15
 * Time: 7:42 PM
 */
public abstract class ExternalPushNotificationClientRuntimeException extends RuntimeException {

    private static final long serialVersionUID = 4525851652146053826L;

    /* Constructors */
    public ExternalPushNotificationClientRuntimeException(final String exceptionMessage, final Exception originalException) {
        super(exceptionMessage, originalException);
    }

    public ExternalPushNotificationClientRuntimeException(final String exceptionMessage) {
        super(exceptionMessage);
    }
}
