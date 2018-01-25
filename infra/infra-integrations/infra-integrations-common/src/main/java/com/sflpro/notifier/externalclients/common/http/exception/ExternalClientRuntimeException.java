package com.sflpro.notifier.externalclients.common.http.exception;

/**
 * User: Ruben Dilanyan
 * Company: SFL LLC
 * Date: 3/15/15
 * Time: 12:12 PM
 */
public class ExternalClientRuntimeException extends RuntimeException {
    private static final long serialVersionUID = -4285381500428801260L;

    /* Constructors */
    public ExternalClientRuntimeException(final Throwable throwable) {
        super(throwable);
    }

    public ExternalClientRuntimeException(final String message) {
        super(message);
    }

    public ExternalClientRuntimeException(final String message, final Throwable throwable) {
        super(message, throwable);
    }
}
