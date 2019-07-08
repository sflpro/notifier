package com.sflpro.notifier.externalclients.push.registry;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * Created by Hayk Mkrtchyan.
 * Date: 7/3/19
 * Time: 5:49 PM
 */
@Configuration
@ComponentScan(basePackages = {
        "com.sflpro.notifier.externalclients.push.amazon",
        "com.sflpro.notifier.externalclients.push.firebase",
        "com.sflpro.notifier.externalclients.push.autoconfiguration"
})
class PushMessageRegistryConfiguration {
}
