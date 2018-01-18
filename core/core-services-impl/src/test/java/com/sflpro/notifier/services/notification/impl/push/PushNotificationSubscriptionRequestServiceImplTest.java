package com.sflpro.notifier.services.notification.impl.push;

import com.sflpro.notifier.db.entities.device.UserDevice;
import com.sflpro.notifier.db.entities.user.User;
import com.sflpro.notifier.persistence.repositories.notification.push.PushNotificationSubscriptionRequestRepository;
import com.sflpro.notifier.services.device.UserDeviceService;
import com.sflpro.notifier.services.notification.dto.push.PushNotificationSubscriptionRequestDto;
import com.sflpro.notifier.services.notification.exception.push.PushNotificationSubscriptionInvalidDeviceUserException;
import com.sflpro.notifier.services.notification.exception.push.PushNotificationSubscriptionRequestInvalidRecipientUserException;
import com.sflpro.notifier.services.notification.exception.push.PushNotificationSubscriptionRequestNotFoundForIdException;
import com.sflpro.notifier.services.notification.exception.push.PushNotificationSubscriptionRequestNotFoundForUuIdException;
import com.sflpro.notifier.db.entities.notification.push.PushNotificationRecipient;
import com.sflpro.notifier.db.entities.notification.push.PushNotificationSubscription;
import com.sflpro.notifier.db.entities.notification.push.PushNotificationSubscriptionRequest;
import com.sflpro.notifier.db.entities.notification.push.PushNotificationSubscriptionRequestState;
import com.sflpro.notifier.services.notification.push.PushNotificationRecipientService;
import com.sflpro.notifier.services.test.AbstractServicesUnitTest;
import com.sflpro.notifier.services.user.UserService;
import org.easymock.Mock;
import org.easymock.TestSubject;
import org.junit.Assert;
import org.junit.Test;

import java.util.Date;

import static org.easymock.EasyMock.*;
import static org.junit.Assert.*;

/**
 * User: Ruben Dilanyan
 * Company: SFL LLC
 * Date: 8/21/15
 * Time: 9:20 AM
 */
public class PushNotificationSubscriptionRequestServiceImplTest extends AbstractServicesUnitTest {

    /* Test subject and mocks */
    @TestSubject
    private PushNotificationSubscriptionRequestServiceImpl pushNotificationSubscriptionRequestService = new PushNotificationSubscriptionRequestServiceImpl();

    @Mock
    private PushNotificationSubscriptionRequestRepository pushNotificationSubscriptionRequestRepository;

    @Mock
    private UserService userService;

    @Mock
    private UserDeviceService userMobileDeviceService;

    @Mock
    private PushNotificationRecipientService pushNotificationRecipientService;

    /* Constructors */
    public PushNotificationSubscriptionRequestServiceImplTest() {
    }

    /* Test methods */
    @Test
    public void testUpdatePushNotificationSubscriptionRequestRecipientWithInvalidArguments() {
        // Test data
        final Long requestId = 1L;
        final Long recipientId = 2L;
        // Reset
        resetAll();
        // Replay
        replayAll();
        // Run test scenario
        try {
            pushNotificationSubscriptionRequestService.updatePushNotificationSubscriptionRequestRecipient(null, recipientId);
            fail("Exception should be thrown");
        } catch (final IllegalArgumentException ex) {
            // Expected
        }
        try {
            pushNotificationSubscriptionRequestService.updatePushNotificationSubscriptionRequestRecipient(requestId, null);
            fail("Exception should be thrown");
        } catch (final IllegalArgumentException ex) {
            // Expected
        }
        // Verify
        verifyAll();
    }

