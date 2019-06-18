package com.sflpro.notifier.sms;

import jdk.nashorn.internal.ir.annotations.Immutable;

/**
 * Created by Hayk Mkrtchyan.
 * Date: 6/18/19
 * Time: 5:21 PM
 */
@Immutable
final class ImmutableSmsSenderRegistry implements SmsSenderRegistry{

    private final String name;
    private final SmsSender sender;

    ImmutableSmsSenderRegistry(final String name, final SmsSender sender) {
        this.name = name;
        this.sender = sender;
    }

    @Override
    public String name() {
        return name;
    }

    @Override
    public SmsSender sender() {
        return sender;
    }
}
