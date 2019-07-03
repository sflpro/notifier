package com.sflpro.notifier.services.notification.impl.push;

import com.sflpro.notifier.spi.push.PushMessageServiceRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * Created by Hayk Mkrtchyan.
 * Date: 7/3/19
 * Time: 5:52 PM
 */
@Configuration
@ComponentScan("com.sflpro.notifier.externalclients.push.registry")
class PushNotificationConfiguration {

    private static final Logger logger = LoggerFactory.getLogger(PushNotificationConfiguration.class);

    @Bean
    PushMessageServiceProvider pushMessageServiceProvider(final List<PushMessageServiceRegistry> registries) {
        if (CollectionUtils.isEmpty(registries)) {
            logger.info("No push notification service was registered for sending(subscribing) push notifications.");
        }
        return new DefaultPushMessageServiceProvider(registries);
    }
}
