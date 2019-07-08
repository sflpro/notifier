package com.sflpro.notifier.services.notification.impl.push;

import com.sflpro.notifier.db.entities.device.UserDevice;
import com.sflpro.notifier.db.entities.notification.NotificationProviderType;
import com.sflpro.notifier.db.entities.notification.push.PushNotificationProviderType;
import com.sflpro.notifier.db.entities.notification.push.PushNotificationRecipient;
import com.sflpro.notifier.db.entities.notification.push.PushNotificationSubscriptionRequest;
import com.sflpro.notifier.db.entities.notification.push.PushNotificationSubscriptionRequestState;
import com.sflpro.notifier.db.entities.user.User;
import com.sflpro.notifier.db.repositories.utility.PersistenceUtilityService;
import com.sflpro.notifier.services.common.exception.ServicesRuntimeException;
import com.sflpro.notifier.services.notification.dto.push.PushNotificationSubscriptionProcessingParameters;
import com.sflpro.notifier.services.notification.exception.push.PushNotificationSubscriptionRequestInvalidStateException;
import com.sflpro.notifier.services.notification.push.PushNotificationSubscriptionProcessingService;
import com.sflpro.notifier.services.notification.push.PushNotificationSubscriptionRequestService;
import com.sflpro.notifier.services.test.AbstractServicesUnitTest;
import org.easymock.Mock;
import org.easymock.TestSubject;
import org.junit.Assert;
import org.junit.Test;

import java.util.UUID;

import static org.easymock.EasyMock.*;
import static org.junit.Assert.*;

/**
 * User: Ruben Dilanyan
 * Company: SFL LLC
 * Date: 8/21/15
 * Time: 11:12 AM
 */
public class PushNotificationSubscriptionRequestProcessingServiceImplTest extends AbstractServicesUnitTest {

    /* Dependencies */
    @TestSubject
    private PushNotificationSubscriptionRequestProcessingServiceImpl pushNotificationSubscriptionRequestProcessingService = new PushNotificationSubscriptionRequestProcessingServiceImpl(NotificationProviderType.AMAZON_SNS);

    @Mock
    private PushNotificationSubscriptionRequestService pushNotificationSubscriptionRequestService;

    @Mock
    private PushNotificationSubscriptionProcessingService pushNotificationSubscriptionProcessingService;

    @Mock
    private PersistenceUtilityService persistenceUtilityService;

    /* Constructors */
    public PushNotificationSubscriptionRequestProcessingServiceImplTest() {
    }

    /* Test methods */
    @Test
    public void testProcessPushNotificationSubscriptionRequestWithInvalidArguments() {
        // Reset
        resetAll();
        // Replay
        replayAll();
        // Run test scenario
        try {
            pushNotificationSubscriptionRequestProcessingService.processPushNotificationSubscriptionRequest(null);
            fail("Exception should be thrown");
        } catch (final IllegalArgumentException ex) {
            // Expected
        }
        // Verify
        verifyAll();
    }

    @Test
    public void testProcessPushNotificationSubscriptionRequestWhenRequestHasInvalidState() {
        // Test data
        final Long requestId = 1L;
        final PushNotificationSubscriptionRequest request = getServicesImplTestHelper().createPushNotificationSubscriptionRequest();
        request.setId(requestId);
        request.setState(PushNotificationSubscriptionRequestState.PROCESSING);
        // Reset
        resetAll();
        // Expectations
        expect(pushNotificationSubscriptionRequestService.getPushNotificationSubscriptionRequestById(eq(requestId))).andReturn(request).once();
        // Replay
        replayAll();
        // Run test scenario
        try {
            pushNotificationSubscriptionRequestProcessingService.processPushNotificationSubscriptionRequest(requestId);
            fail("Exception should be thrown");
        } catch (final PushNotificationSubscriptionRequestInvalidStateException ex) {
            // Expected
            assertPushNotificationSubscriptionRequestInvalidStateException(ex, requestId, request.getState());
        }
        // Verify
        verifyAll();
    }

