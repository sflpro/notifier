package com.sflpro.notifier.services.notification.impl;


import com.sflpro.notifier.spi.sms.SimpleSmsMessage;
import com.sflpro.notifier.spi.sms.SimpleSmsSender;
import com.sflpro.notifier.spi.sms.SmsMessageSendingResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Hayk Mkrtchyan.
 * Date: 6/19/19
 * Time: 12:36 PM
 */
public class DummySimpleSmsSender implements SimpleSmsSender {

    private static final Logger logger = LoggerFactory.getLogger(DummySimpleSmsSender.class);

    @Override
    public SmsMessageSendingResult send(final SimpleSmsMessage simpleSmsMessage) {
        logger.debug("Simulating sms sending for {}", simpleSmsMessage);
        return SmsMessageSendingResult.of(simpleSmsMessage.recipientNumber());
    }
}
