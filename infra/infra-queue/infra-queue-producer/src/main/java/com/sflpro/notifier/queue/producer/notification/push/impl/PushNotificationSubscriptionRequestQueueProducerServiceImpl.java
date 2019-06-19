package com.sflpro.notifier.queue.producer.notification.push.impl;

import com.sflpro.notifier.queue.amqp.model.notification.push.PushNotificationSubscriptionRequestRPCTransferModel;
import com.sflpro.notifier.queue.amqp.rpc.RPCCallType;
import com.sflpro.notifier.queue.producer.connector.AmqpConnectorService;
import com.sflpro.notifier.queue.producer.connector.AmqpResponseHandler;
import com.sflpro.notifier.queue.producer.notification.push.PushNotificationSubscriptionRequestQueueProducerService;
import com.sflpro.notifier.services.notification.event.push.StartPushNotificationSubscriptionRequestProcessingEvent;
import com.sflpro.notifier.services.notification.event.push.StartPushNotificationSubscriptionRequestProcessingEventListenerAdapter;
import com.sflpro.notifier.services.system.event.ApplicationEventDistributionService;
import org.apache.commons.lang3.time.StopWatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.annotation.Nonnull;

/**
 * User: Ruben Dilanyan
 * Company: SFL LLC
 * Date: 8/22/15
 * Time: 8:02 PM
 */
@Service
public class PushNotificationSubscriptionRequestQueueProducerServiceImpl implements PushNotificationSubscriptionRequestQueueProducerService, InitializingBean {

    private static final Logger LOGGER = LoggerFactory.getLogger(PushNotificationSubscriptionRequestQueueProducerServiceImpl.class);

    /* Dependencies */
    @Autowired
    private ApplicationEventDistributionService applicationEventDistributionService;

    @Autowired
    private AmqpConnectorService amqpConnectorService;

    /* Constructors */
    public PushNotificationSubscriptionRequestQueueProducerServiceImpl() {
        LOGGER.debug("Initializing push notification subscription request queue producer service");
    }

    @Override
    public void afterPropertiesSet() {
        applicationEventDistributionService.subscribe(new StartPushNotificationProcessingEventListener());
    }

    @Override
    public void processPushNotificationSubscriptionRequest(@Nonnull final Long requestId) {
        Assert.notNull(requestId, "Push notification subscription request should not be null");
        LOGGER.debug("Processing push notification subscription request with id - {}", requestId);
        amqpConnectorService.publishMessage(RPCCallType.START_PUSH_NOTIFICATION_SUBSCRIPTION_PROCESSING, new PushNotificationSubscriptionRequestRPCTransferModel(requestId), PushNotificationSubscriptionRequestRPCTransferModel.class, new PushNotificationSubscriptionRPCResponseHandler());
    }

    /* Properties getters and setters */
    public ApplicationEventDistributionService getApplicationEventDistributionService() {
        return applicationEventDistributionService;
    }

    public void setApplicationEventDistributionService(final ApplicationEventDistributionService applicationEventDistributionService) {
        this.applicationEventDistributionService = applicationEventDistributionService;
    }

    public AmqpConnectorService getAmqpConnectorService() {
        return amqpConnectorService;
    }

    public void setAmqpConnectorService(final AmqpConnectorService amqpConnectorService) {
        this.amqpConnectorService = amqpConnectorService;
    }

    /* Inner classes */
    private class StartPushNotificationProcessingEventListener extends StartPushNotificationSubscriptionRequestProcessingEventListenerAdapter {

        /* Constructors */
        public StartPushNotificationProcessingEventListener() {
        }

        @Override
        protected void processPushNotificationSubscriptionRequestEvent(final StartPushNotificationSubscriptionRequestProcessingEvent event) {
            PushNotificationSubscriptionRequestQueueProducerServiceImpl.this.processPushNotificationSubscriptionRequest(event.getPushNotificationSubscriptionRequestId());
        }
    }

    private static class PushNotificationSubscriptionRPCResponseHandler implements AmqpResponseHandler<PushNotificationSubscriptionRequestRPCTransferModel> {

        private final StopWatch stopWatch;

        /* Constructors */
        public PushNotificationSubscriptionRPCResponseHandler() {
            this.stopWatch = new StopWatch();
            stopWatch.start();
        }

        @Override
        public void handleResponse(@Nonnull final PushNotificationSubscriptionRequestRPCTransferModel responseModel) {
            stopWatch.stop();
            LOGGER.debug("Finalized processing push notification subscription request, response model - {}, duration - {}", responseModel, stopWatch.getTime());

        }
    }
}
