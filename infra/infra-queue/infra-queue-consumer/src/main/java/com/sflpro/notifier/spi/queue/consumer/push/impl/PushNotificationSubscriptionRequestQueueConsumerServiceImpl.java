package com.sflpro.notifier.queue.consumer.push.impl;

import com.sflpro.notifier.queue.consumer.push.PushNotificationSubscriptionRequestQueueConsumerService;
import com.sflpro.notifier.services.notification.push.PushNotificationSubscriptionRequestProcessingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.annotation.Nonnull;

/**
 * User: Ruben Dilanyan
 * Company: SFL LLC
 * Date: 8/22/15
 * Time: 8:17 PM
 */
@Service
@Lazy(false)
public class PushNotificationSubscriptionRequestQueueConsumerServiceImpl implements PushNotificationSubscriptionRequestQueueConsumerService {

    private static final Logger LOGGER = LoggerFactory.getLogger(PushNotificationSubscriptionRequestQueueConsumerServiceImpl.class);

    /* Dependencies */
    @Autowired
    private PushNotificationSubscriptionRequestProcessingService pushNotificationSubscriptionRequestProcessingService;

    /* Constructors */
    public PushNotificationSubscriptionRequestQueueConsumerServiceImpl() {
        LOGGER.debug("Initializing push notification subscription request queue consumer service");
    }

    @Override
    public void processPushNotificationSubscriptionRequest(@Nonnull final Long requestId) {
        Assert.notNull(requestId, "Push notification subscription request id should not be null");
        LOGGER.debug("Processing push notification subscription request with id - {}", requestId);
        pushNotificationSubscriptionRequestProcessingService.processPushNotificationSubscriptionRequest(requestId);
    }

    /* Properties getters and setters */
    public PushNotificationSubscriptionRequestProcessingService getPushNotificationSubscriptionRequestProcessingService() {
        return pushNotificationSubscriptionRequestProcessingService;
    }

    public void setPushNotificationSubscriptionRequestProcessingService(final PushNotificationSubscriptionRequestProcessingService pushNotificationSubscriptionRequestProcessingService) {
        this.pushNotificationSubscriptionRequestProcessingService = pushNotificationSubscriptionRequestProcessingService;
    }
}
