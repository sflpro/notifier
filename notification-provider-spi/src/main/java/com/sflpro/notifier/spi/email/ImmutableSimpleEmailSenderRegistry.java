package com.sflpro.notifier.email;

/**
 * Created by Hayk Mkrtchyan.
 * Date: 6/19/19
 * Time: 11:48 AM
 */
final class ImmutableSimpleEmailSenderRegistry implements SimpleEmailSenderRegistry {

    private final String name;
    private final SimpleEmailSender sender;

    ImmutableSimpleEmailSenderRegistry(final String name, final SimpleEmailSender sender) {
        this.name = name;
        this.sender = sender;
    }

    @Override
    public String name() {
        return name;
    }

    @Override
    public SimpleEmailSender sender() {
        return sender;
    }
}