    @Test
    public void testUpdatePushNotificationSubscriptionRequestRecipientWithNotExistingId() {
        // Test data
        final Long requestId = 1L;
        final Long recipientId = 2L;
        // Reset
        resetAll();
        // Expectations
        expect(pushNotificationSubscriptionRequestRepository.findOne(eq(requestId))).andReturn(null).once();
        // Replay
        replayAll();
        // Run test scenario
        try {
            pushNotificationSubscriptionRequestService.updatePushNotificationSubscriptionRequestRecipient(requestId, recipientId);
            fail("Exception should be thrown");
        } catch (final PushNotificationSubscriptionRequestNotFoundForIdException ex) {
            // Expected
            assertPushNotificationSubscriptionRequestNotFoundForIdException(ex, requestId);
        }
        // Verify
        verifyAll();
    }

    @Test
    public void testUpdatePushNotificationSubscriptionRequestRecipientWithDifferentRecipientUser() {
        // Test data
        final Long requestId = 1L;
        final PushNotificationSubscriptionRequest request = getServicesImplTestHelper().createPushNotificationSubscriptionRequest();
        request.setId(requestId);
        final Long userId = 3L;
        final User user = getServicesImplTestHelper().createUser();
        user.setId(userId);
        request.setUser(user);
        final Long recipientId = 2L;
        final PushNotificationRecipient recipient = getServicesImplTestHelper().createPushNotificationSnsRecipient();
        recipient.setId(recipientId);
        final Long subscriptionId = 4L;
        final PushNotificationSubscription subscription = getServicesImplTestHelper().createPushNotificationSubscription();
        subscription.setId(subscriptionId);
        recipient.setSubscription(subscription);
        final Long subscriptionUserId = 5L;
        final User subscriptionUser = getServicesImplTestHelper().createUser();
        subscriptionUser.setId(subscriptionUserId);
        subscription.setUser(subscriptionUser);
        // Reset
        resetAll();
        // Expectations
        expect(pushNotificationSubscriptionRequestRepository.findOne(eq(requestId))).andReturn(request).once();
        expect(pushNotificationRecipientService.getPushNotificationRecipientById(eq(recipientId))).andReturn(recipient).once();
        // Replay
        replayAll();
        // Run test scenario
        try {
            pushNotificationSubscriptionRequestService.updatePushNotificationSubscriptionRequestRecipient(requestId, recipientId);
            fail("Exception should be thrown");
        } catch (final PushNotificationSubscriptionRequestInvalidRecipientUserException ex) {
            // Expected
            assertPushNotificationSubscriptionRequestInvalidRecipientUserException(ex, requestId, userId, recipientId, subscriptionUserId);
        }
        // Verify
        verifyAll();
    }

    @Test
    public void testUpdatePushNotificationSubscriptionRequestRecipient() {
        // Test data
        final Long requestId = 1L;
        final PushNotificationSubscriptionRequest request = getServicesImplTestHelper().createPushNotificationSubscriptionRequest();
        request.setId(requestId);
        final Date requestUpdateDate = request.getUpdated();
        final Long userId = 3L;
        final User user = getServicesImplTestHelper().createUser();
        user.setId(userId);
        request.setUser(user);
        final Long recipientId = 2L;
        final PushNotificationRecipient recipient = getServicesImplTestHelper().createPushNotificationSnsRecipient();
        recipient.setId(recipientId);
        final Long subscriptionId = 4L;
        final PushNotificationSubscription subscription = getServicesImplTestHelper().createPushNotificationSubscription();
        subscription.setId(subscriptionId);
        recipient.setSubscription(subscription);
        subscription.setUser(user);
        // Reset
        resetAll();
        // Expectations
        expect(pushNotificationSubscriptionRequestRepository.findOne(eq(requestId))).andReturn(request).once();
        expect(pushNotificationRecipientService.getPushNotificationRecipientById(eq(recipientId))).andReturn(recipient).once();
        expect(pushNotificationSubscriptionRequestRepository.save(isA(PushNotificationSubscriptionRequest.class))).andAnswer(() -> (PushNotificationSubscriptionRequest) getCurrentArguments()[0]).once();
        // Replay
        replayAll();
        // Run test scenario
        final PushNotificationSubscriptionRequest result = pushNotificationSubscriptionRequestService.updatePushNotificationSubscriptionRequestRecipient(requestId, recipientId);
        assertNotNull(result);
        assertEquals(recipient, result.getRecipient());
        assertTrue(result.getUpdated().compareTo(requestUpdateDate) >= 0 && requestUpdateDate != result.getUpdated());
        // Verify
        verifyAll();
    }

