package com.sfl.nms.services.notification.impl.push.sns;

import com.amazonaws.AmazonServiceException;
import com.sfl.nms.services.device.model.mobile.DeviceOperatingSystemType;
import com.sfl.nms.externalclients.push.amazon.communicator.AmazonSnsApiCommunicator;
import com.sfl.nms.externalclients.push.amazon.model.request.GetDeviceEndpointAttributesRequest;
import com.sfl.nms.externalclients.push.amazon.model.request.RegisterUserDeviceTokenRequest;
import com.sfl.nms.externalclients.push.amazon.model.request.UpdateDeviceEndpointAttributesRequest;
import com.sfl.nms.externalclients.push.amazon.model.response.GetDeviceEndpointAttributesResponse;
import com.sfl.nms.externalclients.push.amazon.model.response.RegisterUserDeviceTokenResponse;
import com.sfl.nms.services.notification.dto.push.sns.PushNotificationSnsRecipientDto;
import com.sfl.nms.services.notification.model.push.PushNotificationRecipient;
import com.sfl.nms.services.notification.model.push.sns.PushNotificationSnsRecipient;
import com.sfl.nms.services.notification.push.sns.PushNotificationSnsRecipientService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * User: Ruben Dilanyan
 * Company: SFL LLC
 * Date: 8/18/15
 * Time: 10:10 AM
 */
@Component
public class PushNotificationUserDeviceTokenSnsProcessorImpl implements PushNotificationUserDeviceTokenSnsProcessor {

    private static final Logger LOGGER = LoggerFactory.getLogger(PushNotificationUserDeviceTokenSnsProcessorImpl.class);

    /* Dependencies */
    @Autowired
    private PushNotificationSnsRecipientService pushNotificationSnsRecipientService;

    @Autowired
    private AmazonSnsApiCommunicator amazonSnsApiCommunicator;

    @Autowired
    private SnsArnConfigurationService snsArnConfigurationService;

    /* Constructors */
    public PushNotificationUserDeviceTokenSnsProcessorImpl() {
        LOGGER.debug("Initializing push notification user device token SNS processor");
    }

