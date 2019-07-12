package com.sflpro.notifier.spi.push;

/**
 * Created by Hayk Mkrtchyan.
 * Date: 7/3/19
 * Time: 10:56 AM
 */
public interface PushMessageSubscriber {

    String refreshDeviceEndpointArn(final String existingDeviceEndpointArn, final String userDeviceToken,
                                    final String applicationArn);

    String registerDeviceEndpointArn(final String userDeviceToken,
                                     final String applicationArn);
}
