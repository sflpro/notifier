package com.sflpro.notifier.services.notification.impl;


import com.sflpro.notifier.spi.email.TemplatedEmailMessage;
import com.sflpro.notifier.spi.email.TemplatedEmailSender;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Hayk Mkrtchyan.
 * Date: 6/19/19
 * Time: 12:36 PM
 */
public class DummyTemplatedEmailSender implements TemplatedEmailSender {

    private static final Logger LOGGER = LoggerFactory.getLogger(DummyTemplatedEmailSender.class);

    @Override
    public void send(final TemplatedEmailMessage message) {
        LOGGER.debug("Simulating templayed email sending for {}", message);
    }
}