    @Test
    public void testProcessPushNotificationSubscriptionRequestWhenExceptionOccursDuringProcessing() {
        // Test data
        // Create new user
        final Long userId = 5L;
        final User user = getServicesImplTestHelper().createUser();
        user.setId(userId);
        // User mobile device
        final Long userMobileDeviceId = 6L;
        final UserDevice userMobileDevice = getServicesImplTestHelper().createUserDevice();
        userMobileDevice.setId(userMobileDeviceId);
        // Create new request
        final Long requestId = 1L;
        final PushNotificationSubscriptionRequest request = getServicesImplTestHelper().createPushNotificationSubscriptionRequest();
        request.setId(requestId);
        request.setState(PushNotificationSubscriptionRequestState.CREATED);
        request.setUser(user);
        request.setUserMobileDevice(userMobileDevice);
        // Create previous request
        final Long previousRequestId = 2L;
        final PushNotificationSubscriptionRequest previousRequest = getServicesImplTestHelper().createPushNotificationSubscriptionRequest();
        previousRequest.setId(previousRequestId);
        previousRequest.setUserDeviceToken(UUID.randomUUID().toString());
        // Create previous recipient
        final Long previousRecipientId = 3L;
        final PushNotificationRecipient previousRecipient = getServicesImplTestHelper().createPushNotificationSnsRecipient();
        previousRecipient.setId(previousRecipientId);
        previousRequest.setRecipient(previousRecipient);
        // Created recipient
        final Long createdRecipientId = 4L;
        final PushNotificationRecipient createdRecipient = getServicesImplTestHelper().createPushNotificationSnsRecipient();
        createdRecipient.setId(createdRecipientId);
        // Exception message
        final String expectedExceptionMessage = "Testing error handling";
        // Processing parameters
        final PushNotificationSubscriptionProcessingParameters processingParameters = buildExpectedSubscriptionProcessingParameters(request, previousRecipient);
        // Reset
        resetAll();
        // Expectations
        expect(pushNotificationSubscriptionRequestService.getPushNotificationSubscriptionRequestById(eq(requestId))).andReturn(request).once();
        expect(pushNotificationSubscriptionRequestService.updatePushNotificationSubscriptionRequestState(eq(requestId), eq(PushNotificationSubscriptionRequestState.PROCESSING))).andReturn(request).once();
        expect(pushNotificationSubscriptionRequestService.checkIfPushNotificationSubscriptionRecipientExistsForUuId(eq(request.getPreviousSubscriptionRequestUuId()))).andReturn(true).once();
        expect(pushNotificationSubscriptionRequestService.getPushNotificationSubscriptionRequestByUuId(eq(request.getPreviousSubscriptionRequestUuId()))).andReturn(previousRequest).once();
        expect(pushNotificationSubscriptionProcessingService.processPushNotificationSubscriptionChange(eq(processingParameters))).andThrow(new ServicesRuntimeException(expectedExceptionMessage)).once();
        expect(pushNotificationSubscriptionRequestService.updatePushNotificationSubscriptionRequestState(eq(requestId), eq(PushNotificationSubscriptionRequestState.FAILED))).andReturn(request).once();
        persistenceUtilityService.runInNewTransaction(isA(Runnable.class));
        expectLastCall().andAnswer(() -> {
            final Runnable runnable = (Runnable) getCurrentArguments()[0];
            runnable.run();
            return null;
        }).anyTimes();
        // Replay
        replayAll();
        // Run test scenario
        try {
            pushNotificationSubscriptionRequestProcessingService.processPushNotificationSubscriptionRequest(requestId);
            fail("Exception should be thrown");
        } catch (final ServicesRuntimeException ex) {
            // Expected
            assertEquals(expectedExceptionMessage, ex.getCause().getMessage());
        }
        // Verify
        verifyAll();
    }

