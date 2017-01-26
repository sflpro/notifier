package com.sfl.nms.queue.amqp.rpc;


import com.sfl.nms.queue.amqp.rpc.message.RPCMethodHandler;

import javax.annotation.Nonnull;

/**
 * User: Ruben Dilanyan
 * Company: SFL LLC
 * Date: 12/12/14
 * Time: 1:39 PM
 */
public interface ServiceRPCAdaptersRegistry {

    /**
     * Returns RPC message handler for provided identifier
     *
     * @param identifier
     * @return rpcMethodHandler
     */
    RPCMethodHandler<?> getRpcMethodHandlerForIdentifier(@Nonnull final String identifier);
}
