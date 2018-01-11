package com.sflpro.notifier.externalclients.email.mandrill.exception;

import com.sflpro.notifier.externalclients.email.common.exception.ExternalEmailNotificationClientRuntimeException;

/**
 * Company: SFL LLC
 * Created on 04/12/2017
 *
 * @author Davit Harutyunyan
 */
public class MandrillEmailClientRuntimeException extends ExternalEmailNotificationClientRuntimeException {

    private static final long serialVersionUID = 3157858197834455252L;

    /* Constructors */
    public MandrillEmailClientRuntimeException(final String message) {
        super(message);
    }

    public MandrillEmailClientRuntimeException(final String message, final Exception originalException) {
        super(message, originalException);
    }
}