    @Test
    public void testProcessPushNotificationSubscriptionRequest() {
        // Test data
        // Create new user
        final Long userId = 5L;
        final User user = getServicesImplTestHelper().createUser();
        user.setId(userId);
        // User mobile device
        final Long userMobileDeviceId = 6L;
        final UserDevice userMobileDevice = getServicesImplTestHelper().createUserDevice();
        userMobileDevice.setId(userMobileDeviceId);
        // Create new request
        final Long requestId = 1L;
        final PushNotificationSubscriptionRequest request = getServicesImplTestHelper().createPushNotificationSubscriptionRequest();
        request.setId(requestId);
        request.setState(PushNotificationSubscriptionRequestState.CREATED);
        request.setUser(user);
        request.setUserMobileDevice(userMobileDevice);
        // Create previous request
        final Long previousRequestId = 2L;
        final PushNotificationSubscriptionRequest previousRequest = getServicesImplTestHelper().createPushNotificationSubscriptionRequest();
        previousRequest.setId(previousRequestId);
        previousRequest.setUserDeviceToken(UUID.randomUUID().toString());
        // Create previous recipient
        final Long previousRecipientId = 3L;
        final PushNotificationRecipient previousRecipient = getServicesImplTestHelper().createPushNotificationSnsRecipient();
        previousRecipient.setId(previousRecipientId);
        previousRequest.setRecipient(previousRecipient);
        // Created recipient
        final Long createdRecipientId = 4L;
        final PushNotificationRecipient createdRecipient = getServicesImplTestHelper().createPushNotificationSnsRecipient();
        createdRecipient.setId(createdRecipientId);
        // Processing parameters
        final PushNotificationSubscriptionProcessingParameters processingParameters = buildExpectedSubscriptionProcessingParameters(request, previousRecipient);
        // Reset
        resetAll();
        // Expectations
        expect(pushNotificationSubscriptionRequestService.getPushNotificationSubscriptionRequestById(eq(requestId))).andReturn(request).once();
        expect(pushNotificationSubscriptionRequestService.updatePushNotificationSubscriptionRequestState(eq(requestId), eq(PushNotificationSubscriptionRequestState.PROCESSING))).andReturn(request).once();
        expect(pushNotificationSubscriptionRequestService.checkIfPushNotificationSubscriptionRecipientExistsForUuId(eq(request.getPreviousSubscriptionRequestUuId()))).andReturn(true).once();
        expect(pushNotificationSubscriptionRequestService.getPushNotificationSubscriptionRequestByUuId(eq(request.getPreviousSubscriptionRequestUuId()))).andReturn(previousRequest).once();
        expect(pushNotificationSubscriptionProcessingService.processPushNotificationSubscriptionChange(eq(processingParameters))).andReturn(createdRecipient).once();
        expect(pushNotificationSubscriptionRequestService.updatePushNotificationSubscriptionRequestRecipient(eq(requestId), eq(createdRecipientId))).andReturn(request).once();
        expect(pushNotificationSubscriptionRequestService.updatePushNotificationSubscriptionRequestState(eq(requestId), eq(PushNotificationSubscriptionRequestState.PROCESSED))).andReturn(request).once();
        persistenceUtilityService.runInNewTransaction(isA(Runnable.class));
        expectLastCall().andAnswer(() -> {
            final Runnable runnable = (Runnable) getCurrentArguments()[0];
            runnable.run();
            return null;
        }).anyTimes();
        // Replay
        replayAll();
        // Run test scenario
        final PushNotificationRecipient result = pushNotificationSubscriptionRequestProcessingService.processPushNotificationSubscriptionRequest(requestId);
        assertNotNull(result);
        assertEquals(createdRecipient, result);
        // Verify
        verifyAll();
    }

    /* Utility methods */
    private void assertPushNotificationSubscriptionRequestInvalidStateException(final PushNotificationSubscriptionRequestInvalidStateException ex, final Long requestId, final PushNotificationSubscriptionRequestState currentState) {
        assertEquals(requestId, ex.getRequestId());
        Assert.assertEquals(currentState, ex.getCurrentState());
        assertNotNull(ex.getRequiredStates());
        assertEquals(2, ex.getRequiredStates().size());
        assertTrue(ex.getRequiredStates().contains(PushNotificationSubscriptionRequestState.CREATED));
        assertTrue(ex.getRequiredStates().contains(PushNotificationSubscriptionRequestState.FAILED));
    }

    private PushNotificationSubscriptionProcessingParameters buildExpectedSubscriptionProcessingParameters(final PushNotificationSubscriptionRequest request, final PushNotificationRecipient previousRecipient) {
        // Build processing parameters
        final PushNotificationSubscriptionProcessingParameters parameters = new PushNotificationSubscriptionProcessingParameters();
        parameters.setDeviceToken(request.getUserDeviceToken());
        parameters.setSubscribe(request.isSubscribe());
        parameters.setUserMobileDeviceId(request.getUserMobileDevice().getId());
        parameters.setUserId(request.getUser().getId());
        parameters.setApplicationType(request.getApplicationType());
        if (previousRecipient != null) {
            parameters.setCurrentPushNotificationProviderType(previousRecipient.getType());
            parameters.setCurrentProviderToken(previousRecipient.getDestinationRouteToken());
        }
        parameters.setPushNotificationProviderType(PushNotificationProviderType.SNS);
        // Return parameters
        return parameters;
    }

}
