package com.sflpro.notifier.externalclients.sms.webhook.exception;

import com.sflpro.notifier.externalclients.sms.common.exception.ExternalSmsClientRuntimeException;

/**
 * User: Tigran Tserunyan
 * Company: SFL LLC
 * Date: 22/05/2017
 * Time: 5:01 PM
 */
public class WebhookSenderClientRuntimeException extends ExternalSmsClientRuntimeException {

    public WebhookSenderClientRuntimeException(final String senderNumber, final String recipientNumber) {
        super(senderNumber, recipientNumber);
    }

    public WebhookSenderClientRuntimeException(final String senderNumber, final String recipientNumber, final Exception cause) {
        super(senderNumber, recipientNumber, cause);
    }
}
