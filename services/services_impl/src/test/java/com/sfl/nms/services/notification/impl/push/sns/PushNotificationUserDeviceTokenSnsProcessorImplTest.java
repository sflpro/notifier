package com.sfl.nms.services.notification.impl.push.sns;

import com.amazonaws.AmazonServiceException;
import com.sfl.nms.services.device.model.mobile.DeviceOperatingSystemType;
import com.sfl.nms.services.notification.dto.push.sns.PushNotificationSnsRecipientDto;
import com.sfl.nms.services.notification.model.push.PushNotificationRecipient;
import com.sfl.nms.services.notification.model.push.sns.PushNotificationSnsRecipient;
import com.sfl.nms.services.notification.push.sns.PushNotificationSnsRecipientService;
import com.sfl.nms.services.test.AbstractServicesUnitTest;
import com.sfl.nms.externalclients.push.amazon.communicator.AmazonSnsApiCommunicator;
import com.sfl.nms.externalclients.push.amazon.model.request.GetDeviceEndpointAttributesRequest;
import com.sfl.nms.externalclients.push.amazon.model.request.RegisterUserDeviceTokenRequest;
import com.sfl.nms.externalclients.push.amazon.model.request.UpdateDeviceEndpointAttributesRequest;
import com.sfl.nms.externalclients.push.amazon.model.response.GetDeviceEndpointAttributesResponse;
import com.sfl.nms.externalclients.push.amazon.model.response.RegisterUserDeviceTokenResponse;
import static org.easymock.EasyMock.*;
import org.easymock.Mock;
import org.easymock.TestSubject;
import static org.junit.Assert.*;
import org.junit.Test;

/**
 * User: Ruben Dilanyan
 * Company: SFL LLC
 * Date: 8/18/15
 * Time: 10:12 AM
 */
public class PushNotificationUserDeviceTokenSnsProcessorImplTest extends AbstractServicesUnitTest {

    /* Constants */
    private static final String APPLICATION_TYPE = "customer";

    private static final String APPLICATION_ARN = "apparn";

    /* Test subject and mocks */
    @TestSubject
    private PushNotificationUserDeviceTokenSnsProcessorImpl pushNotificationUserDeviceTokenSnsProcessor = new PushNotificationUserDeviceTokenSnsProcessorImpl();

    @Mock
    private AmazonSnsApiCommunicator amazonSnsApiCommunicator;

    @Mock
    private PushNotificationSnsRecipientService pushNotificationSnsRecipientService;

    @Mock
    private SnsArnConfigurationService snsArnConfigurationService;

    /* Constructors */
    public PushNotificationUserDeviceTokenSnsProcessorImplTest() {
    }

    /* Test methods */
    @Test
    public void testCreatePushNotificationRecipientWithInvalidArguments() {
        // Test data
        final Long subscriptionId = 1L;
        final String recipientRouteToken = "KHHGFR&*^^FTD$&DRTX&CGK";
        final DeviceOperatingSystemType operatingSystemType = DeviceOperatingSystemType.IOS;
        final String applicationType = APPLICATION_TYPE;
        // Reset
        resetAll();
        // Replay
        replayAll();
        // Run test scenario
        try {
            pushNotificationUserDeviceTokenSnsProcessor.createPushNotificationRecipient(null, recipientRouteToken, operatingSystemType, applicationType);
            fail("Exception should be thrown");
        } catch (final IllegalArgumentException ex) {
            // Expected
        }
        try {
            pushNotificationUserDeviceTokenSnsProcessor.createPushNotificationRecipient(subscriptionId, null, operatingSystemType, applicationType);
            fail("Exception should be thrown");
        } catch (final IllegalArgumentException ex) {
            // Expected
        }
        try {
            pushNotificationUserDeviceTokenSnsProcessor.createPushNotificationRecipient(subscriptionId, recipientRouteToken, null, applicationType);
            fail("Exception should be thrown");
        } catch (final IllegalArgumentException ex) {
            // Expected
        }
        try {
            pushNotificationUserDeviceTokenSnsProcessor.createPushNotificationRecipient(subscriptionId, recipientRouteToken, operatingSystemType, null);
            fail("Exception should be thrown");
        } catch (final IllegalArgumentException ex) {
            // Expected
        }
        // Verify
        verifyAll();
    }

    @Test
    public void testCreatePushNotificationRecipient() {
        testCreatePushNotificationRecipient(DeviceOperatingSystemType.IOS, APPLICATION_TYPE, APPLICATION_ARN);
    }

