package com.sflpro.notifier.queue.amqp.rpc;

/**
 * User: Ruben Dilanyan
 * Company: SFL LLC
 * Date: 12/13/14
 * Time: 11:54 PM
 */
public enum RPCCallType {
    START_NOTIFICATION_PROCESSING("notification.processing.start"),
    START_PUSH_NOTIFICATION_SUBSCRIPTION_PROCESSING("notification.push.subscription.processing.start");

    /* Properties */
    private final String callIdentifier;

    RPCCallType(final String callIdentifier) {
        this.callIdentifier = callIdentifier;
    }

    /* Properties getters and setters */

    public String getCallIdentifier() {
        return callIdentifier;
    }
}
