package com.sflpro.notifier.services.notification.impl.email;

import com.sflpro.notifier.email.SimpleEmailSenderRegistry;
import com.sflpro.notifier.email.TemplatedEmailSenderRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * Created by Hayk Mkrtchyan.
 * Date: 6/19/19
 * Time: 12:15 PM
 */
@Configuration
@ComponentScan("com.sflpro.notifier.externalclients.email.registry")
class EmailConfiguration {

    private static final Logger LOGGER = LoggerFactory.getLogger(EmailConfiguration.class);

    @Bean
    EmailSenderProvider emailSenderProvider(final List<SimpleEmailSenderRegistry> simpleEmailSenderRegistries,
                                            final List<TemplatedEmailSenderRegistry> templatedEmailSenderRegistries) {
        if (simpleEmailSenderRegistries.isEmpty()) {
            LOGGER.info("No any component was registered for sending simple email");
        }
        if (templatedEmailSenderRegistries.isEmpty()) {
            LOGGER.info("No any component was registered for sending templated email");
        }
        return new DefaultEmailSenderProvider(simpleEmailSenderRegistries, templatedEmailSenderRegistries);
    }
}