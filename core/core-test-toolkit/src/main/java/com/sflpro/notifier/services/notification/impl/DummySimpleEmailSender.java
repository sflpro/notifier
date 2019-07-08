package com.sflpro.notifier.services.notification.impl;


import com.sflpro.notifier.spi.email.SimpleEmailMessage;
import com.sflpro.notifier.spi.email.SimpleEmailSender;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Hayk Mkrtchyan.
 * Date: 6/19/19
 * Time: 12:36 PM
 */
public class DummySimpleEmailSender implements SimpleEmailSender {

    private static final Logger logger = LoggerFactory.getLogger(DummySimpleEmailSender.class);

    @Override
    public void send(final SimpleEmailMessage message) {
        logger.debug("Simulating simple email sending for {}", message);
    }
}
