package com.sflpro.notifier.services.notification.impl.sms;

import com.sflpro.notifier.sms.SmsSenderRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.Assert;

import java.util.List;

/**
 * Created by Hayk Mkrtchyan.
 * Date: 6/18/19
 * Time: 7:32 PM
 */
@Configuration
@ComponentScan("com.sflpro.notifier.externalclients.sms.registry")
class SmsConfiguration {

    @Bean
    SmsSenderProvider smsSenderProvider(final List<SmsSenderRegistry> registries) {
        Assert.notEmpty(registries, "No any component was registered for sending sms!");
        return new DefaultSmsSenderProvider(registries);
    }
}
