package com.sflpro.notifier.externalclients.email.smtp;


import com.sflpro.notifier.spi.email.SimpleEmailMessage;
import com.sflpro.notifier.spi.email.SimpleEmailSender;

/**
 * Created by Hayk Mkrtchyan.
 * Date: 6/19/19
 * Time: 11:08 AM
 */
class SmtpSimpleEmailSender implements SimpleEmailSender {

    private final com.sflpro.notifier.externalclients.email.smtp.SmtpTransportService smtpTransportService;

    SmtpSimpleEmailSender(final com.sflpro.notifier.externalclients.email.smtp.SmtpTransportService smtpTransportService) {
        this.smtpTransportService = smtpTransportService;
    }

    @Override
    public void send(final SimpleEmailMessage message) {
        smtpTransportService.sendMessageOverSmtp(
                message.from(),
                message.to(),
                message.replyTo(),
                message.subject(),
                message.body(),
                message.fileAttachments()
        );
    }
}
