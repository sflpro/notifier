package com.sflpro.notifier.externalclients.sms.common.exception;

import com.sflpro.notifier.externalclients.common.http.exception.ExternalClientRuntimeException;

import static java.lang.String.format;

/**
 * User: Mher Sargsyan
 * Company: SFL LLC
 * Date: 4/9/15
 * Time: 7:42 PM
 */
public class ExternalSmsClientRuntimeException extends ExternalClientRuntimeException {
    private static final long serialVersionUID = 4525851652146053826L;

    /* Properties */
    private final String senderNumber;
    private final String recipientNumber;

    /* Constructors */
    public ExternalSmsClientRuntimeException(final String senderNumber, final String recipientNumber, final Exception originalException) {
        super(messageFor(senderNumber, recipientNumber), originalException);
        this.senderNumber = senderNumber;
        this.recipientNumber = recipientNumber;
    }

    public ExternalSmsClientRuntimeException(final String senderNumber, final String recipientNumber) {
        super(messageFor(senderNumber, recipientNumber));
        this.senderNumber = senderNumber;
        this.recipientNumber = recipientNumber;
    }

    private static String messageFor(final String senderNumber, final String recipientNumber) {
        return format("Unable to send sms message from number - %s, to number - %s", senderNumber, recipientNumber);
    }

    public String getSenderNumber() {
        return senderNumber;
    }

    public String getRecipientNumber() {
        return recipientNumber;
    }
}
