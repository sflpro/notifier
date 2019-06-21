package com.sflpro.notifier.email;

import org.springframework.util.Assert;

/**
 * Created by Hayk Mkrtchyan.
 * Date: 6/19/19
 * Time: 11:44 AM
 */
public interface TemplatedEmailSenderRegistry {

    String name();

    TemplatedEmailSender sender();

    static TemplatedEmailSenderRegistry of(final String name, final TemplatedEmailSender sender) {
        Assert.hasText(name, "Null or empty text was passed as an argument for parameter 'name'.");
        Assert.notNull(sender, "Null was passed as an argument for parameter 'sender'.");
        return new ImmutableTemplatedEmailSenderRegistry(name, sender);
    }
}
