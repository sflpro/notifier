package com.sfl.nms.services.notification.impl.push.sns;

import com.sfl.nms.services.common.exception.ServicesRuntimeException;
import com.sfl.nms.services.device.model.mobile.DeviceOperatingSystemType;
import com.sfl.nms.services.notification.model.push.PushNotificationProperty;
import com.sfl.nms.services.notification.model.push.PushNotificationRecipient;
import com.sfl.nms.externalclients.push.amazon.communicator.AmazonSnsApiCommunicator;
import com.sfl.nms.externalclients.push.amazon.model.AmazonSNSPlatformType;
import com.sfl.nms.externalclients.push.amazon.model.request.SendPushNotificationRequestMessageInformation;
import com.sfl.nms.externalclients.push.amazon.model.response.SendPushNotificationResponse;
import com.sfl.nms.services.notification.model.push.PushNotification;
import com.sfl.nms.services.notification.model.push.PushNotificationProviderType;
import com.sfl.nms.services.test.AbstractServicesUnitTest;
import static org.easymock.EasyMock.*;
import org.easymock.Mock;
import org.easymock.TestSubject;
import static org.junit.Assert.*;
import org.junit.Test;

import java.util.*;

/**
 * User: Ruben Dilanyan
 * Company: SFL LLC
 * Date: 8/17/15
 * Time: 3:56 PM
 */
public class PushNotificationSnsProviderProcessorImplTest extends AbstractServicesUnitTest {

    /* Test subject and mocks */
    @TestSubject
    private PushNotificationSnsProviderProcessorImpl pushNotificationSnsProcessor = new PushNotificationSnsProviderProcessorImpl();

    @Mock
    private AmazonSnsApiCommunicator amazonSnsApiCommunicator;

    /* Constructors */
    public PushNotificationSnsProviderProcessorImplTest() {
    }

    /* Test methods */
    @Test
    public void testProcessPushNotificationWithInvalidArguments() {
        // Reset
        resetAll();
        // Replay
        replayAll();
        // Run test scenario
        try {
            pushNotificationSnsProcessor.processPushNotification(null);
            fail("Exception should be thrown");
        } catch (final IllegalArgumentException ex) {
            // Expected
        }
        // Verify
        verifyAll();
    }

    @Test
    public void testProcessPushNotificationWithInvalidProviderType() {
        // Push notification
        final Long notificationId = 1L;
        final PushNotification notification = getServicesImplTestHelper().createPushNotification();
        notification.setId(notificationId);
        final Long recipientId = 2L;
        final PushNotificationRecipient recipient = getServicesImplTestHelper().createPushNotificationSnsRecipient();
        recipient.setId(recipientId);
        recipient.setType(PushNotificationProviderType.GCM);
        notification.setRecipient(recipient);
        // Reset
        resetAll();
        // Replay
        replayAll();
        // Run test scenario
        try {
            pushNotificationSnsProcessor.processPushNotification(notification);
            fail("Exception should be thrown");
        } catch (final IllegalArgumentException ex) {
            // Expected
        }
        // Verify
        verifyAll();
    }

    @Test
    public void testProcessPushNotificationForIOSPlatform() {
        testProcessPushNotification(DeviceOperatingSystemType.IOS);
    }

    @Test
    public void testProcessPushNotificationForAndroidPlatform() {
        testProcessPushNotification(DeviceOperatingSystemType.ANDROID);
    }

    /* Utility methods */
    private void testProcessPushNotification(final DeviceOperatingSystemType operatingSystemType) {
        // Push notification
        final Long notificationId = 1L;
        final PushNotification notification = getServicesImplTestHelper().createPushNotification();
        notification.setId(notificationId);
        final Long recipientId = 2L;
        final PushNotificationRecipient recipient = getServicesImplTestHelper().createPushNotificationSnsRecipient();
        recipient.setId(recipientId);
        recipient.setDeviceOperatingSystemType(operatingSystemType);
        notification.setRecipient(recipient);
        // Amazon SNS platform type
        final AmazonSNSPlatformType amazonSNSPlatformType = getAmazonSNSPlatformType(recipient.getDeviceOperatingSystemType());
        // Create list of push notification properties
        final Set<PushNotificationProperty> pushNotificationProperties = createPushNotificationProperties(10);
        notification.setProperties(pushNotificationProperties);
        // Create expected attributes map
        final Map<String, String> pushNotificationAttributes = createPushNotificationAttributes(notification);
        final SendPushNotificationRequestMessageInformation messageInformation = new SendPushNotificationRequestMessageInformation(notification.getSubject(), notification.getContent(), pushNotificationAttributes, amazonSNSPlatformType);
        final String amazonMessageId = UUID.randomUUID().toString();
        final SendPushNotificationResponse response = new SendPushNotificationResponse(amazonMessageId);
        // Reset
        resetAll();
        // Expectations
        expect(amazonSnsApiCommunicator.sendPushNotification(eq(messageInformation), eq(recipient.getDestinationRouteToken()))).andReturn(response).once();
        // Replay
        replayAll();
        // Run test scenario
        final String result = pushNotificationSnsProcessor.processPushNotification(notification);
        assertEquals(result, response.getMessageId());
        // Verify
        verifyAll();
    }

    private AmazonSNSPlatformType getAmazonSNSPlatformType(final DeviceOperatingSystemType operatingSystemType) {
        switch (operatingSystemType) {
            case IOS:
                return AmazonSNSPlatformType.APNS;
            case ANDROID:
                return AmazonSNSPlatformType.GCM;
            default: {
                final String message = "Unsupported operating system type - " + operatingSystemType;
                throw new ServicesRuntimeException(message);
            }
        }
    }

    private Set<PushNotificationProperty> createPushNotificationProperties(final int count) {
        final Set<PushNotificationProperty> pushNotificationProperties = new LinkedHashSet<>();
        for (int i = 0; i < count; i++) {
            final PushNotificationProperty pushNotificationProperty = getServicesImplTestHelper().createPushNotificationProperty();
            pushNotificationProperty.setPropertyKey(pushNotificationProperty.getPropertyKey() + "_" + i);
            pushNotificationProperty.setPropertyValue(pushNotificationProperty.getPropertyValue() + "_" + i);
        }
        return pushNotificationProperties;
    }

    private Map<String, String> createPushNotificationAttributes(final PushNotification pushNotification) {
        final Map<String, String> attributes = new LinkedHashMap<>();
        pushNotification.getProperties().forEach(pushNotificationProperty -> {
            // Add to the map of attributes
            attributes.put(pushNotificationProperty.getPropertyKey(), pushNotificationProperty.getPropertyValue());
        });
        return attributes;
    }
}
