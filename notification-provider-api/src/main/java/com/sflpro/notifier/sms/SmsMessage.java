package com.sflpro.notifier.sms;

import org.springframework.util.Assert;

/**
 * Created by Hayk Mkrtchyan.
 * Date: 6/18/19
 * Time: 3:48 PM
 */
public interface SmsMessage {

    String senderNumber();

    String recipientNumber();

    String messageBody();

    static SmsMessage of(final String senderNumber, final String recipientNumber, final String messageBody) {
        Assert.hasText(senderNumber, "Null or empty text was passed as an argument for parameter 'senderNumber'.");
        Assert.hasText(recipientNumber, "Null or empty text was passed as an argument for parameter 'recipientNumber'.");
        Assert.hasText(messageBody, "Null or empty text was passed as an argument for parameter 'messageBody'.");
        return new ImmutableSmsMessage(senderNumber, recipientNumber, messageBody);
    }
}
