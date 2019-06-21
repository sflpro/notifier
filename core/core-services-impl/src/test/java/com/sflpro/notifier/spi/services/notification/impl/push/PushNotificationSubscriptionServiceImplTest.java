package com.sflpro.notifier.services.notification.impl.push;

import com.sflpro.notifier.db.entities.notification.push.PushNotificationSubscription;
import com.sflpro.notifier.db.entities.user.User;
import com.sflpro.notifier.db.repositories.repositories.notification.push.PushNotificationSubscriptionRepository;
import com.sflpro.notifier.services.notification.dto.push.PushNotificationSubscriptionDto;
import com.sflpro.notifier.services.notification.exception.push.PushNotificationSubscriptionAlreadyExistsForUserException;
import com.sflpro.notifier.services.notification.exception.push.PushNotificationSubscriptionNotFoundForIdException;
import com.sflpro.notifier.services.notification.exception.push.PushNotificationSubscriptionNotFoundForUserException;
import com.sflpro.notifier.services.test.AbstractServicesUnitTest;
import com.sflpro.notifier.services.user.UserService;
import org.easymock.Mock;
import org.easymock.TestSubject;
import org.junit.Assert;
import org.junit.Test;

import java.util.Optional;

import static org.easymock.EasyMock.*;
import static org.junit.Assert.*;

/**
 * User: Ruben Dilanyan
 * Company: SFL LLC
 * Date: 8/13/15
 * Time: 10:29 AM
 */
public class PushNotificationSubscriptionServiceImplTest extends AbstractServicesUnitTest {

    /* Test subject and mocks */
    @TestSubject
    private PushNotificationSubscriptionServiceImpl pushNotificationSubscriptionService = new PushNotificationSubscriptionServiceImpl();

    @Mock
    private PushNotificationSubscriptionRepository pushNotificationSubscriptionRepository;

    @Mock
    private UserService userService;

    /* Constructors */
    public PushNotificationSubscriptionServiceImplTest() {
    }

    /* Test methods */
    @Test
    public void testGetPushNotificationSubscriptionForUserWithInvalidArguments() {
        // Reset
        resetAll();
        // Replay
        replayAll();
        // Run test scenario
        try {
            pushNotificationSubscriptionService.getPushNotificationSubscriptionForUser(null);
            fail("Exception should be thrown");
        } catch (final IllegalArgumentException ex) {
            // Expected
        }
        // Verify
        verifyAll();
    }

    @Test
    public void testGetPushNotificationSubscriptionForUserWhenNoSubscriptionExists() {
        // Test data
        final Long userId = 1L;
        final User user = getServicesImplTestHelper().createUser();
        user.setId(userId);
        // Reset
        resetAll();
        // Expectations
        expect(userService.getUserById(eq(userId))).andReturn(user).once();
        expect(pushNotificationSubscriptionRepository.findByUser(eq(user))).andReturn(null).once();
        // Replay
        replayAll();
        // Run test scenario
        try {
            pushNotificationSubscriptionService.getPushNotificationSubscriptionForUser(userId);
            fail("Exception should be thrown");
        } catch (final PushNotificationSubscriptionNotFoundForUserException ex) {
            // Expected
            assertPushNotificationSubscriptionNotFoundForUserException(ex, userId);
        }
        // Verify
        verifyAll();
    }

    @Test
    public void testGetPushNotificationSubscriptionForUser() {
        // Test data
        final Long userId = 1L;
        final User user = getServicesImplTestHelper().createUser();
        user.setId(userId);
        final Long subscriptionId = 2L;
        final PushNotificationSubscription subscription = getServicesImplTestHelper().createPushNotificationSubscription();
        subscription.setId(subscriptionId);
        // Reset
        resetAll();
        // Expectations
        expect(userService.getUserById(eq(userId))).andReturn(user).once();
        expect(pushNotificationSubscriptionRepository.findByUser(eq(user))).andReturn(subscription).once();
        // Replay
        replayAll();
        // Run test scenario
        final PushNotificationSubscription result = pushNotificationSubscriptionService.getPushNotificationSubscriptionForUser(userId);
        assertEquals(subscription, result);
        // Verify
        verifyAll();
    }

