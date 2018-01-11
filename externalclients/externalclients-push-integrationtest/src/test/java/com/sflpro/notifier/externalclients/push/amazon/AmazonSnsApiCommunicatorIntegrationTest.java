package com.sflpro.notifier.externalclients.push.amazon;

import com.sflpro.notifier.externalclients.push.amazon.communicator.AmazonSnsApiCommunicator;
import com.sflpro.notifier.externalclients.push.amazon.model.AmazonSNSPlatformType;
import com.sflpro.notifier.externalclients.push.amazon.model.request.GetDeviceEndpointAttributesRequest;
import com.sflpro.notifier.externalclients.push.amazon.model.request.RegisterUserDeviceTokenRequest;
import com.sflpro.notifier.externalclients.push.amazon.model.request.SendPushNotificationRequestMessageInformation;
import com.sflpro.notifier.externalclients.push.amazon.model.request.UpdateDeviceEndpointAttributesRequest;
import com.sflpro.notifier.externalclients.push.amazon.model.response.GetDeviceEndpointAttributesResponse;
import com.sflpro.notifier.externalclients.push.amazon.model.response.RegisterUserDeviceTokenResponse;
import com.sflpro.notifier.externalclients.push.amazon.model.response.SendPushNotificationResponse;
import com.sflpro.notifier.externalclients.push.test.AbstractPushNotificationIntegrationTest;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

/**
 * User: Ruben Dilanyan
 * Company: SFL LLC
 * Date: 3/15/15
 * Time: 11:03 AM
 */
@Ignore
public class AmazonSnsApiCommunicatorIntegrationTest extends AbstractPushNotificationIntegrationTest {

    /* Constants */
    private static final String MESSAGE_SUBJECT = "Happy message subject";

    private static final String MESSAGE_BODY = "Happy message bogy";

    private static final String HEX_CHARACTER_SET = "ABCDEF0123456789";

    private static final int IOS_TOKEN_LENGTH = 64;

    /* Dependencies */
    @Autowired
    private AmazonSnsApiCommunicator amazonSnsApiCommunicator;

    @Value("#{appProperties['amazon.account.sns.application.arn.ios']}")
    private String iosPlatformApplicationArn;

    @Value("#{appProperties['amazon.account.sns.application.arn.android']}")
    private String androidPlatformApplicationArn;

    /* Properties */

    /* Constructors */
    public AmazonSnsApiCommunicatorIntegrationTest() {
    }

    /* Test methods */
    @Test
    public void testSendPushNotification() {
        // Prepare data
        final String deviceEndpointArn = registerIOSDeviceEndpoint();
        final Map<String, String> messageAttributes = new HashMap<>();
        messageAttributes.put("testKey", "testValue");
        final SendPushNotificationRequestMessageInformation messageInformation = new SendPushNotificationRequestMessageInformation(MESSAGE_SUBJECT, MESSAGE_BODY, messageAttributes, AmazonSNSPlatformType.APNS);
        // Execute send request
        final SendPushNotificationResponse response = amazonSnsApiCommunicator.sendPushNotification(messageInformation, deviceEndpointArn);
        assertNotNull(response);
        assertNotNull(response.getMessageId());
    }

    @Test
    public void testRegisterUserDeviceToken() {
        // Prepare data
        final String platformApplicationArn = iosPlatformApplicationArn;
        final String userDeviceToken = generateIOSToken();
        // Build request
        final RegisterUserDeviceTokenRequest request = new RegisterUserDeviceTokenRequest(userDeviceToken, platformApplicationArn);
        // Execute request
        RegisterUserDeviceTokenResponse response = amazonSnsApiCommunicator.registerUserDeviceToken(request);
        assertNotNull(response);
        final String deviceEndpointArnOfFirstRequest = response.getDeviceEndpointArn();
        assertNotNull(deviceEndpointArnOfFirstRequest);
        // Register same user device token again
        response = amazonSnsApiCommunicator.registerUserDeviceToken(request);
        assertNotNull(response);
        assertEquals(deviceEndpointArnOfFirstRequest, response.getDeviceEndpointArn());
    }