    @Test
    public void testRegisterUserDeviceTokenWithInvalidArguments() {
        // Test data
        final String userDeviceToken = "JHHFTDRUDRDFGO&*RRDTFCKK";
        final DeviceOperatingSystemType operatingSystemType = DeviceOperatingSystemType.ANDROID;
        final String applicationType = APPLICATION_TYPE;
        final String currentProviderToken = "UY^RE%*^DGCURD^OGUCTDRY";
        // Reset
        resetAll();
        // Replay
        replayAll();
        // Run test scenario
        try {
            pushNotificationUserDeviceTokenSnsProcessor.registerUserDeviceToken(null, operatingSystemType, applicationType, currentProviderToken);
            fail("Exception should be thrown");
        } catch (final IllegalArgumentException ex) {
            // Expected
        }
        try {
            pushNotificationUserDeviceTokenSnsProcessor.registerUserDeviceToken(userDeviceToken, null, applicationType, currentProviderToken);
            fail("Exception should be thrown");
        } catch (final IllegalArgumentException ex) {
            // Expected
        }
        try {
            pushNotificationUserDeviceTokenSnsProcessor.registerUserDeviceToken(userDeviceToken, operatingSystemType, null, currentProviderToken);
            fail("Exception should be thrown");
        } catch (final IllegalArgumentException ex) {
            // Expected
        }
        // Verify
        verifyAll();
    }

    @Test
    public void testRegisterUserDeviceTokenWhenOldTokenIsNotFoundAndNoAttributesUpdateIsRequiredForIOS() {
        testRegisterUserDeviceTokenWhenOldTokenIsNotFoundAndNoAttributesUpdateIsRequired(DeviceOperatingSystemType.IOS, APPLICATION_ARN);
    }

    @Test
    public void testRegisterUserDeviceTokenWhenOldTokenIsNotFoundAndNoAttributesUpdateIsRequiredForAndroid() {
        testRegisterUserDeviceTokenWhenOldTokenIsNotFoundAndNoAttributesUpdateIsRequired(DeviceOperatingSystemType.ANDROID, APPLICATION_ARN);
    }

    @Test
    public void testRegisterUserDeviceTokenWhenAttributesUpdateIsRequiredBecauseOfDifferentToken() {
        // Test data
        final String userDeviceToken = "JHHFTDRUDRDFGO&*RRDTFCKK";
        final DeviceOperatingSystemType operatingSystemType = DeviceOperatingSystemType.IOS;
        final String applicationType = APPLICATION_TYPE;
        final String userDeviceEndpointArn = "*&VTD%F^^f06f9crdf9ctccc";
        final String applicationArn = "JBVITCTCITRCJCKCKG";
        final String currentProviderToken = null;
        // Reset
        resetAll();
        // Expectations
        expect(snsArnConfigurationService.getApplicationArnForMobilePlatform(eq(operatingSystemType), eq(applicationType))).andReturn(applicationArn).once();
        expect(amazonSnsApiCommunicator.registerUserDeviceToken(eq(new RegisterUserDeviceTokenRequest(userDeviceToken, applicationArn)))).andReturn(new RegisterUserDeviceTokenResponse(userDeviceToken, applicationArn, userDeviceEndpointArn)).once();
        expect(amazonSnsApiCommunicator.getDeviceEndpointAttributes(eq(new GetDeviceEndpointAttributesRequest(userDeviceEndpointArn)))).andReturn(new GetDeviceEndpointAttributesResponse(userDeviceEndpointArn, userDeviceToken + "_different", true, true)).once();
        amazonSnsApiCommunicator.updateDeviceEndpointAttributes(eq(new UpdateDeviceEndpointAttributesRequest(userDeviceEndpointArn, userDeviceToken, true)));
        expectLastCall().once();
        // Replay
        replayAll();
        // Run test scenario
        final String result = pushNotificationUserDeviceTokenSnsProcessor.registerUserDeviceToken(userDeviceToken, operatingSystemType, applicationType, currentProviderToken);
        assertEquals(userDeviceEndpointArn, result);
        // Verify
        verifyAll();
    }

    @Test
    public void testRegisterUserDeviceTokenWhenAttributesUpdateIsRequiredBecauseOfDisabledEndpoint() {
        // Test data
        final String userDeviceToken = "JHHFTDRUDRDFGO&*RRDTFCKK";
        final DeviceOperatingSystemType operatingSystemType = DeviceOperatingSystemType.IOS;
        final String applicationType = APPLICATION_TYPE;
        final String userDeviceEndpointArn = "*&VTD%F^^f06f9crdf9ctccc";
        final String currentProviderToken = null;
        final String applicationArn = "JNBHGYUVITYITTI";
        // Reset
        resetAll();
        // Expectations
        expect(snsArnConfigurationService.getApplicationArnForMobilePlatform(eq(operatingSystemType), eq(applicationType))).andReturn(applicationArn).once();
        expect(amazonSnsApiCommunicator.registerUserDeviceToken(eq(new RegisterUserDeviceTokenRequest(userDeviceToken, applicationArn)))).andReturn(new RegisterUserDeviceTokenResponse(userDeviceToken, applicationArn, userDeviceEndpointArn)).once();
        expect(amazonSnsApiCommunicator.getDeviceEndpointAttributes(eq(new GetDeviceEndpointAttributesRequest(userDeviceEndpointArn)))).andReturn(new GetDeviceEndpointAttributesResponse(userDeviceEndpointArn, userDeviceToken, false, true)).once();
        amazonSnsApiCommunicator.updateDeviceEndpointAttributes(eq(new UpdateDeviceEndpointAttributesRequest(userDeviceEndpointArn, userDeviceToken, true)));
        expectLastCall().once();
        // Replay
        replayAll();
        // Run test scenario
        final String result = pushNotificationUserDeviceTokenSnsProcessor.registerUserDeviceToken(userDeviceToken, operatingSystemType, applicationType, currentProviderToken);
        assertEquals(userDeviceEndpointArn, result);
        // Verify
        verifyAll();
    }

