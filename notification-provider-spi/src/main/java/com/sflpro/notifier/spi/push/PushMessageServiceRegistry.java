package com.sflpro.notifier.spi.push;

import org.springframework.util.Assert;

/**
 * Created by Hayk Mkrtchyan.
 * Date: 7/3/19
 * Time: 10:58 AM
 */
public interface PushMessageServiceRegistry {

    String name();

    PushMessageSender sender();

    PushMessageSubscriber subscriber();

    static PushMessageServiceRegistry of(final String name,
                                         final PushMessageSender sender,
                                         final PushMessageSubscriber subscriber) {
        Assert.hasText(name, "Null or empty text was passed as an argument for parameter 'name'.");
        Assert.hasText(name, "Null was passed as an argument for parameter 'sender'.");
        Assert.hasText(name, "Null text was passed as an argument for parameter 'subscriber'.");
        return new ImmutablePushMessageServiceRegistry(name, sender, subscriber);
    }

}
