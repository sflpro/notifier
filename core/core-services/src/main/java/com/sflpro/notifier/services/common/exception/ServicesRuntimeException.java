package com.sflpro.notifier.services.common.exception;

/**
 * User: Ruben Dilanyan
 * Company: SFL LLC
 * Date: 11/17/14
 * Time: 12:09 PM
 */
public class ServicesRuntimeException extends RuntimeException {

    private static final long serialVersionUID = -8710986180320271380L;

    public ServicesRuntimeException(final Throwable cause) {
        super(cause);
    }

    public ServicesRuntimeException(final String message) {
        super(message);
    }

    public ServicesRuntimeException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
