package com.sflpro.notifier.sms;

import jdk.nashorn.internal.ir.annotations.Immutable;

/**
 * Created by Hayk Mkrtchyan.
 * Date: 6/18/19
 * Time: 5:21 PM
 */
@Immutable
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
