package com.sflpro.notifier.services.notification.impl;

import com.sflpro.notifier.spi.push.PushMessageSubscriber;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;

/**
 * Created by Hayk Mkrtchyan.
 * Date: 7/3/19
 * Time: 6:01 PM
 */
public class DummyPushMessageSubscriber implements PushMessageSubscriber {

    private static final Logger logger = LoggerFactory.getLogger(DummySimpleSmsSender.class);

    @Override
    public String refreshDeviceEndpointArn(final String existingDeviceEndpointArn, final String userDeviceToken, final String applicationArn) {
        logger.debug("Simulating deviceEndpointArn refresh - existingDeviceEndpointArn = {}, userDeviceToken = {}, applicationArn = {}",
                existingDeviceEndpointArn, userDeviceToken, applicationArn);
        return existingDeviceEndpointArn;
    }

    @Override
    public String registerDeviceEndpointArn(final String userDeviceToken, final String applicationArn) {
        logger.debug("Simulating deviceEndpointArn refresh - userDeviceToken = {}, applicationArn = {}", userDeviceToken, applicationArn);
        return UUID.randomUUID().toString();
    }
}
