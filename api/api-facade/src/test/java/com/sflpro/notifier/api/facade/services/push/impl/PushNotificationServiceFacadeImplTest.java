package com.sflpro.notifier.api.facade.services.push.impl;

import com.sflpro.notifier.api.internal.facade.test.AbstractFacadeUnitTest;
import com.sflpro.notifier.api.model.common.result.ErrorType;
import com.sflpro.notifier.api.model.common.result.ResultResponseModel;
import com.sflpro.notifier.api.model.notification.NotificationSendingPriorityClientType;
import com.sflpro.notifier.api.model.push.request.CreatePushNotificationRequest;
import com.sflpro.notifier.api.model.push.request.UpdatePushNotificationSubscriptionRequest;
import com.sflpro.notifier.api.model.push.response.CreatePushNotificationResponse;
import com.sflpro.notifier.api.model.push.response.UpdatePushNotificationSubscriptionResponse;
import com.sflpro.notifier.db.entities.device.UserDevice;
import com.sflpro.notifier.db.entities.device.mobile.DeviceOperatingSystemType;
import com.sflpro.notifier.db.entities.notification.NotificationSendingPriority;
import com.sflpro.notifier.db.entities.notification.email.NotificationProperty;
import com.sflpro.notifier.db.entities.notification.push.PushNotification;
import com.sflpro.notifier.db.entities.notification.push.PushNotificationRecipient;
import com.sflpro.notifier.db.entities.notification.push.PushNotificationSubscriptionRequest;
import com.sflpro.notifier.db.entities.user.User;
import com.sflpro.notifier.services.device.UserDeviceService;
import com.sflpro.notifier.services.device.dto.UserDeviceDto;
import com.sflpro.notifier.services.notification.dto.push.PushNotificationDto;
import com.sflpro.notifier.services.notification.dto.push.PushNotificationSubscriptionRequestDto;
import com.sflpro.notifier.services.notification.event.push.StartPushNotificationSubscriptionRequestProcessingEvent;
import com.sflpro.notifier.services.notification.event.sms.StartSendingNotificationEvent;
import com.sflpro.notifier.services.notification.push.PushNotificationService;
import com.sflpro.notifier.services.notification.push.PushNotificationSubscriptionRequestService;
import com.sflpro.notifier.services.system.event.ApplicationEventDistributionService;
import com.sflpro.notifier.services.user.UserService;
import org.apache.commons.lang3.mutable.MutableInt;
import org.easymock.Mock;
import org.easymock.TestSubject;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.easymock.EasyMock.*;
import static org.junit.Assert.*;

/**
 * User: Ruben Dilanyan
 * Company: SFL LLC
 * Date: 1/13/16
 * Time: 7:43 PM
 */
public class PushNotificationServiceFacadeImplTest extends AbstractFacadeUnitTest {

    /* Test subject and mocks */
    @TestSubject
    private PushNotificationServiceFacadeImpl pushNotificationServiceFacade = new PushNotificationServiceFacadeImpl();

    @Mock
    private PushNotificationService pushNotificationService;

    @Mock
    private UserService userService;

    @Mock
    private ApplicationEventDistributionService applicationEventDistributionService;

    @Mock
    private UserDeviceService userDeviceService;

    @Mock
    private PushNotificationSubscriptionRequestService pushNotificationSubscriptionRequestService;

    /* Constructors */
    public PushNotificationServiceFacadeImplTest() {
    }

    /* Test methods */
    @Test
    public void testUpdatePushNotificationSubscriptionWithInvalidArguments() {
        // Reset
        resetAll();
        // Replay
        replayAll();
        // Run test scenario
        try {
            pushNotificationServiceFacade.updatePushNotificationSubscription(null);
            fail("Exception should be thrown");
        } catch (final IllegalArgumentException ex) {
            // Expected
        }
        // Verify
        verifyAll();
    }

