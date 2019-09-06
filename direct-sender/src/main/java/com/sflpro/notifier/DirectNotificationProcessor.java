package com.sflpro.notifier;

import com.sflpro.notifier.services.notification.NotificationProcessingService;
import com.sflpro.notifier.services.notification.event.push.StartPushNotificationSubscriptionRequestProcessingEvent;
import com.sflpro.notifier.services.notification.event.push.StartPushNotificationSubscriptionRequestProcessingEventListenerAdapter;
import com.sflpro.notifier.services.notification.event.sms.StartSendingNotificationEvent;
import com.sflpro.notifier.services.notification.event.sms.StartSendingNotificationEventListenerAdapter;
import com.sflpro.notifier.services.notification.push.PushNotificationSubscriptionRequestProcessingService;
import com.sflpro.notifier.services.system.event.ApplicationEventDistributionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

/**
 * Created by Hayk Mkrtchyan.
 * Date: 7/9/19
 * Time: 10:31 AM
 */
class DirectNotificationProcessor {

    private static final Logger logger = LoggerFactory.getLogger(DirectNotificationProcessor.class);

    private final NotificationProcessingService notificationProcessingService;

    private final PushNotificationSubscriptionRequestProcessingService pushNotificationSubscriptionRequestProcessingService;

    private final Executor executor;

    DirectNotificationProcessor(final NotificationProcessingService notificationProcessingService,
                                final PushNotificationSubscriptionRequestProcessingService pushNotificationSubscriptionRequestProcessingService,
                                final ApplicationEventDistributionService applicationEventDistributionService,
                                final Executor executor) {
        this.notificationProcessingService = notificationProcessingService;
        this.pushNotificationSubscriptionRequestProcessingService = pushNotificationSubscriptionRequestProcessingService;
        this.executor = executor;
        applicationEventDistributionService.subscribe(new StartSendingNotificationEventListenerAdapter() {
            @Override
            protected void processStartSendingNotificationEvent(final StartSendingNotificationEvent event) {
                DirectNotificationProcessor.this.processStartSendingNotificationEvent(event);
            }
        });
        applicationEventDistributionService.subscribe(new StartPushNotificationSubscriptionRequestProcessingEventListenerAdapter() {
            @Override
            protected void processPushNotificationSubscriptionRequestEvent(final StartPushNotificationSubscriptionRequestProcessingEvent event) {
                DirectNotificationProcessor.this.processPushNotificationSubscriptionRequestEvent(event);
            }
        });

    }

    private void processPushNotificationSubscriptionRequestEvent(final StartPushNotificationSubscriptionRequestProcessingEvent event) {
        Assert.notNull(event.getPushNotificationSubscriptionRequestId(), "Notification pushNotificationSubscriptionRequestId should not be null.");
        logger.debug("Processing push notification subscription request with id - {}", event.getPushNotificationSubscriptionRequestId());
        processPushNotificationSubscriptionRequest(event.getPushNotificationSubscriptionRequestId());
    }

    private void processStartSendingNotificationEvent(final StartSendingNotificationEvent event) {
        Assert.notNull(event.getNotificationId(), "Notification id should not be null.");
        Assert.notNull(event.getSecureProperties(), "Secure properties should not be null.");
        logger.debug("Processing notification sending event for notification by id - {}", event.getNotificationId());
        processNotification(event.getNotificationId(), event.getSecureProperties());
    }

    private void processNotification(final Long notificationId, final Map<String, String> secureProperties) {
        CompletableFuture.runAsync(() -> notificationProcessingService.processNotification(notificationId, secureProperties), executor).whenComplete((aVoid, th) -> {
            if (th != null) {
                logger.error("Failure when sending notifications directly, notification id is {}", notificationId, th);
            } else {
                logger.debug("Notification with id {} was successfully sent.", notificationId);
            }
        });
    }


    private void processPushNotificationSubscriptionRequest(final Long requestId) {
        CompletableFuture.runAsync(() -> pushNotificationSubscriptionRequestProcessingService.processPushNotificationSubscriptionRequest(requestId), executor).whenComplete((aVoid, th) -> {
            if (th != null) {
                logger.error("Failure when processing push notification subscriptions directly, request id is {}", requestId, th);
            } else {
                logger.debug("Notification subscription with id {} was successfully processed.", requestId);
            }
        });
    }
}
