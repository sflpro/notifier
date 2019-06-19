package com.sflpro.notifier.externalclients.push.amazon.communicator;

import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.sns.AmazonSNSClient;
import com.amazonaws.services.sns.model.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sflpro.notifier.externalclients.push.amazon.exception.AmazonSnsClientRuntimeException;
import com.sflpro.notifier.externalclients.push.amazon.model.AmazonSNSPlatformType;
import com.sflpro.notifier.externalclients.push.amazon.model.request.GetDeviceEndpointAttributesRequest;
import com.sflpro.notifier.externalclients.push.amazon.model.request.RegisterUserDeviceTokenRequest;
import com.sflpro.notifier.externalclients.push.amazon.model.request.SendPushNotificationRequestMessageInformation;
import com.sflpro.notifier.externalclients.push.amazon.model.request.UpdateDeviceEndpointAttributesRequest;
import com.sflpro.notifier.externalclients.push.amazon.model.response.GetDeviceEndpointAttributesResponse;
import com.sflpro.notifier.externalclients.push.amazon.model.response.RegisterUserDeviceTokenResponse;
import com.sflpro.notifier.externalclients.push.amazon.model.response.SendPushNotificationResponse;
import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * User: Ruben Dilanyan
 * Company: SFL LLC
 * Date: 4/9/15
 * Time: 3:26 PM
 */
@Component
public class AmazonSnsApiCommunicatorImpl implements AmazonSnsApiCommunicator, InitializingBean {

    private static final Logger LOGGER = LoggerFactory.getLogger(AmazonSnsApiCommunicatorImpl.class);

    /* Constants */
    private static final String ENDPOINT_EXISTS_EXCEPTION_PATTERN = ".*Endpoint (arn:aws:sns[^ ]+) already exists with the same Token.*";

    private static final String ENDPOINT_ATTRIBUTE_NAME_TOKEN = "Token";

    private static final String ENDPOINT_ATTRIBUTE_NAME_ENABLED = "Enabled";

    private static final String MESSAGE_STRUCTURE_JSON = "json";

    /* Properties */
    @Value("${amazon.account.sns.accesskey}")
    private String accessKey;

    @Value("${amazon.account.sns.secretkey}")
    private String secretKey;

    @Value("${amazon.account.sns.region}")
    private String awsRegion;

    private AmazonSNSClient amazonSNSClient;

    /* Constructors */
    public AmazonSnsApiCommunicatorImpl() {
        LOGGER.debug("Initializing Amazon SNS API communicator");
    }

    @Override
    public void afterPropertiesSet() {
        /* Create instance of twillio rest client */
        LOGGER.debug("Creating Amazon SNS client client instance with access key - {} and secret key - {}, AWS region - {}", accessKey, secretKey, awsRegion);
        final BasicAWSCredentials basicAWSCredentials = new BasicAWSCredentials(accessKey, secretKey);
        this.amazonSNSClient = new AmazonSNSClient(basicAWSCredentials);
        this.amazonSNSClient.setRegion(Region.getRegion(Regions.valueOf(awsRegion)));
    }

