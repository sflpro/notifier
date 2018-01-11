package com.sflpro.notifier.services.notification.exception.email;

import com.sflpro.notifier.services.common.exception.ServicesRuntimeException;

/**
 * User: Ruben Dilanyan
 * Company: SFL LLC
 * Date: 11/4/14
 * Time: 4:16 PM
 */
public class SmtpTransportException extends ServicesRuntimeException {
    private static final long serialVersionUID = -8536487738405812588L;

    /* Properties */
    private final String smtpHost;

    private final String smtpUsername;

    public SmtpTransportException(final String smtpHost, final String smtpUsername, final Throwable cause) {
        super("Unable to send message over smtp for host - " + smtpHost + " with username - " + smtpUsername, cause);
        this.smtpHost = smtpHost;
        this.smtpUsername = smtpUsername;
    }

    /* Getters and setters */
    public String getSmtpHost() {
        return smtpHost;
    }

    public String getSmtpUsername() {
        return smtpUsername;
    }
}
