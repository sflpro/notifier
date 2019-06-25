package com.sflpro.notifier.spi.sms;

import org.springframework.util.Assert;

/**
 * Created by Hayk Mkrtchyan.
 * Date: 6/18/19
 * Time: 5:21 PM
 */
public interface SimpleSmsSenderRegistry {

    String name();

    SimpleSmsSender sender();

    static SimpleSmsSenderRegistry of(final String name, final SimpleSmsSender sender) {
        Assert.hasText(name, "Null or empty text was passed as an argument for parameter 'sender'.");
        Assert.notNull(sender, "Null was passed as an argument for parameter 'sender'.");
        return new ImmutableSimpleSmsSenderRegistry(name, sender);
    }
}