    @Test
    public void testCheckIfPushNotificationSubscriptionRecipientExistsForUuIdWithInvalidArguments() {
        // Reset
        resetAll();
        // Replay
        replayAll();
        // Run test scenario
        try {
            pushNotificationSubscriptionRequestService.checkIfPushNotificationSubscriptionRecipientExistsForUuId(null);
            fail("Exception should be thrown");
        } catch (final IllegalArgumentException ex) {
            // Expected
        }
        // Verify
        verifyAll();
    }

    @Test
    public void testCheckIfPushNotificationSubscriptionRecipientExistsForUuIdWhenItDoesNotExistYet() {
        // Test data
        final String uuId = "KJUH*&)TCITCITRUFUGU";
        // Reset
        resetAll();
        // Expectations
        expect(pushNotificationSubscriptionRequestRepository.findByUuId(eq(uuId))).andReturn(null).once();
        // Replay
        replayAll();
        // Run test scenario
        final boolean exists = pushNotificationSubscriptionRequestService.checkIfPushNotificationSubscriptionRecipientExistsForUuId(uuId);
        assertFalse(exists);
        // Verify
        verifyAll();
    }

    @Test
    public void testCheckIfPushNotificationSubscriptionRecipientExistsForUuIdWhenItAlreadyExists() {
        // Test data
        final String uuId = "KJUH*&)TCITCITRUFUGU";
        final Long requestId = 1L;
        final PushNotificationSubscriptionRequest request = getServicesImplTestHelper().createPushNotificationSubscriptionRequest();
        request.setId(requestId);
        // Reset
        resetAll();
        // Expectations
        expect(pushNotificationSubscriptionRequestRepository.findByUuId(eq(uuId))).andReturn(request).once();
        // Replay
        replayAll();
        // Run test scenario
        final boolean exists = pushNotificationSubscriptionRequestService.checkIfPushNotificationSubscriptionRecipientExistsForUuId(uuId);
        assertTrue(exists);
        // Verify
        verifyAll();
    }

    @Test
    public void testGetPushNotificationSubscriptionRequestByUuIdWithInvalidArguments() {
        // Reset
        resetAll();
        // Replay
        replayAll();
        // Run test scenario
        try {
            pushNotificationSubscriptionRequestService.getPushNotificationSubscriptionRequestByUuId(null);
            fail("Exception should be thrown");
        } catch (final IllegalArgumentException ex) {
            // Expected
        }
        // Verify
        verifyAll();
    }

    @Test
    public void testGetPushNotificationSubscriptionRequestByUuIdWithNotExistingUuId() {
        // Test data
        final String uuId = "IHPUGYGOYG&**GOYVVVOLB";
        // Reset
        resetAll();
        // Expectations
        expect(pushNotificationSubscriptionRequestRepository.findByUuId(eq(uuId))).andReturn(null).once();
        // Replay
        replayAll();
        // Run test scenario
        try {
            pushNotificationSubscriptionRequestService.getPushNotificationSubscriptionRequestByUuId(uuId);
            fail("Exception should be thrown");
        } catch (final PushNotificationSubscriptionRequestNotFoundForUuIdException ex) {
            // Expected
            assertPushNotificationSubscriptionRequestNotFoundForUuIdException(ex, uuId);
        }
        // Verify
        verifyAll();
    }

    @Test
    public void testGetPushNotificationSubscriptionRequestByUuId() {
        // Test data
        final String uuId = "IHPUGYGOYG&**GOYVVVOLB";
        final Long requestId = 1L;
        final PushNotificationSubscriptionRequest request = getServicesImplTestHelper().createPushNotificationSubscriptionRequest();
        request.setId(requestId);
        // Reset
        resetAll();
        // Expectations
        expect(pushNotificationSubscriptionRequestRepository.findByUuId(eq(uuId))).andReturn(request).once();
        // Replay
        replayAll();
        // Run test scenario
        final PushNotificationSubscriptionRequest result = pushNotificationSubscriptionRequestService.getPushNotificationSubscriptionRequestByUuId(uuId);
        assertEquals(result, request);
        // Verify
        verifyAll();
    }

