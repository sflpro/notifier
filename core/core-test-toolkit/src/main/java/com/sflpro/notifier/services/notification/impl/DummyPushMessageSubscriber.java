package com.sflpro.notifier.services.notification.impl;

import com.sflpro.notifier.spi.push.PushMessageSubscriber;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Hayk Mkrtchyan.
 * Date: 7/3/19
 * Time: 6:01 PM
 */
public class DummyPushMessageSubscriber implements PushMessageSubscriber {

    private static final Logger logger = LoggerFactory.getLogger(DummySimpleSmsSender.class);

    private static final int MAX_LENGTH_DEVICE_ENDPOINT_ARN = 36;

    @Override
    public String refreshDeviceEndpointArn(final String existingDeviceEndpointArn, final String userDeviceToken, final String applicationArn) {
        logger.debug("Simulating deviceEndpointArn refresh - existingDeviceEndpointArn = {}, userDeviceToken = {}, applicationArn = {}",
                existingDeviceEndpointArn, userDeviceToken, applicationArn);
        return existingDeviceEndpointArn;
    }

    @Override
    public String registerDeviceEndpointArn(final String userDeviceToken, final String applicationArn) {
        logger.debug("Simulating deviceEndpointArn refresh - userDeviceToken = {}, applicationArn = {}", userDeviceToken, applicationArn);
        if (userDeviceToken.length() > MAX_LENGTH_DEVICE_ENDPOINT_ARN) {
            return userDeviceToken.substring(0, MAX_LENGTH_DEVICE_ENDPOINT_ARN);
        }
        return userDeviceToken;
    }
}