    /* Public methods */
    @Override
    @Nonnull
    public SendPushNotificationResponse sendPushNotification(@Nonnull final SendPushNotificationRequestMessageInformation messageInformation, @Nonnull final String deviceEndpointArn) {
        Assert.notNull(messageInformation, "Push notification message information should not be null");
        Assert.notNull(messageInformation.getMessageBody(), "Message body in push notification message information should not be null");
        Assert.notNull(messageInformation.getAmazonSNSPlatformType(), "Platform type in push notification message information should not be null");
        Assert.notNull(deviceEndpointArn, "Device endpoint ARN should not be null");
        LOGGER.debug("Sending push notification to with information - {}, device endpoint ARN - {}", messageInformation, deviceEndpointArn);
        // Generate notification body
        final String notificationContent = generateNotificationContent(messageInformation.getMessageBody(), messageInformation.getMessageProperties(), messageInformation.getAmazonSNSPlatformType());
        // Create publish request
        final PublishRequest publishRequest = new PublishRequest();
        publishRequest.setMessage(notificationContent);
        publishRequest.setSubject(messageInformation.getMessageSubject());
        publishRequest.setTargetArn(deviceEndpointArn);
        publishRequest.setMessageStructure(MESSAGE_STRUCTURE_JSON);
        // Publish message
        try {
            final PublishResult publishResult = amazonSNSClient.publish(publishRequest);
            final SendPushNotificationResponse response = new SendPushNotificationResponse(publishResult.getMessageId());
            LOGGER.debug("Got following result - {} when publishing push notification - {} to ARN - {}, returning result - {}", publishRequest, messageInformation, deviceEndpointArn, response);
            return response;
        } catch (final Exception ex) {
            final String message = "Error occurred while sending push notification with information - " + messageInformation + ", ARN - " + deviceEndpointArn;
            LOGGER.error(message, ex);
            throw new AmazonSnsClientRuntimeException(message, ex);
        }
    }

    @Nonnull
    @Override
    public RegisterUserDeviceTokenResponse registerUserDeviceToken(@Nonnull final RegisterUserDeviceTokenRequest request) {
        Assert.notNull(request, "Register user device token request should not be null");
        Assert.notNull(request.getPlatformApplicationArn(), "Platform application ARN in register user device token request should not be null");
        Assert.notNull(request.getUserDeviceToken(), "user device token in register user device token request should not be null");
        LOGGER.debug("Registering user device token with SNS service, request parameters - {}", request);
        String deviceEndpointArn;
        try {
            // Create request object
            final CreatePlatformEndpointRequest createEndpointRequest = new CreatePlatformEndpointRequest();
            createEndpointRequest.withPlatformApplicationArn(request.getPlatformApplicationArn());
            createEndpointRequest.withToken(request.getUserDeviceToken());
            final CreatePlatformEndpointResult createPlatformEndpointResult = amazonSNSClient.createPlatformEndpoint(createEndpointRequest);
            // Grab parameters
            deviceEndpointArn = createPlatformEndpointResult.getEndpointArn();
        } catch (InvalidParameterException ex) {
            // Check if message contains information about existing user device token
            final String message = ex.getErrorMessage();
            final Pattern p = Pattern.compile(ENDPOINT_EXISTS_EXCEPTION_PATTERN);
            final Matcher m = p.matcher(message);
            if (m.matches()) {
                // Extract existing endpoint from error message
                deviceEndpointArn = m.group(1);
            } else {
                final String errorMessage = "Error occurred while creating platform endpoint for parameters - " + request;
                LOGGER.debug(errorMessage, ex);
                throw new AmazonSnsClientRuntimeException(errorMessage, ex);
            }
        }
        // Create response object
        final RegisterUserDeviceTokenResponse response = new RegisterUserDeviceTokenResponse(request.getUserDeviceToken(), request.getPlatformApplicationArn(), deviceEndpointArn);
        LOGGER.debug("User device token registration result is - {}, parameters - {}", response, request);
        return response;
    }

