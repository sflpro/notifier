package com.sflpro.notifier.externalclients.sms.registry;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * Created by Hayk Mkrtchyan.
 * Date: 6/18/19
 * Time: 7:49 PM
 */
@Configuration
@PropertySource("classpath:integrations-sms.properties")
@ComponentScan(basePackages = {
        "com.sflpro.notifier.externalclients.sms.twillio",
        "com.sflpro.notifier.externalclients.sms.provider.autoconfiguration"
})
class SmsRegistryConfiguration {
}
