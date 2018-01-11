package com.sflpro.notifier.queue.producer.connector;

import com.sflpro.notifier.queue.amqp.model.AbstractRPCTransferModel;
import com.sflpro.notifier.queue.amqp.rpc.RPCCallType;

import javax.annotation.Nonnull;

/**
 * User: Ruben Dilanyan
 * Company: SFL LLC
 * Date: 12/13/14
 * Time: 11:44 PM
 */
public interface AmqpConnectorService {

    /**
     * Publish amqp message
     *
     * @param callType
     */
    <T extends AbstractRPCTransferModel> void publishMessage(@Nonnull final RPCCallType callType, @Nonnull final AbstractRPCTransferModel requestModel, @Nonnull final Class<T> responseModelClass, @Nonnull final AmqpResponseHandler<T> responseHandler);
}
