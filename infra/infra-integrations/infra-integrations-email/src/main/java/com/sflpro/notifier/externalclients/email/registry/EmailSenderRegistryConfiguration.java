package com.sflpro.notifier.externalclients.email.registry;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * Created by Hayk Mkrtchyan.
 * Date: 6/18/19
 * Time: 7:49 PM
 */
@Configuration
@PropertySource("classpath:integrations-email.properties")
@ComponentScan(basePackages = {
        "com.sflpro.notifier.externalclients.email.mandrill",
        "com.sflpro.notifier.externalclients.email.smtp",
        "com.sflpro.notifier.externalclients.email.provider.autoconfiguration"
})
public class EmailSenderRegistryConfiguration {
}
