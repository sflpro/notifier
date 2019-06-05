package com.sflpro.notifier.externalclients.sms.twillio.exception;

import com.sflpro.notifier.externalclients.sms.common.exception.ExternalSmsClientRuntimeException;

/**
 * User: Mher Sargsyan
 * Company: SFL LLC
 * Date: 4/9/15
 * Time: 7:42 PM
 */
public class TwillioClientRuntimeException extends ExternalSmsClientRuntimeException {

    private static final long serialVersionUID = 3157858197834455252L;

    /* Constructors */
    public TwillioClientRuntimeException(final String senderNumber, final String recipientNumber, final String messageBody, final Exception originalException) {
        super(senderNumber, recipientNumber, messageBody, originalException);
    }
}