    @Test
    public void testRegisterUserDeviceTokenWhenOldTokenIsFoundAndNoAttributesUpdateIsRequired() {
        // Test data
        final DeviceOperatingSystemType operatingSystemType = DeviceOperatingSystemType.IOS;
        final String applicationType = APPLICATION_TYPE;
        final String userDeviceToken = "JHHFTDRUDRDFGO&*RRDTFCKK";
        final String currentProviderToken = "&^^%^$%FXDDGJRURXEYXE";
        final String applicationArn = "JHHOVYCITCU";
        // Reset
        resetAll();
        // Expectations
        expect(amazonSnsApiCommunicator.getDeviceEndpointAttributes(eq(new GetDeviceEndpointAttributesRequest(currentProviderToken)))).andReturn(new GetDeviceEndpointAttributesResponse(currentProviderToken, userDeviceToken, true, true)).once();
        expect(snsArnConfigurationService.getApplicationArnForMobilePlatform(eq(operatingSystemType), eq(applicationType))).andReturn(applicationArn).once();
        // Replay
        replayAll();
        // Run test scenario
        final String result = pushNotificationUserDeviceTokenSnsProcessor.registerUserDeviceToken(userDeviceToken, operatingSystemType, applicationType, currentProviderToken);
        assertEquals(currentProviderToken, result);
        // Verify
        verifyAll();
    }

    @Test
    public void testRegisterUserDeviceTokenWhenOldTokenIsFoundAndAttributesUpdateIsRequired() {
        // Test data
        final DeviceOperatingSystemType operatingSystemType = DeviceOperatingSystemType.IOS;
        final String applicationType = APPLICATION_TYPE;
        final String userDeviceToken = "JHHFTDRUDRDFGO&*RRDTFCKK";
        final String currentProviderToken = "&^^%^$%FXDDGJRURXEYXE";
        final String applicationArn = "JUYFITYRRUTXDYXRCOCO";
        // Reset
        resetAll();
        // Expectations
        expect(amazonSnsApiCommunicator.getDeviceEndpointAttributes(eq(new GetDeviceEndpointAttributesRequest(currentProviderToken)))).andReturn(new GetDeviceEndpointAttributesResponse(currentProviderToken, userDeviceToken, false, true)).once();
        amazonSnsApiCommunicator.updateDeviceEndpointAttributes(eq(new UpdateDeviceEndpointAttributesRequest(currentProviderToken, userDeviceToken, true)));
        expectLastCall().once();
        expect(snsArnConfigurationService.getApplicationArnForMobilePlatform(eq(operatingSystemType), eq(applicationType))).andReturn(applicationArn).once();
        // Replay
        replayAll();
        // Run test scenario
        final String result = pushNotificationUserDeviceTokenSnsProcessor.registerUserDeviceToken(userDeviceToken, operatingSystemType, applicationType, currentProviderToken);
        assertEquals(currentProviderToken, result);
        // Verify
        verifyAll();
    }