    @Test
    public void testUpdatePushNotificationSubscriptionRequestStateWithInvalidArguments() {
        // Test data
        final Long requestId = 1L;
        final PushNotificationSubscriptionRequestState state = PushNotificationSubscriptionRequestState.PROCESSED;
        // Reset
        resetAll();
        // Replay
        replayAll();
        // Run test scenario
        try {
            pushNotificationSubscriptionRequestService.updatePushNotificationSubscriptionRequestState(null, state);
            fail("Exception should be thrown");
        } catch (final IllegalArgumentException ex) {
            // Expected
        }
        try {
            pushNotificationSubscriptionRequestService.updatePushNotificationSubscriptionRequestState(requestId, null);
            fail("Exception should be thrown");
        } catch (final IllegalArgumentException ex) {
            // Expected
        }
        // Verify
        verifyAll();
    }

    @Test
    public void testUpdatePushNotificationSubscriptionRequestStateWithNotExistingId() {
        // Test data
        final Long requestId = 1L;
        final PushNotificationSubscriptionRequestState state = PushNotificationSubscriptionRequestState.PROCESSED;
        // Reset
        resetAll();
        // Expectations
        expect(pushNotificationSubscriptionRequestRepository.findOne(eq(requestId))).andReturn(null).once();
        // Replay
        replayAll();
        // Run test scenario
        try {
            pushNotificationSubscriptionRequestService.updatePushNotificationSubscriptionRequestState(requestId, state);
            fail("Exception should be thrown");
        } catch (final PushNotificationSubscriptionRequestNotFoundForIdException ex) {
            // Expected
            assertPushNotificationSubscriptionRequestNotFoundForIdException(ex, requestId);
        }
        // Verify
        verifyAll();
    }

    @Test
    public void testUpdatePushNotificationSubscriptionRequestState() {
        // Test data
        final Long requestId = 1L;
        final PushNotificationSubscriptionRequest request = getServicesImplTestHelper().createPushNotificationSubscriptionRequest();
        request.setId(requestId);
        final Date requestUpdateDate = request.getUpdated();
        final PushNotificationSubscriptionRequestState state = PushNotificationSubscriptionRequestState.PROCESSED;
        // Reset
        resetAll();
        // Expectations
        expect(pushNotificationSubscriptionRequestRepository.findOne(eq(requestId))).andReturn(request).once();
        expect(pushNotificationSubscriptionRequestRepository.save(isA(PushNotificationSubscriptionRequest.class))).andAnswer(() -> (PushNotificationSubscriptionRequest) getCurrentArguments()[0]).once();
        // Replay
        replayAll();
        // Run test scenario
        final PushNotificationSubscriptionRequest result = pushNotificationSubscriptionRequestService.updatePushNotificationSubscriptionRequestState(requestId, state);
        assertNotNull(result);
        assertEquals(request, result);
        assertTrue(result.getUpdated().compareTo(requestUpdateDate) >= 0 && requestUpdateDate != result.getUpdated());
        // Verify
        verifyAll();
    }

    @Test
    public void testGetPushNotificationSubscriptionRequestByIdWithInvalidArguments() {
        // Reset
        resetAll();
        // Replay
        replayAll();
        // Run test scenario
        try {
            pushNotificationSubscriptionRequestService.getPushNotificationSubscriptionRequestById(null);
            fail("Exception should be thrown");
        } catch (final IllegalArgumentException ex) {
            // Expected
        }
        // Verify
        verifyAll();
    }

