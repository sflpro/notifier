package com.sfl.nms.queue.producer.connector;

import com.sfl.nms.queue.amqp.model.AbstractRPCTransferModel;

import javax.annotation.Nonnull;

/**
 * User: Ruben Dilanyan
 * Company: SFL LLC
 * Date: 12/14/14
 * Time: 1:10 AM
 */
public interface AmqpResponseHandler<T extends AbstractRPCTransferModel> {

    /**
     * Handles AMQP response model
     *
     * @param responseModel
     */
    void handleResponse(@Nonnull final T responseModel);
}
