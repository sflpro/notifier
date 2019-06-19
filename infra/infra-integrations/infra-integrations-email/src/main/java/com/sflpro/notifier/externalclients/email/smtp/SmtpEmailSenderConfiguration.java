package com.sflpro.notifier.externalclients.email.smtp;

import com.sflpro.notifier.email.EmailTemplateContentResolver;
import com.sflpro.notifier.email.SimpleEmailSenderRegistry;
import com.sflpro.notifier.email.TemplatedEmailSender;
import com.sflpro.notifier.email.TemplatedEmailSenderRegistry;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by Hayk Mkrtchyan.
 * Date: 6/19/19
 * Time: 11:52 AM
 */
@Configuration
@ConditionalOnProperty(name = "smtp.host")
class SmtpEmailSenderConfiguration {

    private static final String SMTP_PROVIDER_REGISTRY_NAME = "smtp_server";

    @Bean("smtpTransportService")
    SmtpTransportService smtpTransportService() {
        return new SmtpTransportServiceImpl();
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
    @ConditionalOnBean(EmailTemplateContentResolver.class)
    TemplatedEmailSender smtpTemplatedEmailSender(final EmailTemplateContentResolver emailTemplateContentResolver) {
        return new SmtpTemplatedEmailSender(smtpTransportService(), emailTemplateContentResolver);
    }

    @Bean("smtpSimpleEmailSenderRegistry")
    @ConditionalOnBean(name = "smtpTemplatedEmailSender")
    TemplatedEmailSenderRegistry smtpTemplatedEmailSenderRegistry(final TemplatedEmailSender smtpTemplatedEmailSender) {
        return TemplatedEmailSenderRegistry.of("smtp_server", smtpTemplatedEmailSender);
    }

}
