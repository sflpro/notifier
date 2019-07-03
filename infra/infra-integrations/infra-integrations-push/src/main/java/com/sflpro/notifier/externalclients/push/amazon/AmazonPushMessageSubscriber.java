package com.sflpro.notifier.externalclients.push.amazon;

import com.amazonaws.AmazonServiceException;
import com.sflpro.notifier.externalclients.push.amazon.communicator.AmazonSnsApiCommunicator;
import com.sflpro.notifier.externalclients.push.amazon.model.request.GetDeviceEndpointAttributesRequest;
import com.sflpro.notifier.externalclients.push.amazon.model.request.RegisterUserDeviceTokenRequest;
import com.sflpro.notifier.externalclients.push.amazon.model.request.UpdateDeviceEndpointAttributesRequest;
import com.sflpro.notifier.externalclients.push.amazon.model.response.GetDeviceEndpointAttributesResponse;
import com.sflpro.notifier.externalclients.push.amazon.model.response.RegisterUserDeviceTokenResponse;
import com.sflpro.notifier.spi.push.PushMessageSubscriber;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

/**
 * Created by Hayk Mkrtchyan.
 * Date: 7/3/19
 * Time: 11:09 AM
 */
public class AmazonPushMessageSubscriber implements PushMessageSubscriber {

    private static final Logger logger = LoggerFactory.getLogger(AmazonPushMessageSubscriber.class);

    private final AmazonSnsApiCommunicator amazonSnsApiCommunicator;

    AmazonPushMessageSubscriber(final AmazonSnsApiCommunicator amazonSnsApiCommunicator) {
        this.amazonSnsApiCommunicator = amazonSnsApiCommunicator;
    }

    @Override
    public String refreshDeviceEndpointArn(final String existingDeviceEndpointArn, final String userDeviceToken, final String applicationArn) {
        Assert.hasText(existingDeviceEndpointArn, "Null or empty text was passed as an argument for parameter 'existingDeviceEndpointArn'.");
        assertValidDeviceTokenAndAplicationArn(userDeviceToken, applicationArn);
        return refreshDeviceEndpointArnInternal(existingDeviceEndpointArn, userDeviceToken, applicationArn);
    }

    @Override
    public String registerDeviceEndpointArn(final String userDeviceToken, final String applicationArn) {
        assertValidDeviceTokenAndAplicationArn(userDeviceToken, applicationArn);
        return refreshDeviceEndpointArnInternal(
                executeRegisterDeviceEndpointRequest(userDeviceToken, applicationArn),
                userDeviceToken,
                applicationArn
        );
    }

    private static void assertValidDeviceTokenAndAplicationArn(final String userDeviceToken, final String applicationArn) {
        Assert.hasText(userDeviceToken, "Null or empty text was passed as an argument for parameter 'userDeviceToken'.");
        Assert.hasText(applicationArn, "Null or empty text was passed as an argument for parameter 'applicationArn'.");
    }

    private String refreshDeviceEndpointArnInternal(final String existingDeviceEndpointArn, final String userDeviceToken,
                                                    final String applicationArn) {
        String deviceEndpointArn = existingDeviceEndpointArn != null ? existingDeviceEndpointArn : executeRegisterDeviceEndpointRequest(userDeviceToken, applicationArn);
        // Grab device endpoint attributes
        GetDeviceEndpointAttributesResponse getDeviceEndpointAttributesResponse = executeGetEndpointAttributesRequest(deviceEndpointArn);
        // Check if endpoint exists, this might be the case when old token is provided from outside
        if (!getDeviceEndpointAttributesResponse.isExists()) {
            // Create new endpoint and execute again attributes retrieval call again
            deviceEndpointArn = executeRegisterDeviceEndpointRequest(userDeviceToken, applicationArn);
            getDeviceEndpointAttributesResponse = executeGetEndpointAttributesRequest(deviceEndpointArn);
        }
        // Check if attributes has to be updated
        if (!getDeviceEndpointAttributesResponse.isEnabled() || !userDeviceToken.equals(getDeviceEndpointAttributesResponse.getToken())) {
            logger.debug("Updating user device endpoint attributes since either token does not match or endpoint is disabled. Attributes - {}, device token - {}", getDeviceEndpointAttributesResponse, userDeviceToken);
            try {
                final UpdateDeviceEndpointAttributesRequest updateDeviceEndpointAttributesRequest = new UpdateDeviceEndpointAttributesRequest(deviceEndpointArn, userDeviceToken, true);
                amazonSnsApiCommunicator.updateDeviceEndpointAttributes(updateDeviceEndpointAttributesRequest);
            } catch (final AmazonServiceException ex) {
                logger.error("Error occurred while trying to update device attributes for endpoint ARN - " + deviceEndpointArn, ex);
                // As fallback simply create new endpoint to get rid of old endpoint errors
                deviceEndpointArn = executeRegisterDeviceEndpointRequest(userDeviceToken, applicationArn);
            }
        }
        return deviceEndpointArn;
    }

    private GetDeviceEndpointAttributesResponse executeGetEndpointAttributesRequest(final String deviceEndpointArn) {
        final GetDeviceEndpointAttributesRequest getDeviceEndpointAttributesRequest = new GetDeviceEndpointAttributesRequest(deviceEndpointArn);
        return amazonSnsApiCommunicator.getDeviceEndpointAttributes(getDeviceEndpointAttributesRequest);
    }

    private String executeRegisterDeviceEndpointRequest(final String userDeviceToken, final String applicationArn) {
        final RegisterUserDeviceTokenRequest registerUserDeviceTokenRequest = new RegisterUserDeviceTokenRequest(userDeviceToken, applicationArn);
        final RegisterUserDeviceTokenResponse registerUserDeviceTokenResponse = amazonSnsApiCommunicator.registerUserDeviceToken(registerUserDeviceTokenRequest);
        return registerUserDeviceTokenResponse.getDeviceEndpointArn();
    }
}
