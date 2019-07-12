package com.sflpro.notifier.services.notification.impl.sms;

import com.sflpro.notifier.services.template.TemplatingService;
import com.sflpro.notifier.spi.sms.SimpleSmsSenderRegistry;
import com.sflpro.notifier.spi.sms.SmsTemplateContentResolver;
import com.sflpro.notifier.spi.sms.TemplatedSmsSenderRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.util.List;



/**
 * Created by Hayk Mkrtchyan.
 * Date: 6/18/19
 * Time: 7:32 PM
 */
@Configuration
@ComponentScan("com.sflpro.notifier.externalclients.sms.registry")
class SmsConfiguration {

    private static final Logger LOGGER = LoggerFactory.getLogger(SmsConfiguration.class);

    @Bean
    SmsSenderProvider smsSenderProvider(final List<SimpleSmsSenderRegistry> simpleSmsSenderRegistries, final List<TemplatedSmsSenderRegistry> templatedSmsSenderRegistries) {
        if (simpleSmsSenderRegistries.isEmpty()) {
            LOGGER.info("No any component was registered for sending simple sms messages!");
        }
        if (templatedSmsSenderRegistries.isEmpty()) {
            LOGGER.info("No any component was registered for sending templated sms messages!");
        }
        return new DefaultSmsSenderProvider(simpleSmsSenderRegistries, templatedSmsSenderRegistries);
    }

    @Bean("localSmsTemplateContentResolver")
    SmsTemplateContentResolver localSmsTemplateContentResolver(final TemplatingService templatingService) {
        return new LocalSmsTemplateContentResolver(templatingService);
    }
}
