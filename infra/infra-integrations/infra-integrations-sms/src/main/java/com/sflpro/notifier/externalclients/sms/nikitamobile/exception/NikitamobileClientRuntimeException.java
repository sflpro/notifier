package com.sflpro.notifier.externalclients.sms.nikitamobile.exception;

import com.sflpro.notifier.externalclients.sms.common.exception.ExternalSmsClientRuntimeException;

/**
 * Company: SFL LLC
 * Date: 4/9/15
 * Time: 7:42 PM
 */
public class NikitamobileClientRuntimeException extends ExternalSmsClientRuntimeException {

    private static final long serialVersionUID = 3157858197834455252L;

    /* Constructors */
    public NikitamobileClientRuntimeException(final String senderNumber, final String recipientNumber,final Exception originalException) {
        super(senderNumber, recipientNumber, originalException);
    }

    public NikitamobileClientRuntimeException(final String senderNumber, final String recipientNumber) {
        super(senderNumber, recipientNumber);
    }
}
