package com.sflpro.notifier.sms;

import org.springframework.util.Assert;

/**
 * Created by Hayk Mkrtchyan.
 * Date: 6/18/19
 * Time: 5:21 PM
 */
public interface SmsSenderRegistry {

    String name();

    SmsSender sender();

    static SmsSenderRegistry of(final String name, final SmsSender sender) {
        Assert.hasText(name, "Null or empty text was passed as an argument for parameter 'sender'.");
        Assert.notNull(sender, "Null was passed as an argument for parameter 'sender'.");
        return new ImmutableSmsSenderRegistry(name, sender);
    }
}
