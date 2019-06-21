package com.sflpro.notifier.email;

/**
 * Created by Hayk Mkrtchyan.
 * Date: 6/19/19
 * Time: 11:44 AM
 */
final class ImmutableTemplatedEmailSenderRegistry implements TemplatedEmailSenderRegistry{

    private final String name;
    private final TemplatedEmailSender sender;

    ImmutableTemplatedEmailSenderRegistry(final String name, final TemplatedEmailSender sender) {
        this.name = name;
        this.sender = sender;
    }

    @Override
    public String name() {
        return name;
    }

    @Override
    public TemplatedEmailSender sender() {
        return sender;
    }
}