    @Nonnull
    @Override
    public GetDeviceEndpointAttributesResponse getDeviceEndpointAttributes(@Nonnull final GetDeviceEndpointAttributesRequest request) {
        Assert.notNull(request, "Get device endpoint attributes request should not be null");
        Assert.notNull(request.getDeviceEndpointArn(), "Device endpoint ARN in get device endpoint attributes request should not be null");
        LOGGER.debug("Getting device endpoint attributes for request - {}", request);
        // Extract required fields
        String token = null;
        boolean enabled = false;
        boolean exists;
        try {
            // Prepare and execute request
            final GetEndpointAttributesRequest getEndpointAttributesRequest = new GetEndpointAttributesRequest().withEndpointArn(request.getDeviceEndpointArn());
            final GetEndpointAttributesResult getEndpointAttributesResult = amazonSNSClient.getEndpointAttributes(getEndpointAttributesRequest);
            // Extract required fields
            token = getEndpointAttributesResult.getAttributes().get(ENDPOINT_ATTRIBUTE_NAME_TOKEN);
            enabled = getEndpointAttributesResult.getAttributes().get(ENDPOINT_ATTRIBUTE_NAME_ENABLED).equalsIgnoreCase(String.valueOf(true));
            exists = true;
        } catch (final NotFoundException ex) {
            LOGGER.error("Error occurred while trying to retrieve attributes for device endpoint - " + request.getDeviceEndpointArn(), ex);
            exists = false;
        }
        // Build result
        final GetDeviceEndpointAttributesResponse response = new GetDeviceEndpointAttributesResponse(request.getDeviceEndpointArn(), token, enabled, exists);
        LOGGER.debug("Successfully retrieved response - {} for endpoint attributes request - {}", response, request);
        return response;
    }

    @Override
    public void updateDeviceEndpointAttributes(@Nonnull final UpdateDeviceEndpointAttributesRequest request) {
        Assert.notNull(request, "Update device endpoint attributes request should not be null");
        Assert.notNull(request.getDeviceEndpointArn(), "Device endpoint ARN in update device endpoint attributes request should not be null");
        Assert.notNull(request.getToken(), "Token in update device endpoint attributes request should not be null");
        LOGGER.debug("Updating device endpoint attributes for request - {}", request);
        // Build attributes map
        final Map<String, String> attributes = new HashMap<>();
        attributes.put(ENDPOINT_ATTRIBUTE_NAME_TOKEN, request.getToken());
        attributes.put(ENDPOINT_ATTRIBUTE_NAME_ENABLED, String.valueOf(request.isEnabled()));
        final SetEndpointAttributesRequest setEndpointAttributesRequest = new SetEndpointAttributesRequest();
        setEndpointAttributesRequest.withEndpointArn(request.getDeviceEndpointArn());
        setEndpointAttributesRequest.withAttributes(attributes);
        // Execute call
        amazonSNSClient.setEndpointAttributes(setEndpointAttributesRequest);
        LOGGER.debug("Successfully update device endpoint attributes for request - {}", request);
    }

    /* Utility methods */
    private String generateNotificationContent(final String message, final Map<String, String> propertiesMap, final AmazonSNSPlatformType amazonSNSPlatformType) {
        final AbstractMessageBuilder messageBuilder;
        switch (amazonSNSPlatformType) {
            case APNS: {
                messageBuilder = new APNSMessageBuilder(propertiesMap, message, amazonSNSPlatformType);
                break;
            }
            case APNS_SANDBOX: {
                messageBuilder = new APNSMessageBuilder(propertiesMap, message, amazonSNSPlatformType);
                break;
            }
            case GCM: {
                messageBuilder = new GCMMessageBuilder(propertiesMap, message, amazonSNSPlatformType);
                break;
            }
            default: {
                final String errorMessage = "Unknown Amazon SNS platform type - " + amazonSNSPlatformType;
                LOGGER.error(errorMessage);
                throw new AmazonSnsClientRuntimeException(errorMessage);
            }
        }
        // Generate message and return
        final String notificationContent = messageBuilder.generateJsonContent();
        LOGGER.debug("Successfully generated notification body for properties map - {}, platform type - {}, message - {}, generated body - {}", propertiesMap, amazonSNSPlatformType, message, notificationContent);
        return notificationContent;
    }


    /* Getters and setters */
    public String getAccessKey() {
        return accessKey;
    }

