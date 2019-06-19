package com.sflpro.notifier.services.notification.impl;

import com.sflpro.notifier.sms.SmsMessage;
import com.sflpro.notifier.sms.SmsMessageSendingResult;
import com.sflpro.notifier.sms.SmsSender;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Hayk Mkrtchyan.
 * Date: 6/19/19
 * Time: 12:36 PM
 */
public class DummySmsSender implements SmsSender {

    private static final Logger LOGGER = LoggerFactory.getLogger(DummySmsSender.class);

    @Override
    public SmsMessageSendingResult send(final SmsMessage smsMessage) {
        LOGGER.debug("Simulating sms sending for {}", smsMessage);
        return SmsMessageSendingResult.of(smsMessage.recipientNumber());
    }
}
