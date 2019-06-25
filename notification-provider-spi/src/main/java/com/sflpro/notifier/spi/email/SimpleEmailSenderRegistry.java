package com.sflpro.notifier.spi.email;

import org.springframework.util.Assert;

/**
 * Created by Hayk Mkrtchyan.
 * Date: 6/19/19
 * Time: 11:44 AM
 */
public interface SimpleEmailSenderRegistry {

    String name();

    SimpleEmailSender sender();

    static SimpleEmailSenderRegistry of(final String name, final SimpleEmailSender sender) {
        Assert.hasText(name, "Null or empty text was passed as an argument for parameter 'name'.");
        Assert.notNull(sender, "Null was passed as an argument for parameter 'sender'.");
        return new ImmutableSimpleEmailSenderRegistry(name, sender);
    }
}
