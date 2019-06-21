package com.sflpro.notifier.spi.sms;

import org.springframework.util.Assert;

/**
 * Created by Hayk Mkrtchyan.
 * Date: 6/18/19
 * Time: 5:21 PM
 */
public interface TemplatedSmsSenderRegistry {

    String name();

    TemplatedSmsSender sender();

    static TemplatedSmsSenderRegistry of(final String name, final TemplatedSmsSender sender) {
        Assert.hasText(name, "Null or empty text was passed as an argument for parameter 'sender'.");
        Assert.notNull(sender, "Null was passed as an argument for parameter 'sender'.");
        return new ImmutableTemplatedSmsSenderRegistry(name, sender);
    }
}
