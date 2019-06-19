package com.sflpro.notifier.externalclients.email.smtp;

import com.sflpro.notifier.email.SimpleEmailMessage;
import com.sflpro.notifier.email.SimpleEmailSender;

/**
 * Created by Hayk Mkrtchyan.
 * Date: 6/19/19
 * Time: 11:08 AM
 */
class SmtpSimpleEmailSender implements SimpleEmailSender {

    private final SmtpTransportService smtpTransportService;

    SmtpSimpleEmailSender(final SmtpTransportService smtpTransportService) {
        this.smtpTransportService = smtpTransportService;
    }

    @Override
    public void send(final SimpleEmailMessage message) {
        smtpTransportService.sendMessageOverSmtp(
                message.from(),
                message.to(),
                message.subject(),
                message.body());
    }
}
