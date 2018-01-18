package com.sflpro.notifier.services.notification.push;

import com.sflpro.notifier.db.entities.user.User;
import com.sflpro.notifier.services.notification.dto.push.PushNotificationSubscriptionDto;
import com.sflpro.notifier.services.notification.exception.push.PushNotificationSubscriptionAlreadyExistsForUserException;
import com.sflpro.notifier.db.entities.notification.push.PushNotificationSubscription;
import com.sflpro.notifier.services.test.AbstractServiceIntegrationTest;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.*;

/**
 * User: Ruben Dilanyan
 * Company: SFL LLC
 * Date: 8/13/15
 * Time: 11:18 AM
 */
public class PushNotificationSubscriptionServiceIntegrationTest extends AbstractServiceIntegrationTest {

    /* Dependencies */
    @Autowired
    private PushNotificationSubscriptionService pushNotificationSubscriptionService;

    /* Constructors */
    public PushNotificationSubscriptionServiceIntegrationTest() {
    }

    /* Test methods */
    @Test
    public void testGetPushNotificationSubscriptionForUser() {
        // Prepare data
        final PushNotificationSubscription subscription = getServicesTestHelper().createPushNotificationSubscription();
        // Try to load subscription for user
        PushNotificationSubscription result = pushNotificationSubscriptionService.getPushNotificationSubscriptionForUser(subscription.getUser().getId());
        assertEquals(subscription, result);
        // Flush, clear, reload and assert
        flushAndClear();
        result = pushNotificationSubscriptionService.getPushNotificationSubscriptionForUser(subscription.getUser().getId());
        assertEquals(subscription, result);
    }

    @Test
    public void testCheckIfPushNotificationSubscriptionExistsForUser() {
        // Prepare data
        final User user = getServicesTestHelper().createUser();
        flushAndClear();
        // Check if subscription exists for user
        boolean result = pushNotificationSubscriptionService.checkIfPushNotificationSubscriptionExistsForUser(user.getId());
        assertFalse(result);
        // Create subscription
        final PushNotificationSubscription subscription = pushNotificationSubscriptionService.createPushNotificationSubscription(user.getId(), getServicesTestHelper().createPushNotificationSubscriptionDto());
        assertNotNull(subscription);
        result = pushNotificationSubscriptionService.checkIfPushNotificationSubscriptionExistsForUser(user.getId());
        assertTrue(result);
        // Flush, clear and assert again
        flushAndClear();
        result = pushNotificationSubscriptionService.checkIfPushNotificationSubscriptionExistsForUser(user.getId());
        assertTrue(result);
    }

    @Test
    public void testGetPushNotificationSubscriptionById() {
        // Prepare data
        final PushNotificationSubscription subscription = getServicesTestHelper().createPushNotificationSubscription();
        // Load push notification and assert
        PushNotificationSubscription result = pushNotificationSubscriptionService.getPushNotificationSubscriptionById(subscription.getId());
        assertEquals(subscription, result);
        // Flush, clear, reload and assert
        flushAndClear();
        result = pushNotificationSubscriptionService.getPushNotificationSubscriptionById(subscription.getId());
        assertEquals(subscription, result);
    }

    @Test
    public void testCreatePushNotificationSubscription() {
        // Prepare data
        final PushNotificationSubscriptionDto subscriptionDto = getServicesTestHelper().createPushNotificationSubscriptionDto();
        final User user = getServicesTestHelper().createUser();
        flushAndClear();
        // Create push notification subscription
        PushNotificationSubscription subscription = pushNotificationSubscriptionService.createPushNotificationSubscription(user.getId(), subscriptionDto);
        assertPushNotificationSubscription(subscription, subscriptionDto, user);
        // Flush, clear, reload and assert
        flushAndClear();
        subscription = pushNotificationSubscriptionService.getPushNotificationSubscriptionById(subscription.getId());
        assertPushNotificationSubscription(subscription, subscriptionDto, user);
    }

    @Test
    public void testCreatePushNotificationSubscriptionWhenUserAlreadyHasOne() {
        // Prepare data
        final PushNotificationSubscriptionDto subscriptionDto = getServicesTestHelper().createPushNotificationSubscriptionDto();
        final User user = getServicesTestHelper().createUser();
        flushAndClear();
        // Create push notification subscription
        PushNotificationSubscription subscription = pushNotificationSubscriptionService.createPushNotificationSubscription(user.getId(), subscriptionDto);
        assertNotNull(subscription);
        try {
            pushNotificationSubscriptionService.createPushNotificationSubscription(user.getId(), subscriptionDto);
            fail("Exception should be thrown");
        } catch (final PushNotificationSubscriptionAlreadyExistsForUserException ex) {
            // Expected
        }
        // Flush, clear, reload and assert
        flushAndClear();
        try {
            pushNotificationSubscriptionService.createPushNotificationSubscription(user.getId(), subscriptionDto);
            fail("Exception should be thrown");
        } catch (final PushNotificationSubscriptionAlreadyExistsForUserException ex) {
            // Expected
        }
    }

    /* Utility methods */
    private void assertPushNotificationSubscription(final PushNotificationSubscription subscription, final PushNotificationSubscriptionDto subscriptionDto, final User user) {
        getServicesTestHelper().assertPushNotificationSubscription(subscription, subscriptionDto);
        assertNotNull(subscription.getUser().getId());
        Assert.assertEquals(user.getId(), subscription.getUser().getId());
    }
}
