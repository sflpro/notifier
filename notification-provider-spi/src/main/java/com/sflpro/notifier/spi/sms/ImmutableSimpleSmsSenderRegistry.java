package com.sflpro.notifier.spi.sms;



/**
 * Created by Hayk Mkrtchyan.
 * Date: 6/18/19
 * Time: 5:21 PM
 */
final class ImmutableSimpleSmsSenderRegistry implements SimpleSmsSenderRegistry {

    private final String name;
    private final SimpleSmsSender sender;

    ImmutableSimpleSmsSenderRegistry(final String name, final SimpleSmsSender sender) {
        this.name = name;
        this.sender = sender;
    }

    @Override
    public String name() {
        return name;
    }

    @Override
    public SimpleSmsSender sender() {
        return sender;
    }
}