    @Test
    public void testGetPushNotificationSubscriptionRequestByIdWithNotExistingId() {
        // Test data
        final Long requestId = 1L;
        // Reset
        resetAll();
        // Expectations
        expect(pushNotificationSubscriptionRequestRepository.findOne(eq(requestId))).andReturn(null).once();
        // Replay
        replayAll();
        // Run test scenario
        try {
            pushNotificationSubscriptionRequestService.getPushNotificationSubscriptionRequestById(requestId);
            fail("Exception should be thrown");
        } catch (final PushNotificationSubscriptionRequestNotFoundForIdException ex) {
            // Expected
            assertPushNotificationSubscriptionRequestNotFoundForIdException(ex, requestId);
        }
        // Verify
        verifyAll();
    }

    @Test
    public void testGetPushNotificationSubscriptionRequestById() {
        // Test data
        final Long requestId = 1L;
        final PushNotificationSubscriptionRequest request = getServicesImplTestHelper().createPushNotificationSubscriptionRequest();
        request.setId(requestId);
        // Reset
        resetAll();
        // Expectations
        expect(pushNotificationSubscriptionRequestRepository.findOne(eq(requestId))).andReturn(request).once();
        // Replay
        replayAll();
        // Run test scenario
        final PushNotificationSubscriptionRequest result = pushNotificationSubscriptionRequestService.getPushNotificationSubscriptionRequestById(requestId);
        assertEquals(request, result);
        // Verify
        verifyAll();
    }

    @Test
    public void testCreatePushNotificationSubscriptionRequestWithInvalidArguments() {
        // Test data
        final Long userId = 1L;
        final Long userDeviceId = 2L;
        final PushNotificationSubscriptionRequestDto requestDto = getServicesImplTestHelper().createPushNotificationSubscriptionRequestDto();
        // Reset
        resetAll();
        // Replay
        replayAll();
        // Run test scenario
        try {
            pushNotificationSubscriptionRequestService.createPushNotificationSubscriptionRequest(null, userDeviceId, requestDto);
            fail("Exception should be thrown");
        } catch (final IllegalArgumentException ex) {
            // Expected
        }
        try {
            pushNotificationSubscriptionRequestService.createPushNotificationSubscriptionRequest(userId, null, requestDto);
            fail("Exception should be thrown");
        } catch (final IllegalArgumentException ex) {
            // Expected
        }
        try {
            pushNotificationSubscriptionRequestService.createPushNotificationSubscriptionRequest(userId, userDeviceId, null);
            fail("Exception should be thrown");
        } catch (final IllegalArgumentException ex) {
            // Expected
        }
        try {
            pushNotificationSubscriptionRequestService.createPushNotificationSubscriptionRequest(userId, userDeviceId, new PushNotificationSubscriptionRequestDto(null, requestDto.getApplicationType(), requestDto.isSubscribe(), requestDto.getPreviousSubscriptionRequestUuId()));
            fail("Exception should be thrown");
        } catch (final IllegalArgumentException ex) {
            // Expected
        }
        try {
            pushNotificationSubscriptionRequestService.createPushNotificationSubscriptionRequest(userId, userDeviceId, new PushNotificationSubscriptionRequestDto(requestDto.getUserDeviceToken(), null, requestDto.isSubscribe(), requestDto.getPreviousSubscriptionRequestUuId()));
            fail("Exception should be thrown");
        } catch (final IllegalArgumentException ex) {
            // Expected
        }
        // Verify
        verifyAll();
    }

