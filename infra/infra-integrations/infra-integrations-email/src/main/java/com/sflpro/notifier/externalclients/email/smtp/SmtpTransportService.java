package com.sflpro.notifier.externalclients.email.smtp;

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
    void sendMessageOverSmtp(final String from, final String to, final String subject, final String body);
}
