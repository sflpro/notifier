package com.sflpro.notifier.services.notification.impl;

import com.sflpro.notifier.spi.sms.SmsMessageSendingResult;
import com.sflpro.notifier.spi.sms.TemplatedSmsMessage;
import com.sflpro.notifier.spi.sms.TemplatedSmsSender;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

;

/**
 * Created by Hayk Mkrtchyan.
 * Date: 6/19/19
 * Time: 12:36 PM
 */
public class DummyTemplatedSmsSender implements TemplatedSmsSender {

    private static final Logger LOGGER = LoggerFactory.getLogger(DummyTemplatedSmsSender.class);

    @Override
    public SmsMessageSendingResult send(final TemplatedSmsMessage simpleSmsMessage) {
        LOGGER.debug("Simulating sms sending for {}", simpleSmsMessage);
        return SmsMessageSendingResult.of(simpleSmsMessage.recipientNumber());
    }
}