    public void setAccessKey(final String accessKey) {
        this.accessKey = accessKey;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(final String secretKey) {
        this.secretKey = secretKey;
    }

    public String getAwsRegion() {
        return awsRegion;
    }

    public void setAwsRegion(String awsRegion) {
        this.awsRegion = awsRegion;
    }

    public AmazonSNSClient getAmazonSNSClient() {
        return amazonSNSClient;
    }

    public void setAmazonSNSClient(final AmazonSNSClient amazonSNSClient) {
        this.amazonSNSClient = amazonSNSClient;
    }

    /* Inner classes */
    private abstract static class AbstractMessageBuilder {

        /* Properties */
        private final Map<String, String> attributesMap;

        private final String message;

        private final AmazonSNSPlatformType amazonSNSPlatformType;

        /* Constructors */
        public AbstractMessageBuilder(final Map<String, String> attributesMap, final String message, final AmazonSNSPlatformType amazonSNSPlatformType) {
            this.attributesMap = attributesMap;
            this.message = message;
            this.amazonSNSPlatformType = amazonSNSPlatformType;
        }

        /* Properties getters and setters */
        public Map<String, String> getAttributesMap() {
            return attributesMap;
        }

        public String getMessage() {
            return message;
        }

        public AmazonSNSPlatformType getAmazonSNSPlatformType() {
            return amazonSNSPlatformType;
        }

        protected String convertMapToJson(final Map<String, Object> propertiesMap) {
            final String content;
            try {
                final ObjectMapper objectMapper = new ObjectMapper();
                content = objectMapper.writeValueAsString(propertiesMap);
            } catch (final JsonProcessingException ex) {
                final String errorMessage = "Error occurred while generating JSON body for properties - " + propertiesMap;
                LOGGER.error(errorMessage, ex);
                throw new AmazonSnsClientRuntimeException(errorMessage, ex);
            }
            return content;
        }

        /* Abstract methods */
        public abstract String generateJsonContent();
    }

    private static class APNSMessageBuilder extends AbstractMessageBuilder {

        /* Constants */
        private static final String BADGE_PROPERTY = "badge";

        /* Constructors */
        public APNSMessageBuilder(final Map<String, String> attributesMap, final String message, final AmazonSNSPlatformType amazonSNSPlatformType) {
            super(attributesMap, message, amazonSNSPlatformType);
        }

        @Override
        public String generateJsonContent() {
            // Properties map
            final Map<String, Object> dataProperties = new HashMap<>();
            addCustomAttributes(dataProperties);
            dataProperties.put("alert", getMessage());
            // Create GCM properties
            final Map<String, Object> apsProperties = new HashMap<>();
            apsProperties.put("aps", dataProperties);
            // Create all properties
            final Map<String, Object> allProperties = new HashMap<>();
            allProperties.put(getAmazonSNSPlatformType().getJsonKey(), convertMapToJson(apsProperties));
            return convertMapToJson(allProperties);
        }


        private void addCustomAttributes(final Map<String, Object> dataProperties) {
            getAttributesMap().forEach((key, value) -> {
                if (BADGE_PROPERTY.equals(key) && NumberUtils.isNumber(value)) {
                    dataProperties.put(key, Integer.valueOf(value));
                } else {
                    dataProperties.put(key, value);
                }
            });
        }
    }

    private static class GCMMessageBuilder extends AbstractMessageBuilder {

        public GCMMessageBuilder(final Map<String, String> attributesMap, final String message, final AmazonSNSPlatformType amazonSNSPlatformType) {
            super(attributesMap, message, amazonSNSPlatformType);
        }

        @Override
        public String generateJsonContent() {
            // Properties map
            final Map<String, String> dataProperties = new HashMap<>();
            dataProperties.putAll(getAttributesMap());
            dataProperties.put("message", getMessage());
            // Create GCM properties
            final Map<String, Object> gcmProperties = new HashMap<>();
            gcmProperties.put("data", dataProperties);
            // Create all properties
            final Map<String, Object> allProperties = new HashMap<>();
            allProperties.put(getAmazonSNSPlatformType().getJsonKey(), convertMapToJson(gcmProperties));
            return convertMapToJson(allProperties);
        }
    }
}
