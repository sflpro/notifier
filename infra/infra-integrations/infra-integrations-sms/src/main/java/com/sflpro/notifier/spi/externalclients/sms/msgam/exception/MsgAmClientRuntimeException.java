package com.sflpro.notifier.externalclients.sms.msgam.exception;

import static java.lang.String.format;

/**
 * User: Tigran Tserunyan
 * Company: SFL LLC
 * Date: 22/05/2017
 * Time: 5:01 PM
 */
public class MsgAmClientRuntimeException extends RuntimeException {

    private static final long serialVersionUID = 2318648898642455252L;

    public MsgAmClientRuntimeException(final String senderNumber, final String recipientNumber) {
        super(messageFor(senderNumber, recipientNumber));
    }

    public MsgAmClientRuntimeException(final String senderNumber, final String recipientNumber, final Exception cause) {
        super(messageFor(senderNumber, recipientNumber), cause);
    }

    private static String messageFor(final String senderNumber, final String recipientNumber) {
        return format("Exception when sending sms from number '%s' to '%s'.", senderNumber, recipientNumber);
    }
}
