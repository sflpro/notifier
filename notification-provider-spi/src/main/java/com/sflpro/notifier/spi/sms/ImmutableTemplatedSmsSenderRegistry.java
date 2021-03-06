package com.sflpro.notifier.spi.sms;

/**
 * Created by Hayk Mkrtchyan.
 * Date: 6/18/19
 * Time: 5:21 PM
 */
final class ImmutableTemplatedSmsSenderRegistry implements TemplatedSmsSenderRegistry {

    private final String name;
    private final TemplatedSmsSender sender;

    ImmutableTemplatedSmsSenderRegistry(final String name, final TemplatedSmsSender sender) {
        this.name = name;
        this.sender = sender;
    }

    @Override
    public String name() {
        return name;
    }

    @Override
    public TemplatedSmsSender sender() {
        return sender;
    }
}
