package com.sflpro.notifier.sms;

import org.springframework.util.Assert;

/**
 * Created by Hayk Mkrtchyan.
 * Date: 6/18/19
 * Time: 3:49 PM
 */
public interface SmsMessageSendingResult {

    String sid();

    static SmsMessageSendingResult of(final String sid) {
        Assert.hasText(sid, "Null or empty text was passed as an argument for parameter 'sid'.");
        return new ImmutableSmsMessageSendingResult(sid);
    }
}
