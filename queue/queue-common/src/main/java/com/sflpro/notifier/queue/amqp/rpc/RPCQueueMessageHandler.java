package com.sflpro.notifier.queue.amqp.rpc;

import javax.annotation.Nonnull;

/**
 * User: Ruben Dilanyan
 * Company: SFL LLC
 * Date: 12/12/14
 * Time: 1:45 PM
 */
public interface RPCQueueMessageHandler {

    /**
     * Handles and returns RPC message
     *
     * @param message
     * @return handledMessageResult
     */
    @Nonnull
    String handleMessage(@Nonnull final Object message);
}

