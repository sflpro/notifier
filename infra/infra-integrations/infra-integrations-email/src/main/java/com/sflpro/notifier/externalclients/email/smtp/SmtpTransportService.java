package com.sflpro.notifier.externalclients.email.smtp;

import com.sflpro.notifier.spi.email.SpiEmailNotificationFileAttachment;

import java.util.List;

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
     * @param from
     * @param to
     * @param subject
     * @param body
     * @param fileAttachments
     */
    void sendMessageOverSmtp(final String from,
                             final String to,
                             final String subject,
                             final String body,
                             final List<SpiEmailNotificationFileAttachment> fileAttachments);
}