    @Override
    public String registerUserDeviceToken(@Nonnull final String userDeviceToken, @Nonnull final DeviceOperatingSystemType operatingSystemType, @Nonnull final String applicationType, @Nullable final String currentProviderToken) {
        Assert.notNull(userDeviceToken, "User device token should not be null");
        Assert.notNull(operatingSystemType, "Operating system type should not be null");
        Assert.notNull(applicationType, "Push notification application type should not be null");
        LOGGER.debug("Registering user device token - {} , operating system type - {}", userDeviceToken, operatingSystemType);
        final String applicationArn = snsArnConfigurationService.getApplicationArnForMobilePlatform(operatingSystemType, applicationType);
        String deviceEndpointArn = currentProviderToken;
        // Check if there is any current token
        if (deviceEndpointArn == null) {
            deviceEndpointArn = executeRegisterDeviceEndpointRequest(userDeviceToken, applicationArn);
        }
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
            LOGGER.debug("Updating user device endpoint attributes since either token does not match or endpoint is disabled. Attributes - {}, device token - {}", getDeviceEndpointAttributesResponse, userDeviceToken);
            try {
                final UpdateDeviceEndpointAttributesRequest updateDeviceEndpointAttributesRequest = new UpdateDeviceEndpointAttributesRequest(deviceEndpointArn, userDeviceToken, true);
                amazonSnsApiCommunicator.updateDeviceEndpointAttributes(updateDeviceEndpointAttributesRequest);
            } catch (final AmazonServiceException ex) {
                LOGGER.error("Error occurred while trying to update device attributes for endpoint ARN - " + deviceEndpointArn, ex);
                // As fallback simply create new endpoint to get rid of old endpoint errors
                deviceEndpointArn = executeRegisterDeviceEndpointRequest(userDeviceToken, applicationArn);
            }
        }
        LOGGER.debug("Successfully registered user device token - {}, result endpoint ARN is - {}, platform - {}", userDeviceToken, deviceEndpointArn, operatingSystemType);
        return deviceEndpointArn;
    }

    @Override
    public PushNotificationRecipient createPushNotificationRecipient(@Nonnull final Long subscriptionId, @Nonnull final String recipientRouteToken, @Nonnull final DeviceOperatingSystemType operatingSystemType, @Nonnull final String applicationType) {
        Assert.notNull(subscriptionId, "Push notification subscription should not be null");
        Assert.notNull(recipientRouteToken, "Recipient route token should not be null");
        Assert.notNull(operatingSystemType, "Mobile device operating system type should not be null");
        Assert.notNull(applicationType, "Application type should not be null");
        LOGGER.debug("Creating push notification SNS recipient for subscription with id - {}, recipient route token - {}, operating system type - {}", subscriptionId, recipientRouteToken, operatingSystemType);
        // Create push notification SNS recipient DTO
        final String platformApplicationArn = snsArnConfigurationService.getApplicationArnForMobilePlatform(operatingSystemType, applicationType);
        final PushNotificationSnsRecipientDto snsRecipientDto = new PushNotificationSnsRecipientDto();
        snsRecipientDto.setPlatformApplicationArn(platformApplicationArn);
        snsRecipientDto.setDestinationRouteToken(recipientRouteToken);
        snsRecipientDto.setDeviceOperatingSystemType(operatingSystemType);
        snsRecipientDto.setApplicationType(applicationType);
        // Create recipient
        final PushNotificationSnsRecipient recipient = pushNotificationSnsRecipientService.createPushNotificationRecipient(subscriptionId, snsRecipientDto);
        LOGGER.debug("Successfully created push notification SNS recipient for subscription with id - {}, recipient route token - {}, operating system type - {}. Recipient - {}", subscriptionId, recipientRouteToken, operatingSystemType, recipient);
        return recipient;
    }

    /* Utility methods */
    private GetDeviceEndpointAttributesResponse executeGetEndpointAttributesRequest(final String deviceEndpointArn) {
        final GetDeviceEndpointAttributesRequest getDeviceEndpointAttributesRequest = new GetDeviceEndpointAttributesRequest(deviceEndpointArn);
        return amazonSnsApiCommunicator.getDeviceEndpointAttributes(getDeviceEndpointAttributesRequest);
    }

    private String executeRegisterDeviceEndpointRequest(final String userDeviceToken, final String applicationArn) {
        final RegisterUserDeviceTokenRequest registerUserDeviceTokenRequest = new RegisterUserDeviceTokenRequest(userDeviceToken, applicationArn);
        final RegisterUserDeviceTokenResponse registerUserDeviceTokenResponse = amazonSnsApiCommunicator.registerUserDeviceToken(registerUserDeviceTokenRequest);
        return registerUserDeviceTokenResponse.getDeviceEndpointArn();
    }

    /* Properties getters and setters */
    public PushNotificationSnsRecipientService getPushNotificationSnsRecipientService() {
        return pushNotificationSnsRecipientService;
    }

    public void setPushNotificationSnsRecipientService(final PushNotificationSnsRecipientService pushNotificationSnsRecipientService) {
        this.pushNotificationSnsRecipientService = pushNotificationSnsRecipientService;
    }

    public AmazonSnsApiCommunicator getAmazonSnsApiCommunicator() {
        return amazonSnsApiCommunicator;
    }

    public void setAmazonSnsApiCommunicator(final AmazonSnsApiCommunicator amazonSnsApiCommunicator) {
        this.amazonSnsApiCommunicator = amazonSnsApiCommunicator;
    }

    public SnsArnConfigurationService getSnsArnConfigurationService() {
        return snsArnConfigurationService;
    }

    public void setSnsArnConfigurationService(final SnsArnConfigurationService snsArnConfigurationService) {
        this.snsArnConfigurationService = snsArnConfigurationService;
    }
}
