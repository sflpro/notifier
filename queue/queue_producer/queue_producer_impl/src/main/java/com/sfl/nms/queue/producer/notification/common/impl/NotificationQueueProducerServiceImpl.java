package com.sfl.nms.queue.producer.notification.common.impl;

import com.sfl.nms.queue.amqp.model.notification.NotificationRPCTransferModel;
import com.sfl.nms.queue.amqp.rpc.RPCCallType;
import com.sfl.nms.queue.producer.notification.common.NotificationQueueProducerService;
import com.sfl.nms.queue.producer.connector.AmqpConnectorService;
import com.sfl.nms.queue.producer.connector.AmqpResponseHandler;
import com.sfl.nms.services.notification.event.sms.StartSendingNotificationEvent;
import com.sfl.nms.services.notification.event.sms.StartSendingNotificationEventListenerAdapter;
import com.sfl.nms.services.system.event.ApplicationEventDistributionService;
import org.apache.commons.lang3.time.StopWatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.annotation.Nonnull;

/**
 * User: Mher Sargsyan
 * Company: SFL LLC
 * Date: 4/10/15
 * Time: 7:55 PM
 */
@Service
@Lazy(false)
public class NotificationQueueProducerServiceImpl implements NotificationQueueProducerService, InitializingBean {

    private static final Logger LOGGER = LoggerFactory.getLogger(NotificationQueueProducerServiceImpl.class);

    /* Dependencies */
    @Autowired
    private ApplicationEventDistributionService applicationEventDistributionService;

    @Autowired
    private AmqpConnectorService amqpConnectorService;

    /* Constructors */
    public NotificationQueueProducerServiceImpl() {
        LOGGER.debug("Initializing sms notification queue producer service");
    }

    @Override
    public void afterPropertiesSet() {
        applicationEventDistributionService.subscribe(new StartSendingNotificationEventListener());
    }


    /* Public methods */
    @Override
    public void processStartSendingNotificationEvent(@Nonnull final Long notificationId) {
        Assert.notNull(notificationId, "Notification id should not be null.");
        LOGGER.debug("Processing notification sending event for notification by id - {}", notificationId);
        amqpConnectorService.publishMessage(RPCCallType.START_NOTIFICATION_PROCESSING, new NotificationRPCTransferModel(notificationId), NotificationRPCTransferModel.class, new NotificationMessageSendingEventListenerRPCResponseHandler());
    }

    /* Inner classes */
    private class StartSendingNotificationEventListener extends StartSendingNotificationEventListenerAdapter {

        /* Constructors */
        public StartSendingNotificationEventListener() {
        }

        @Override
        protected void processStartSendingNotificationEvent(final StartSendingNotificationEvent event) {
            NotificationQueueProducerServiceImpl.this.processStartSendingNotificationEvent(event.getNotificationId());
        }
    }

    private static class NotificationMessageSendingEventListenerRPCResponseHandler implements AmqpResponseHandler<NotificationRPCTransferModel> {

        private final StopWatch stopWatch;

        /* Constructors */
        public NotificationMessageSendingEventListenerRPCResponseHandler() {
            this.stopWatch = new StopWatch();
            stopWatch.start();
        }

        @Override
        public void handleResponse(@Nonnull final NotificationRPCTransferModel responseModel) {
            stopWatch.stop();
            LOGGER.debug("Finalized sending notification, response model - {}, duration - {}", responseModel, stopWatch.getTime());

        }
    }
}
