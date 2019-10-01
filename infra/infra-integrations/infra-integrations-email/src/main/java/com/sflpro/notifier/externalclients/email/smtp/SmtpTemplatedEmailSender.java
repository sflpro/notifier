package com.sflpro.notifier.externalclients.email.smtp;

import com.sflpro.notifier.spi.email.EmailTemplateContent;
import com.sflpro.notifier.spi.email.EmailTemplateContentResolver;
import com.sflpro.notifier.spi.email.TemplatedEmailMessage;
import com.sflpro.notifier.spi.email.TemplatedEmailSender;
import org.springframework.util.Assert;


/**
 * Created by Hayk Mkrtchyan.
 * Date: 6/19/19
 * Time: 5:07 PM
 */
class SmtpTemplatedEmailSender implements TemplatedEmailSender {

    private final SmtpTransportService smtpTransportService;
    private final EmailTemplateContentResolver emailTemplateContentResolver;

    SmtpTemplatedEmailSender(final SmtpTransportService smtpTransportService, final EmailTemplateContentResolver emailTemplateContentResolver) {
        this.smtpTransportService = smtpTransportService;
        this.emailTemplateContentResolver = emailTemplateContentResolver;
    }

    @Override
    public void send(final TemplatedEmailMessage message) {
        Assert.notNull(message, "Null was passed as an argument for parameter 'message'.");
        final EmailTemplateContent content = contentFor(message);
        smtpTransportService.sendMessageOverSmtp(message.from(),
                message.to(),
                content.subject(),
                content.body());
    }

    private EmailTemplateContent contentFor(final TemplatedEmailMessage message) {
        return message.locale()
                .map(locale -> emailTemplateContentResolver.resolve(message.templateId(), message.variables(), locale))
                .orElseGet(() -> emailTemplateContentResolver.resolve(message.templateId(), message.variables()));
    }
}