    @Test
    public void testCheckIfPushNotificationSubscriptionExistsForUserWithInvalidArguments() {
        // Reset
        resetAll();
        // Replay
        replayAll();
        // Run test scenario
        try {
            pushNotificationSubscriptionService.checkIfPushNotificationSubscriptionExistsForUser(null);
            fail("Exception should be thrown");
        } catch (final IllegalArgumentException ex) {
            // Expected
        }
        // Verify
        verifyAll();
    }

    @Test
    public void testCheckIfPushNotificationSubscriptionExistsForUserWhenItDoesNotExistYet() {
        // Test data
        final Long userId = 1L;
        final User user = getServicesImplTestHelper().createUser();
        user.setId(userId);
        // Reset
        resetAll();
        // Expectations
        expect(userService.getUserById(eq(userId))).andReturn(user).once();
        expect(pushNotificationSubscriptionRepository.findByUser(eq(user))).andReturn(null).once();
        // Replay
        replayAll();
        // Run test scenario
        final boolean result = pushNotificationSubscriptionService.checkIfPushNotificationSubscriptionExistsForUser(userId);
        assertFalse(result);
        // Verify
        verifyAll();
    }

    @Test
    public void testCheckIfPushNotificationSubscriptionExistsForUserWhenItAlreadyExists() {
        // Test data
        final Long userId = 1L;
        final User user = getServicesImplTestHelper().createUser();
        user.setId(userId);
        final Long subscriptionId = 2L;
        final PushNotificationSubscription subscription = getServicesImplTestHelper().createPushNotificationSubscription();
        subscription.setId(subscriptionId);
        // Reset
        resetAll();
        // Expectations
        expect(userService.getUserById(eq(userId))).andReturn(user).once();
        expect(pushNotificationSubscriptionRepository.findByUser(eq(user))).andReturn(subscription).once();
        // Replay
        replayAll();
        // Run test scenario
        final boolean result = pushNotificationSubscriptionService.checkIfPushNotificationSubscriptionExistsForUser(userId);
        assertTrue(result);
        // Verify
        verifyAll();
    }

    @Test
    public void testGetPushNotificationSubscriptionByIdWithInvalidArguments() {
        // Reset
        resetAll();
        // Replay
        replayAll();
        // Run test scenario
        try {
            pushNotificationSubscriptionService.getPushNotificationSubscriptionById(null);
            fail("Exception should be thrown");
        } catch (final IllegalArgumentException ex) {
            // Expected
        }
        // Verify
        verifyAll();
    }

    @Test
    public void testGetPushNotificationSubscriptionByIdWithNotExistingId() {
        // Test data
        final Long subscriptionId = 1L;
        // Reset
        resetAll();
        // Expectations
        expect(pushNotificationSubscriptionRepository.findById(eq(subscriptionId))).andReturn(Optional.empty()).once();
        // Replay
        replayAll();
        // Run test scenario
        try {
            pushNotificationSubscriptionService.getPushNotificationSubscriptionById(subscriptionId);
            fail("Exception should be thrown");
        } catch (final PushNotificationSubscriptionNotFoundForIdException ex) {
            // Expected
            assertPushNotificationSubscriptionNotFoundForIdException(ex, subscriptionId);
        }
        // Verify
        verifyAll();
    }

    @Test
    public void testGetPushNotificationSubscriptionById() {
        // Test data
        final Long subscriptionId = 1L;
        final PushNotificationSubscription subscription = getServicesImplTestHelper().createPushNotificationSubscription();
        subscription.setId(subscriptionId);
        // Reset
        resetAll();
        // Expectations
        expect(pushNotificationSubscriptionRepository.findById(eq(subscriptionId))).andReturn(Optional.of(subscription)).once();
        // Replay
        replayAll();
        // Run test scenario
        final PushNotificationSubscription result = pushNotificationSubscriptionService.getPushNotificationSubscriptionById(subscriptionId);
        assertEquals(subscription, result);
        // Verify
        verifyAll();
    }

