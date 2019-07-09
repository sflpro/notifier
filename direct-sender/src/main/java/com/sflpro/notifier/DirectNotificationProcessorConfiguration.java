package com.sflpro.notifier;

import com.sflpro.notifier.services.notification.NotificationProcessingService;
import com.sflpro.notifier.services.notification.push.PushNotificationSubscriptionRequestProcessingService;
import com.sflpro.notifier.services.system.event.ApplicationEventDistributionService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

/**
 * Created by Hayk Mkrtchyan.
 * Date: 7/9/19
 * Time: 10:27 AM
 */
@ConditionalOnProperty(value = "notifier.queue.engine", havingValue = "none", matchIfMissing = true)
@Configuration
public class DirectNotificationProcessorConfiguration {

    @Bean
    Executor directSenderExecutor(@Value("${directSenderExecutor.corePoolSize:4}") final int corePoolSize,
                                  @Value("${directSenderExecutor.maxPoolSize:128}") final int maxPoolSize,
                                  @Value("${directSenderExecutor.queueCapacity:4056}") final int queueCapacity) {
        final ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(corePoolSize);
        executor.setMaxPoolSize(maxPoolSize);
        executor.setQueueCapacity(queueCapacity);
        return executor;
    }

    @Bean
    DirectNotificationProcessor directSender(final NotificationProcessingService notificationProcessingService,
                                             final PushNotificationSubscriptionRequestProcessingService pushNotificationSubscriptionRequestProcessingService,
                                             final ApplicationEventDistributionService applicationEventDistributionService,
                                             @Qualifier("directSenderExecutor") final Executor executor) {
        return new DirectNotificationProcessor(notificationProcessingService, pushNotificationSubscriptionRequestProcessingService, applicationEventDistributionService, executor);
    }
}
