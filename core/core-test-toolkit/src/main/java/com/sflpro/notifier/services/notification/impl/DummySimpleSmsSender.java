package com.sflpro.notifier.services.notification.impl;

import com.sflpro.notifier.sms.SimpleSmsMessage;
import com.sflpro.notifier.sms.SmsMessageSendingResult;
import com.sflpro.notifier.sms.SimpleSmsSender;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Hayk Mkrtchyan.
 * Date: 6/19/19
 * Time: 12:36 PM
 */
public class DummySimpleSmsSender implements SimpleSmsSender {

    private static final Logger LOGGER = LoggerFactory.getLogger(DummySimpleSmsSender.class);

    @Override
    public SmsMessageSendingResult send(final SimpleSmsMessage simpleSmsMessage) {
        LOGGER.debug("Simulating sms sending for {}", simpleSmsMessage);
        return SmsMessageSendingResult.of(simpleSmsMessage.recipientNumber());
    }
}
