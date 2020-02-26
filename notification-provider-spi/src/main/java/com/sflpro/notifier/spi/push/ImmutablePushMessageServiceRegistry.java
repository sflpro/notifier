package com.sflpro.notifier.spi.push;

/**
 * Created by Hayk Mkrtchyan.
 * Date: 7/3/19
 * Time: 10:59 AM
 */
final class ImmutablePushMessageServiceRegistry implements PushMessageServiceRegistry {

    private final String name;
    private final PushMessageSender sender;
    private final PushMessageSubscriber subscriber;

    ImmutablePushMessageServiceRegistry(final String name, final PushMessageSender sender, final PushMessageSubscriber subscriber) {
        this.name = name;
        this.sender = sender;
        this.subscriber = subscriber;
    }

    @Override
    public String name() {
        return name;
    }

    @Override
    public PushMessageSender sender() {
        return sender;
    }

    @Override
    public PushMessageSubscriber subscriber() {
        return subscriber;
    }
}
