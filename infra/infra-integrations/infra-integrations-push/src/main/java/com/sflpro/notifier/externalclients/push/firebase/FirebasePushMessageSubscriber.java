package com.sflpro.notifier.externalclients.push.firebase;

import com.sflpro.notifier.spi.push.PushMessageSubscriber;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Hayk Mkrtchyan.
 * Date: 7/5/19
 * Time: 4:29 PM
 */
class FirebasePushMessageSubscriber implements PushMessageSubscriber {

    private static final Logger logger = LoggerFactory.getLogger(FirebasePushMessageSender.class);

    @Override
    public String refreshDeviceEndpointArn(final String existingDeviceEndpointArn, final String userDeviceToken, final String applicationArn) {
        logger.debug("No external API calls are needed just returning provided device token,refreshDeviceEndpointArn was called with arguments existingDeviceEndpointArn - {}, userDeviceToken-{}, applicationArn - {}", existingDeviceEndpointArn, userDeviceToken, applicationArn);
        return userDeviceToken;
    }

    @Override
    public String registerDeviceEndpointArn(final String userDeviceToken, final String applicationArn) {
        logger.debug("No external API calls are needed just returning provided device token.registerDeviceEndpointArn was called with arguments userDeviceToken-{}, applicationArn - {}", userDeviceToken, applicationArn);
        return userDeviceToken;
    }
}
