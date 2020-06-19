package com.sflpro.notifier.externalclients.email.smtp;

import com.sflpro.notifier.spi.email.TemplatedEmailMessage;
import com.sflpro.notifier.spi.email.TemplatedEmailSender;
import com.sflpro.notifier.spi.template.TemplateContent;
import com.sflpro.notifier.spi.template.TemplateContentResolver;
import org.springframework.util.Assert;


/**
 * Created by Hayk Mkrtchyan.
 * Date: 6/19/19
 * Time: 5:07 PM
 */
class SmtpTemplatedEmailSender implements TemplatedEmailSender {

    private final SmtpTransportService smtpTransportService;
    private final TemplateContentResolver templateContentResolver;

    SmtpTemplatedEmailSender(final SmtpTransportService smtpTransportService, final TemplateContentResolver templateContentResolver) {
        this.smtpTransportService = smtpTransportService;
        this.templateContentResolver = templateContentResolver;
    }

    @Override
    public void send(final TemplatedEmailMessage message) {
        Assert.notNull(message, "Null was passed as an argument for parameter 'message'.");
        final TemplateContent content = contentFor(message);
        smtpTransportService.sendMessageOverSmtp(message.from(),
                message.to(),
                content.subject(),
                content.body(),
                message.fileAttachments());
    }

    private TemplateContent contentFor(final TemplatedEmailMessage message) {
        return message.locale()
                .map(locale -> templateContentResolver.resolve(message.templateId(), message.variables(), locale))
                .orElseGet(() -> templateContentResolver.resolve(message.templateId(), message.variables()));
    }
}
