package com.sflpro.notifier.services.notification.impl.push.sns;

import com.sflpro.notifier.db.entities.NotificationProperty;
import com.sflpro.notifier.db.entities.device.mobile.DeviceOperatingSystemType;
import com.sflpro.notifier.db.entities.notification.push.PushNotification;
import com.sflpro.notifier.db.entities.notification.push.PushNotificationProviderType;
import com.sflpro.notifier.db.entities.notification.push.PushNotificationRecipient;
import com.sflpro.notifier.externalclients.push.amazon.communicator.AmazonSnsApiCommunicator;
import com.sflpro.notifier.externalclients.push.amazon.model.AmazonSNSPlatformType;
import com.sflpro.notifier.externalclients.push.amazon.model.request.SendPushNotificationRequestMessageInformation;
import com.sflpro.notifier.externalclients.push.amazon.model.response.SendPushNotificationResponse;
import com.sflpro.notifier.services.common.exception.ServicesRuntimeException;
import com.sflpro.notifier.services.test.AbstractServicesUnitTest;
import org.easymock.Mock;
import org.easymock.TestSubject;
import org.junit.Test;

import java.util.*;

import static org.easymock.EasyMock.eq;
import static org.easymock.EasyMock.expect;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

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
        final Set<NotificationProperty> pushNotificationProperties = createPushNotificationProperties(10);
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

    private Set<NotificationProperty> createPushNotificationProperties(final int count) {
        final Set<NotificationProperty> pushNotificationProperties = new LinkedHashSet<>();
        for (int i = 0; i < count; i++) {
            final NotificationProperty pushNotificationProperty = getServicesImplTestHelper().createNotificationProperty();
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