    @Test
    public void testUpdatePushNotificationSubscriptionWithValidationErrors() {
        // Test data
        final UpdatePushNotificationSubscriptionRequest request = new UpdatePushNotificationSubscriptionRequest();
        // Reset
        resetAll();
        // Replay
        replayAll();
        // Run test scenario
        final ResultResponseModel<UpdatePushNotificationSubscriptionResponse> result = pushNotificationServiceFacade.updatePushNotificationSubscription(request);
        assertNotNull(result);
        assertNull(result.getResponse());
        assertNotNull(result.getErrors());
        assertEquals(6, result.getErrors().size());
        assertErrorExists(result.getErrors(), ErrorType.SUBSCRIPTION_PUSH_APPLICATION_MISSING);
        assertErrorExists(result.getErrors(), ErrorType.SUBSCRIPTION_PUSH_DEVICE_OPERATING_SYSTEM_TYPE_MISSING);
        assertErrorExists(result.getErrors(), ErrorType.SUBSCRIPTION_PUSH_DEVICE_TOKEN_MISSING);
        assertErrorExists(result.getErrors(), ErrorType.SUBSCRIPTION_PUSH_DEVICE_UUID_MISSING);
        assertErrorExists(result.getErrors(), ErrorType.SUBSCRIPTION_PUSH_USER_UUID_MISSING);
        assertErrorExists(result.getErrors(), ErrorType.SUBSCRIPTION_PUSH_SUBSCRIBE_VALUE_MISSING);
        // Verify
        verifyAll();
    }

    @Test
    public void testUpdatePushNotificationSubscription() {
        // Test data
        final UpdatePushNotificationSubscriptionRequest request = getServiceFacadeImplTestHelper().createUpdatePushNotificationSubscriptionRequest();
        // User
        final Long userId = 1L;
        final User user = getServiceFacadeImplTestHelper().createUser();
        user.setUuId(request.getUserUuId());
        user.setId(userId);
        // User device
        final UserDeviceDto userDeviceDto = new UserDeviceDto(request.getDeviceUuId(), DeviceOperatingSystemType.valueOf(request.getDeviceOperatingSystemType().name()));
        final Long userDeviceId = 3L;
        final UserDevice userDevice = getServiceFacadeImplTestHelper().createUserDevice();
        userDevice.setId(userDeviceId);
        // Subscription request
        final PushNotificationSubscriptionRequestDto subscriptionRequestDto = new PushNotificationSubscriptionRequestDto(request.getUserDeviceToken(), request.getApplication(), request.getSubscribe(), request.getLastUsedSubscriptionRequestUuId());
        final Long subscriptionRequestId = 2L;
        final PushNotificationSubscriptionRequest subscriptionRequest = getServiceFacadeImplTestHelper().createPushNotificationSubscriptionRequest();
        subscriptionRequest.setId(subscriptionRequestId);
        // Reset
        resetAll();
        // Expectations
        expect(userService.getOrCreateUserForUuId(eq(user.getUuId()))).andReturn(user).once();
        expect(userDeviceService.getOrCreateUserDevice(eq(userId), eq(userDeviceDto))).andReturn(userDevice).once();
        expect(pushNotificationSubscriptionRequestService.createPushNotificationSubscriptionRequest(eq(userId), eq(userDevice.getId()), eq(subscriptionRequestDto))).andReturn(subscriptionRequest).once();
        applicationEventDistributionService.publishAsynchronousEvent(eq(new StartPushNotificationSubscriptionRequestProcessingEvent(subscriptionRequestId)));
        expectLastCall().once();
        // Replay
        replayAll();
        // Run test scenario
        final ResultResponseModel<UpdatePushNotificationSubscriptionResponse> result = pushNotificationServiceFacade.updatePushNotificationSubscription(request);
        assertNotNull(result);
        assertNotNull(result.getResponse());
        assertNotNull(result.getErrors());
        assertEquals(0, result.getErrors().size());
        final UpdatePushNotificationSubscriptionResponse response = result.getResponse();
        assertEquals(subscriptionRequest.getUuId(), response.getSubscriptionRequestUuId());
        // Verify
        verifyAll();
    }

    @Test
    public void testCreatePushNotificationsWithInvalidArguments() {
        // Reset
        resetAll();
        // Replay
        replayAll();
        // Run test scenario
        try {
            pushNotificationServiceFacade.createPushNotifications(null);
            fail("Exception should be thrown");
        } catch (final IllegalArgumentException ex) {
            // Expected
        }
        // Verify
        verifyAll();
    }

