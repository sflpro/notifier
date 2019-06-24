package com.sflpro.notifier.externalclients.sms.msgam.exception;

import com.sflpro.notifier.externalclients.sms.common.exception.ExternalSmsClientRuntimeException;

/**
 * User: Tigran Tserunyan
 * Company: SFL LLC
 * Date: 22/05/2017
 * Time: 5:01 PM
 */
public class MsgAmClientRuntimeException extends ExternalSmsClientRuntimeException {

    private static final long serialVersionUID = 2318648898642455252L;

    public MsgAmClientRuntimeException(final String senderNumber, final String recipientNumber) {
        super(senderNumber, recipientNumber);
    }

    public MsgAmClientRuntimeException(final String senderNumber, final String recipientNumber, final Exception cause) {
        super(senderNumber, recipientNumber, cause);
    }
}