    @Test
    public void testCreatePushNotificationSubscriptionRequestWithDifferentDeviceAndProvidedUsers() {
        // Test data
        final Long userId = 1L;
        final User user = getServicesImplTestHelper().createUser();
        user.setId(userId);
        final Long userDeviceId = 2L;
        final UserDevice userDevice = getServicesImplTestHelper().createUserDevice();
        userDevice.setId(userDeviceId);
        final Long deviceUserId = 3L;
        final User deviceUser = getServicesImplTestHelper().createUser();
        deviceUser.setId(deviceUserId);
        userDevice.setUser(deviceUser);
        final PushNotificationSubscriptionRequestDto requestDto = getServicesImplTestHelper().createPushNotificationSubscriptionRequestDto();
        // Reset
        resetAll();
        // Expectations
        expect(userService.getUserById(eq(userId))).andReturn(user).once();
        expect(userMobileDeviceService.getUserDeviceById(eq(userDeviceId))).andReturn(userDevice).once();
        // Replay
        replayAll();
        // Run test scenario
        try {
            pushNotificationSubscriptionRequestService.createPushNotificationSubscriptionRequest(userId, userDeviceId, requestDto);
            fail("Exception should be thrown");
        } catch (final PushNotificationSubscriptionInvalidDeviceUserException ex) {
            // Expected
            assertPushNotificationSubscriptionInvalidDeviceUserException(ex, userId, userDeviceId, deviceUserId);
        }
        // Verify
        verifyAll();
    }

    @Test
    public void testCreatePushNotificationSubscriptionRequest() {
        // Test data
        final Long userId = 1L;
        final User user = getServicesImplTestHelper().createUser();
        user.setId(userId);
        final Long userDeviceId = 2L;
        final UserDevice userDevice = getServicesImplTestHelper().createUserDevice();
        userDevice.setId(userDeviceId);
        userDevice.setUser(user);
        final PushNotificationSubscriptionRequestDto requestDto = getServicesImplTestHelper().createPushNotificationSubscriptionRequestDto();
        // Reset
        resetAll();
        // Expectations
        expect(userService.getUserById(eq(userId))).andReturn(user).once();
        expect(userMobileDeviceService.getUserDeviceById(eq(userDeviceId))).andReturn(userDevice).once();
        expect(pushNotificationSubscriptionRequestRepository.save(isA(PushNotificationSubscriptionRequest.class))).andAnswer(() -> (PushNotificationSubscriptionRequest) getCurrentArguments()[0]).once();
        // Replay
        replayAll();
        // Run test scenario
        final PushNotificationSubscriptionRequest result = pushNotificationSubscriptionRequestService.createPushNotificationSubscriptionRequest(userId, userDeviceId, requestDto);
        getServicesImplTestHelper().assertPushNotificationSubscriptionRequest(result, requestDto);
        Assert.assertEquals(user, result.getUser());
        Assert.assertEquals(userDevice, result.getUserMobileDevice());
        // Verify
        verifyAll();
    }

    /* Utility methods */
    private void assertPushNotificationSubscriptionInvalidDeviceUserException(final PushNotificationSubscriptionInvalidDeviceUserException ex, final Long userId, final Long deviceId, final Long deviceUserId) {
        assertEquals(userId, ex.getSubscriptionUserId());
        assertEquals(deviceId, ex.getDeviceId());
        assertEquals(deviceUserId, ex.getDeviceUserId());
    }

    private void assertPushNotificationSubscriptionRequestNotFoundForIdException(final PushNotificationSubscriptionRequestNotFoundForIdException ex, final Long id) {
        Assert.assertEquals(ex.getId(), id);
        Assert.assertEquals(ex.getEntityClass(), PushNotificationSubscriptionRequest.class);
    }

    private void assertPushNotificationSubscriptionRequestNotFoundForUuIdException(final PushNotificationSubscriptionRequestNotFoundForUuIdException ex, final String uuId) {
        Assert.assertEquals(ex.getUuId(), uuId);
        Assert.assertEquals(ex.getEntityClass(), PushNotificationSubscriptionRequest.class);
    }

    private void assertPushNotificationSubscriptionRequestInvalidRecipientUserException(final PushNotificationSubscriptionRequestInvalidRecipientUserException ex, final Long requestId, final Long requestUserId, final Long recipientId, final Long recipientUserId) {
        assertEquals(recipientId, ex.getRecipientId());
        assertEquals(recipientUserId, ex.getRecipientUserId());
        assertEquals(requestId, ex.getRequestId());
        assertEquals(requestUserId, ex.getRequestUserId());
    }
}
