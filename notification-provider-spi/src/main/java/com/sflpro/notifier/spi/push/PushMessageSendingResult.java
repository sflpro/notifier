package com.sflpro.notifier.spi.push;

import org.springframework.util.Assert;

/**
 * Created by Hayk Mkrtchyan.
 * Date: 7/3/19
 * Time: 10:43 AM
 */
public interface PushMessageSendingResult {

    String messageId();

    static PushMessageSendingResult of(final String messageId){
        Assert.hasText(messageId, "Null or empty text was passed as an argument for parameter 'messageId'.");
        return new ImmutablePushMessageSendingResult(messageId);
    }
}