    @Test
    public void testRegisterUserDeviceTokenWhenOldTokenIsFoundAndExceptionIsThrownDuringAttributesUpdate() {
        // Test data
        final DeviceOperatingSystemType operatingSystemType = DeviceOperatingSystemType.IOS;
        final String applicationType = APPLICATION_TYPE;
        final String userDeviceToken = "JHHFTDRUDRDFGO&*RRDTFCKK";
        final String createdUserDeviceEndpointArn = "*&VTD%F^^f06f9crdf9ctccc";
        final String currentProviderToken = "&^^%^$%FXDDGJRURXEYXE";
        final String applicationArn = "JNLHVGVIGGI";
        // Reset
        resetAll();
        // Expectations
        expect(amazonSnsApiCommunicator.getDeviceEndpointAttributes(eq(new GetDeviceEndpointAttributesRequest(currentProviderToken)))).andReturn(new GetDeviceEndpointAttributesResponse(currentProviderToken, userDeviceToken, false, true)).once();
        amazonSnsApiCommunicator.updateDeviceEndpointAttributes(eq(new UpdateDeviceEndpointAttributesRequest(currentProviderToken, userDeviceToken, true)));
        expectLastCall().andThrow(new AmazonServiceException("Testing exception handling")).once();
        expect(amazonSnsApiCommunicator.registerUserDeviceToken(eq(new RegisterUserDeviceTokenRequest(userDeviceToken, applicationArn)))).andReturn(new RegisterUserDeviceTokenResponse(userDeviceToken, applicationArn, createdUserDeviceEndpointArn)).once();
        expect(snsArnConfigurationService.getApplicationArnForMobilePlatform(eq(operatingSystemType), eq(applicationType))).andReturn(applicationArn).once();
        // Replay
        replayAll();
        // Run test scenario
        final String result = pushNotificationUserDeviceTokenSnsProcessor.registerUserDeviceToken(userDeviceToken, operatingSystemType, applicationType, currentProviderToken);
        assertEquals(createdUserDeviceEndpointArn, result);
        // Verify
        verifyAll();
    }

    /* Utility methods */
    private void testCreatePushNotificationRecipient(final DeviceOperatingSystemType operatingSystemType, final String applicationType, final String applicationArn) {
        // Test data
        final Long subscriptionId = 1L;
        final String recipientRouteToken = "KHHGFR&*^^FTD$&DRTX&CGK";
        final Long recipientId = 2L;
        final PushNotificationSnsRecipient recipient = getServicesImplTestHelper().createPushNotificationSnsRecipient();
        recipient.setId(recipientId);
        final PushNotificationSnsRecipientDto snsRecipientDto = new PushNotificationSnsRecipientDto();
        snsRecipientDto.setPlatformApplicationArn(applicationArn);
        snsRecipientDto.setDestinationRouteToken(recipientRouteToken);
        snsRecipientDto.setDeviceOperatingSystemType(operatingSystemType);
        snsRecipientDto.setApplicationType(applicationType);
        // Reset
        resetAll();
        // Expectations
        expect(pushNotificationSnsRecipientService.createPushNotificationRecipient(eq(subscriptionId), eq(snsRecipientDto))).andReturn(recipient).once();
        expect(snsArnConfigurationService.getApplicationArnForMobilePlatform(eq(operatingSystemType), eq(applicationType))).andReturn(applicationArn).once();
        // Replay
        replayAll();
        // Run test scenario
        final PushNotificationRecipient result = pushNotificationUserDeviceTokenSnsProcessor.createPushNotificationRecipient(subscriptionId, recipientRouteToken, operatingSystemType, applicationType);
        assertEquals(recipient, result);
        // Verify
        verifyAll();
    }


    private void testRegisterUserDeviceTokenWhenOldTokenIsNotFoundAndNoAttributesUpdateIsRequired(final DeviceOperatingSystemType operatingSystemType, final String applicationArn) {
        // Test data
        final String userDeviceToken = "JHHFTDRUDRDFGO&*RRDTFCKK";
        final String createdUserDeviceEndpointArn = "*&VTD%F^^f06f9crdf9ctccc";
        final String currentProviderToken = "&^^%^$%FXDDGJRURXEYXE";
        final String applicationType = "customer";
        // Reset
        resetAll();
        // Expectations
        expect(amazonSnsApiCommunicator.getDeviceEndpointAttributes(eq(new GetDeviceEndpointAttributesRequest(currentProviderToken)))).andReturn(new GetDeviceEndpointAttributesResponse(currentProviderToken, userDeviceToken, true, false)).once();
        expect(amazonSnsApiCommunicator.registerUserDeviceToken(eq(new RegisterUserDeviceTokenRequest(userDeviceToken, applicationArn)))).andReturn(new RegisterUserDeviceTokenResponse(userDeviceToken, applicationArn, createdUserDeviceEndpointArn)).once();
        expect(amazonSnsApiCommunicator.getDeviceEndpointAttributes(eq(new GetDeviceEndpointAttributesRequest(createdUserDeviceEndpointArn)))).andReturn(new GetDeviceEndpointAttributesResponse(createdUserDeviceEndpointArn, userDeviceToken, true, true)).once();
        expect(snsArnConfigurationService.getApplicationArnForMobilePlatform(eq(operatingSystemType), eq(applicationType))).andReturn(applicationArn).once();
        // Replay
        replayAll();
        // Run test scenario
        final String result = pushNotificationUserDeviceTokenSnsProcessor.registerUserDeviceToken(userDeviceToken, operatingSystemType, applicationType, currentProviderToken);
        assertEquals(createdUserDeviceEndpointArn, result);
        // Verify
        verifyAll();
    }

}
