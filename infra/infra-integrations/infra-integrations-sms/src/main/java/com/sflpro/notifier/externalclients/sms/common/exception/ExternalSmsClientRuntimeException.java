package com.sflpro.notifier.externalclients.sms.common.exception;

import com.sflpro.notifier.externalclients.common.http.exception.ExternalClientRuntimeException;

/**
 * User: Mher Sargsyan
 * Company: SFL LLC
 * Date: 4/9/15
 * Time: 7:42 PM
 */
public abstract class ExternalSmsClientRuntimeException extends ExternalClientRuntimeException {
    private static final long serialVersionUID = 4525851652146053826L;

    /* Properties */
    private final String senderNumber;

    private final String recipientNumber;

    private final String messageBody;

    /* Constructors */
    public ExternalSmsClientRuntimeException(final String senderNumber, final String recipientNumber, final String messageBody, final Exception originalException) {
        super("Unable to send sms message from number - " + senderNumber + ", to number - " + recipientNumber + " with message body - " + messageBody, originalException);
        this.senderNumber = senderNumber;
        this.recipientNumber = recipientNumber;
        this.messageBody = messageBody;
    }

    /* Getters and setters */
    public String getSenderNumber() {
        return senderNumber;
    }

    public String getRecipientNumber() {
        return recipientNumber;
    }

    public String getMessageBody() {
        return messageBody;
    }
}
