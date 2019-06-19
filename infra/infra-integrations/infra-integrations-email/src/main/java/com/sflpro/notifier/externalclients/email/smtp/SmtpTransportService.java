package com.sflpro.notifier.externalclients.email.smtp;

import com.sflpro.notifier.email.SimpleEmailMessage;

import javax.annotation.Nonnull;

/**
 * User: Ruben Dilanyan
 * Company: SFL LLC
 * Date: 1/11/16
 * Time: 10:55 AM
 */

interface SmtpTransportService {


    /**
     * Perform email over SMTP
     *
     * @param message
     */
    void sendMessageOverSmtp(@Nonnull final SimpleEmailMessage message);
}
