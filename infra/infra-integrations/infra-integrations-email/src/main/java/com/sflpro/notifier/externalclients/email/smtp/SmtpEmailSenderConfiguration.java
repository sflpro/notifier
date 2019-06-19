package com.sflpro.notifier.externalclients.email.smtp;

import com.sflpro.notifier.email.SimpleEmailSenderRegistry;
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
        return SimpleEmailSenderRegistry.of("smtp_server", smtpSimpleEmailSender());
    }
}
