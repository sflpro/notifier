package com.sflpro.notifier.externalclients.email.smtp;

import com.sflpro.notifier.spi.template.TemplateContentResolver;
import com.sflpro.notifier.spi.email.SimpleEmailSenderRegistry;
import com.sflpro.notifier.spi.email.TemplatedEmailSender;
import com.sflpro.notifier.spi.email.TemplatedEmailSenderRegistry;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by Hayk Mkrtchyan.
 * Date: 6/19/19
 * Time: 11:52 AM
 */
@Configuration
@ConditionalOnProperty(name = "smtp.enabled")
class SmtpEmailSenderConfiguration {

    private static final String SMTP_PROVIDER_REGISTRY_NAME = "smtp_server";

    @Bean("smtpTransportService")
    com.sflpro.notifier.externalclients.email.smtp.SmtpTransportService smtpTransportService() {
        return new com.sflpro.notifier.externalclients.email.smtp.SmtpTransportServiceImpl();
    }

    @Bean("smtpSimpleEmailSender")
    SmtpSimpleEmailSender smtpSimpleEmailSender() {
        return new SmtpSimpleEmailSender(smtpTransportService());
    }

    @Bean("smtpSimpleEmailSenderRegistry")
    SimpleEmailSenderRegistry smtpSimpleEmailSenderRegistry() {
        return SimpleEmailSenderRegistry.of(SMTP_PROVIDER_REGISTRY_NAME, smtpSimpleEmailSender());
    }

    @Bean("smtpTemplatedEmailSender")
    TemplatedEmailSender smtpTemplatedEmailSender(final TemplateContentResolver templateContentResolver) {
        return new SmtpTemplatedEmailSender(smtpTransportService(), templateContentResolver);
    }

    @Bean("smtpTemplatedEmailSenderRegistry")
    TemplatedEmailSenderRegistry smtpTemplatedEmailSenderRegistry(final TemplatedEmailSender smtpTemplatedEmailSender) {
        return TemplatedEmailSenderRegistry.of(SMTP_PROVIDER_REGISTRY_NAME, smtpTemplatedEmailSender);
    }

}