    @Test
    public void testGetDeviceEndpointAttributes() {
        // Prepare data
        final String deviceToken = generateIOSToken();
        final String deviceEndpointArn = registerIOSDeviceEndpoint(deviceToken);
        // Get attributes for device endpoint
        GetDeviceEndpointAttributesRequest request = new GetDeviceEndpointAttributesRequest(deviceEndpointArn);
        GetDeviceEndpointAttributesResponse response = amazonSnsApiCommunicator.getDeviceEndpointAttributes(request);
        assertNotNull(response);
        assertEquals(deviceToken.toLowerCase(), response.getToken().toLowerCase());
        assertTrue(response.isEnabled());
        assertTrue(response.isExists());
        // execute another request with not existing endpoint ARN
        final String notExistingDeviceEndpointArn = deviceEndpointArn.substring(0, deviceEndpointArn.length() - 1) + RandomStringUtils.random(1, HEX_CHARACTER_SET).toLowerCase();
        request = new GetDeviceEndpointAttributesRequest(notExistingDeviceEndpointArn);
        response = amazonSnsApiCommunicator.getDeviceEndpointAttributes(request);
        assertNotNull(response);
        assertFalse(response.isExists());
    }

    @Test
    public void testUpdateDeviceEndpointAttributes() {
        // Prepare data
        final String deviceToken = generateIOSToken();
        final String deviceEndpointArn = registerIOSDeviceEndpoint(deviceToken);
        // Verify that endpoint is enabled and token is valid
        GetDeviceEndpointAttributesRequest getDeviceEndpointAttributesRequest = new GetDeviceEndpointAttributesRequest(deviceEndpointArn);
        GetDeviceEndpointAttributesResponse getDeviceEndpointAttributesResponse = amazonSnsApiCommunicator.getDeviceEndpointAttributes(getDeviceEndpointAttributesRequest);
        assertNotNull(getDeviceEndpointAttributesResponse);
        assertEquals(deviceToken.toLowerCase(), getDeviceEndpointAttributesResponse.getToken().toLowerCase());
        assertTrue(getDeviceEndpointAttributesResponse.isEnabled());
        // Update token attributes
        final String newDeviceToken = generateIOSToken();
        final UpdateDeviceEndpointAttributesRequest updateDeviceEndpointAttributesRequest = new UpdateDeviceEndpointAttributesRequest(deviceEndpointArn, newDeviceToken, false);
        amazonSnsApiCommunicator.updateDeviceEndpointAttributes(updateDeviceEndpointAttributesRequest);
        // Sleep after update since Amazon processes update request asynchronously
        sleepForMillis(1000);
        // Load attributes again and assert
        getDeviceEndpointAttributesRequest = new GetDeviceEndpointAttributesRequest(deviceEndpointArn);
        getDeviceEndpointAttributesResponse = amazonSnsApiCommunicator.getDeviceEndpointAttributes(getDeviceEndpointAttributesRequest);
        assertNotNull(getDeviceEndpointAttributesResponse);
        assertEquals(newDeviceToken.toLowerCase(), getDeviceEndpointAttributesResponse.getToken().toLowerCase());
        assertFalse(getDeviceEndpointAttributesResponse.isEnabled());
    }

    /* Utility methods */
    private String generateIOSToken() {
        return RandomStringUtils.random(IOS_TOKEN_LENGTH, HEX_CHARACTER_SET);
    }

    private String registerIOSDeviceEndpoint() {
        return registerIOSDeviceEndpoint(generateIOSToken());
    }

    private String registerIOSDeviceEndpoint(final String iosDeviceToken) {
        // Prepare data
        final String platformApplicationArn = iosPlatformApplicationArn;
        final String userDeviceToken = iosDeviceToken;
        // Build request
        final RegisterUserDeviceTokenRequest request = new RegisterUserDeviceTokenRequest(userDeviceToken, platformApplicationArn);
        // Execute request
        RegisterUserDeviceTokenResponse response = amazonSnsApiCommunicator.registerUserDeviceToken(request);
        return response.getDeviceEndpointArn();
    }

    private void sleepForMillis(final int millis) {
        try {
            Thread.sleep(millis);
        } catch (final InterruptedException ex) {
            // Swallow
        }
    }
}
