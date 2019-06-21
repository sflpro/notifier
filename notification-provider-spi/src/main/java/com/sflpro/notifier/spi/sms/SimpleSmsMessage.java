package com.sflpro.notifier.sms;

import org.springframework.util.Assert;

/**
 * Created by Hayk Mkrtchyan.
 * Date: 6/18/19
 * Time: 3:48 PM
 */
public interface SimpleSmsMessage extends SmsMessage{

    String messageBody();

    static SimpleSmsMessage of(final String sender, final String recipientNumber, final String messageBody) {
        Assert.hasText(sender, "Null or empty text was passed as an argument for parameter 'sender'.");
        Assert.hasText(recipientNumber, "Null or empty text was passed as an argument for parameter 'recipientNumber'.");
        Assert.hasText(messageBody, "Null or empty text was passed as an argument for parameter 'messageBody'.");
        return new ImmutableSimpleSmsMessage(sender, recipientNumber, messageBody);
    }
}