    @Test
    public void testCreatePushNotificationsWithValidationErrors() {
        // Test data
        final CreatePushNotificationRequest request = new CreatePushNotificationRequest();
        // Reset
        resetAll();
        // Replay
        replayAll();
        // Run test scenario
        final ResultResponseModel<CreatePushNotificationResponse> result = pushNotificationServiceFacade.createPushNotifications(request);
        assertNotNull(result);
        assertNull(result.getResponse());
        assertNotNull(result.getErrors());
        assertErrorExists(result.getErrors(), ErrorType.NOTIFICATION_BODY_MISSING);
        assertErrorExists(result.getErrors(), ErrorType.NOTIFICATION_USER_MISSING);
        // Verify
        verifyAll();
    }

    @Test
    public void testCreatePushNotifications() {
        // Test data
        final CreatePushNotificationRequest request = getServiceFacadeImplTestHelper().createCreatePushNotificationRequest();
        request.setSendingPriority(NotificationSendingPriorityClientType.MEDIUM);
        // Create user
        final Long userId = 1L;
        final User user = getServiceFacadeImplTestHelper().createUser();
        user.setUuId(request.getUserUuId());
        user.setId(userId);
        // Expected push notification DTO
        final PushNotificationDto pushNotificationDto = new PushNotificationDto(request.getBody(), request.getSubject(), request.getClientIpAddress());
        pushNotificationDto.setProperties(request.getProperties());
        pushNotificationDto.setSendingPriority(NotificationSendingPriority.MEDIUM);
        final List<PushNotification> pushNotifications = createPushNotifications(10);
        // Reset
        resetAll();
        // Expectations
        expect(userService.getOrCreateUserForUuId(eq(user.getUuId()))).andReturn(user).once();
        expect(pushNotificationService.createNotificationsForUserActiveRecipients(eq(user.getId()), eq(pushNotificationDto))).andReturn(pushNotifications).once();
        pushNotifications.forEach(pushNotification -> {
            applicationEventDistributionService.publishAsynchronousEvent(eq(new StartSendingNotificationEvent(pushNotification.getId())));
            expectLastCall().once();
        });
        // Replay
        replayAll();
        // Run test scenario
        final ResultResponseModel<CreatePushNotificationResponse> result = pushNotificationServiceFacade.createPushNotifications(request);
        assertNotNull(result);
        assertNotNull(result.getResponse());
        assertEquals(0, result.getErrors().size());
        // Assert result
        final MutableInt counter = new MutableInt(0);
        result.getResponse().getNotifications().forEach(pushNotificationModel -> {
            getServiceFacadeImplTestHelper().assertPushNotificationModel(pushNotifications.get(counter.intValue()), pushNotificationModel);
            counter.increment();
        });
        // Verify
        verifyAll();
    }

    /* Utility methods */
    private List<PushNotification> createPushNotifications(final int count) {
        final List<PushNotification> pushNotifications = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            final PushNotification pushNotification = getServiceFacadeImplTestHelper().createPushNotification();
            pushNotification.setId((long) i);
            // Create recipient
            final PushNotificationRecipient recipient = getServiceFacadeImplTestHelper().createPushNotificationSnsRecipient();
            recipient.setId((long) ((i + 1) * 2));
            pushNotification.setRecipient(recipient);
            // Create properties
            final List<NotificationProperty> properties = new ArrayList<>();
            for (int j = 0; j < i + 1; j++) {
                final NotificationProperty pushNotificationProperty = getServiceFacadeImplTestHelper().createPushNotificationProperty();
                pushNotificationProperty.setPropertyKey(pushNotificationProperty.getPropertyKey() + "_" + i + "_" + j);
                pushNotificationProperty.setPropertyValue(pushNotificationProperty.getPropertyValue() + "_" + i + "_" + j);
                properties.add(pushNotificationProperty);
            }
            pushNotification.setProperties(properties);
            // Add to the list
            pushNotifications.add(pushNotification);
        }
        return pushNotifications;
    }

}
