package com.sflpro.notifier.services.notification.impl;

import com.sflpro.notifier.spi.push.PushMessage;
import com.sflpro.notifier.spi.push.PushMessageSender;
import com.sflpro.notifier.spi.push.PushMessageSendingResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Hayk Mkrtchyan.
 * Date: 7/3/19
 * Time: 6:00 PM
 */
public class DummyPushMessageSender implements PushMessageSender {

    private static final Logger logger = LoggerFactory.getLogger(DummyPushMessageSender.class);

    @Override
    public PushMessageSendingResult send(final PushMessage message) {
        logger.debug("Simulating simple push notification sending for {}", message);
        return PushMessageSendingResult.of(message.deviceEndpointArn());
    }
}
