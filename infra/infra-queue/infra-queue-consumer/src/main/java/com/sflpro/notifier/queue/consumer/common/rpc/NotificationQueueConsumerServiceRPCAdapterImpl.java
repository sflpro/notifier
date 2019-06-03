package com.sflpro.notifier.queue.consumer.common.rpc;

import com.sflpro.notifier.db.repositories.utility.PersistenceUtilityService;
import com.sflpro.notifier.queue.amqp.model.notification.NotificationRPCTransferModel;
import com.sflpro.notifier.queue.amqp.rpc.RPCCallType;
import com.sflpro.notifier.queue.amqp.rpc.impl.AbstractRPCServiceAdapterImpl;
import com.sflpro.notifier.queue.amqp.rpc.message.RPCMethodHandler;
import com.sflpro.notifier.queue.consumer.common.NotificationQueueConsumerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * User: Ruben Dilanyan
 * Company: SFL LLC
 * Date: 2/11/15
 * Time: 5:28 PM
 */
@Component("notificationQueueConsumerServiceRPCAdapter")
public class NotificationQueueConsumerServiceRPCAdapterImpl extends AbstractRPCServiceAdapterImpl {
    private static final Logger LOGGER = LoggerFactory.getLogger(NotificationQueueConsumerServiceRPCAdapterImpl.class);

    /* Dependencies */
    @Autowired
    private NotificationQueueConsumerService notificationQueueConsumerService;

    @Autowired
    private PersistenceUtilityService persistenceUtilityService;

    /* Constructors */
    public NotificationQueueConsumerServiceRPCAdapterImpl() {
        LOGGER.debug("Initializing notification queue consumer service rpc adapter");
    }


    @Override
    public void afterPropertiesSet() {
        addMethodHandler(new RPCMethodHandler<NotificationRPCTransferModel>(RPCCallType.START_NOTIFICATION_PROCESSING.getCallIdentifier(), NotificationRPCTransferModel.class) {
            @Override
            public Object executeMethod(final Object parameter) {
                final NotificationRPCTransferModel notificationRPCTransferModel = (NotificationRPCTransferModel) parameter;
                /* Process consumer service call */
                persistenceUtilityService.runInPersistenceSession(() ->
                        notificationQueueConsumerService.processNotification(notificationRPCTransferModel.getNotificationId())
                );
                return notificationRPCTransferModel;
            }
        });
    }
}