    @Test
    public void testCreatePushNotificationSubscriptionWithInvalidArguments() {
        // Test data
        final Long userId = 1L;
        final PushNotificationSubscriptionDto subscriptionDto = getServicesImplTestHelper().createPushNotificationSubscriptionDto();
        // Reset
        resetAll();
        // Replay
        replayAll();
        // Run test scenario
        try {
            pushNotificationSubscriptionService.createPushNotificationSubscription(null, subscriptionDto);
            fail("Exception should be thrown");
        } catch (final IllegalArgumentException ex) {
            // Expected
        }
        try {
            pushNotificationSubscriptionService.createPushNotificationSubscription(userId, null);
            fail("Exception should be thrown");
        } catch (final IllegalArgumentException ex) {
            // Expected
        }
        // Verify
        verifyAll();
    }

    @Test
    public void testCreatePushNotificationSubscriptionWhenUserAlreadyHasOne() {
        // Test data
        final Long userId = 1L;
        final User user = getServicesImplTestHelper().createUser();
        user.setId(userId);
        final PushNotificationSubscriptionDto subscriptionDto = getServicesImplTestHelper().createPushNotificationSubscriptionDto();
        final Long existingSubscriptionId = 2L;
        final PushNotificationSubscription existingSubscription = getServicesImplTestHelper().createPushNotificationSubscription();
        existingSubscription.setId(existingSubscriptionId);
        // Reset
        resetAll();
        // Expectations
        expect(userService.getUserById(eq(userId))).andReturn(user).once();
        expect(pushNotificationSubscriptionRepository.findByUser(eq(user))).andReturn(existingSubscription).once();
        // Replay
        replayAll();
        // Run test scenario
        try {
            pushNotificationSubscriptionService.createPushNotificationSubscription(userId, subscriptionDto);
            fail("Exception should be thrown");
        } catch (final PushNotificationSubscriptionAlreadyExistsForUserException ex) {
            // Expected
            assertPushNotificationSubscriptionAlreadyExistsForUserException(ex, userId, existingSubscriptionId);
        }
        // Verify
        verifyAll();
    }

    @Test
    public void testCreatePushNotificationSubscription() {
        // Test data
        final Long userId = 1L;
        final User user = getServicesImplTestHelper().createUser();
        user.setId(userId);
        final PushNotificationSubscriptionDto subscriptionDto = getServicesImplTestHelper().createPushNotificationSubscriptionDto();
        // Reset
        resetAll();
        // Expectations
        expect(userService.getUserById(eq(userId))).andReturn(user).once();
        expect(pushNotificationSubscriptionRepository.findByUser(eq(user))).andReturn(null).once();
        expect(pushNotificationSubscriptionRepository.save(isA(PushNotificationSubscription.class))).andAnswer(() -> (PushNotificationSubscription) getCurrentArguments()[0]).once();
        // Replay
        replayAll();
        // Run test scenario
        final PushNotificationSubscription result = pushNotificationSubscriptionService.createPushNotificationSubscription(userId, subscriptionDto);
        getServicesImplTestHelper().assertPushNotificationSubscription(result);
        Assert.assertEquals(user, result.getUser());
        // Verify
        verifyAll();
    }

    /* Utility methods */
    private void assertPushNotificationSubscriptionNotFoundForUserException(final PushNotificationSubscriptionNotFoundForUserException ex, final Long userId) {
        assertEquals(userId, ex.getUserId());
    }

    private void assertPushNotificationSubscriptionNotFoundForIdException(final PushNotificationSubscriptionNotFoundForIdException ex, final Long id) {
        Assert.assertEquals(id, ex.getId());
    }

    private void assertPushNotificationSubscriptionAlreadyExistsForUserException(final PushNotificationSubscriptionAlreadyExistsForUserException ex, final Long userId, final Long existingSubscriptionId) {
        assertEquals(userId, ex.getUserId());
        assertEquals(existingSubscriptionId, ex.getExistingSubscriptionId());
    }
}
