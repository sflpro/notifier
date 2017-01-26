package com.sfl.nms.externalclients.push.amazon.communicator;

import com.sfl.nms.externalclients.push.amazon.model.request.GetDeviceEndpointAttributesRequest;
import com.sfl.nms.externalclients.push.amazon.model.request.SendPushNotificationRequestMessageInformation;
import com.sfl.nms.externalclients.push.amazon.model.request.UpdateDeviceEndpointAttributesRequest;
import com.sfl.nms.externalclients.push.amazon.model.response.GetDeviceEndpointAttributesResponse;
import com.sfl.nms.externalclients.push.amazon.model.response.SendPushNotificationResponse;
import com.sfl.nms.externalclients.push.amazon.model.request.RegisterUserDeviceTokenRequest;
import com.sfl.nms.externalclients.push.amazon.model.response.RegisterUserDeviceTokenResponse;

import javax.annotation.Nonnull;

/**
 * User: Ruben Dilanyan
 * Company: SFL LLC
 * Date: 12/25/14
 * Time: 6:50 PM
 */
public interface AmazonSnsApiCommunicator {

    /**
     * Send Push notification
     *
     * @param messageInformation
     * @param deviceEndpointArn
     * @return sendMessageResponse
     */
    @Nonnull
    SendPushNotificationResponse sendPushNotification(@Nonnull final SendPushNotificationRequestMessageInformation messageInformation, @Nonnull final String deviceEndpointArn);

    /**
     * Registers user device token with Amazon SNS
     *
     * @param request
     * @return registerUserDeviceTokenResponse
     */
    @Nonnull
    RegisterUserDeviceTokenResponse registerUserDeviceToken(@Nonnull final RegisterUserDeviceTokenRequest request);

    /**
     * Gets device endpoint attributes
     *
     * @param request
     * @return response
     */
    @Nonnull
    GetDeviceEndpointAttributesResponse getDeviceEndpointAttributes(@Nonnull final GetDeviceEndpointAttributesRequest request);

    /**
     * Updates device endpoint attributes
     *
     * @param request
     */
    void updateDeviceEndpointAttributes(@Nonnull final UpdateDeviceEndpointAttributesRequest request);
}
