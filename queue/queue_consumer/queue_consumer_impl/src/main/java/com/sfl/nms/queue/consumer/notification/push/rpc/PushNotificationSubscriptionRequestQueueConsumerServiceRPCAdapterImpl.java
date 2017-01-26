package com.sfl.nms.queue.consumer.notification.push.rpc;

import com.sfl.nms.persistence.utility.PersistenceUtilityService;
import com.sfl.nms.queue.amqp.model.notification.push.PushNotificationSubscriptionRequestRPCTransferModel;
import com.sfl.nms.queue.amqp.rpc.RPCCallType;
import com.sfl.nms.queue.amqp.rpc.impl.AbstractRPCServiceAdapterImpl;
import com.sfl.nms.queue.amqp.rpc.message.RPCMethodHandler;
import com.sfl.nms.queue.consumer.notification.push.PushNotificationSubscriptionRequestQueueConsumerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * User: Ruben Dilanyan
 * Company: SFL LLC
 * Date: 8/22/15
 * Time: 8:27 PM
 */
@Component("pushNotificationSubscriptionRequestQueueConsumerServiceRPCAdapter")
public class PushNotificationSubscriptionRequestQueueConsumerServiceRPCAdapterImpl extends AbstractRPCServiceAdapterImpl {

    private static final Logger LOGGER = LoggerFactory.getLogger(PushNotificationSubscriptionRequestQueueConsumerServiceRPCAdapterImpl.class);

    /* Dependencies */
    @Autowired
    private PushNotificationSubscriptionRequestQueueConsumerService pushNotificationSubscriptionRequestQueueConsumerService;

    @Autowired
    private PersistenceUtilityService persistenceUtilityService;

    /* Constructors */
    public PushNotificationSubscriptionRequestQueueConsumerServiceRPCAdapterImpl() {
        LOGGER.debug("Initializing push notification subscription request queue consumer service rpc adapter");
    }


    @Override
    public void afterPropertiesSet() {
        addMethodHandler(new RPCMethodHandler<PushNotificationSubscriptionRequestRPCTransferModel>(RPCCallType.START_PUSH_NOTIFICATION_SUBSCRIPTION_PROCESSING.getCallIdentifier(), PushNotificationSubscriptionRequestRPCTransferModel.class) {
            @Override
            public Object executeMethod(final Object parameter) {
                final PushNotificationSubscriptionRequestRPCTransferModel rpcTransferModel = (PushNotificationSubscriptionRequestRPCTransferModel) parameter;
                /* Process consumer service call */
                persistenceUtilityService.runInPersistenceSession(() -> {
                    pushNotificationSubscriptionRequestQueueConsumerService.processPushNotificationSubscriptionRequest(rpcTransferModel.getSubscriptionRequestId());
                });
                return rpcTransferModel;
            }
        });
    }
}
