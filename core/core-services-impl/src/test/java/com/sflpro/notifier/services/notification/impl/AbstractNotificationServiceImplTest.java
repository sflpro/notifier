package com.sflpro.notifier.services.notification.impl;

import com.sflpro.notifier.db.entities.notification.Notification;
import com.sflpro.notifier.db.entities.notification.NotificationState;
import com.sflpro.notifier.db.repositories.repositories.notification.AbstractNotificationRepository;
import com.sflpro.notifier.services.notification.exception.NotificationNotFoundForIdException;
import com.sflpro.notifier.services.test.AbstractServicesUnitTest;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.easymock.EasyMock.*;
import static org.junit.Assert.*;

/**
 * User: Ruben Dilanyan
 * Company: SFL LLC
 * Date: 3/21/15
 * Time: 8:25 PM
 */
@Ignore
public abstract class AbstractNotificationServiceImplTest<T extends Notification> extends AbstractServicesUnitTest {

    /* Constructors */
    public AbstractNotificationServiceImplTest() {
    }

    /* Test methods */
    @Test
    public void testUpdateProviderExternalUuidWithInvalidArguments() {
        // Test data
        final Long notificationId = 1L;
        final String providerExternalUuId = "KUYFTIRUR^^GCKVLV:LYC^r8r58GCFJG";
        // Reset
        resetAll();
        // Replay
        replayAll();
        // Run test scenario
        try {
            getService().updateProviderExternalUuid(null, providerExternalUuId);
            fail("Exception should be thrown");
        } catch (final IllegalArgumentException ex) {
            // Expected
        }
        try {
            getService().updateProviderExternalUuid(notificationId, null);
            fail("Exception should be thrown");
        } catch (final IllegalArgumentException ex) {
            // Expected
        }
        // Verify
        verifyAll();
    }

    @Test
    public void testUpdateProviderExternalUuidWithNotExistingId() {
        // Test data
        final Long notificationId = 1L;
        final String providerExternalUuId = "KUYFTIRUR^^GCKVLV:LYC^r8r58GCFJG";
        // Reset
        resetAll();
        // Expectations
        expect(getRepository().findById(eq(notificationId))).andReturn(Optional.empty()).once();
        // Replay
        replayAll();
        // Run test scenario
        try {
            getService().updateProviderExternalUuid(notificationId, providerExternalUuId);
            fail("Exception should be thrown");
        } catch (final NotificationNotFoundForIdException ex) {
            // Expected
            assertNotificationNotFoundForIdException(ex, notificationId);
        }
        // Verify
        verifyAll();
    }

    @Test
    public void testUpdateProviderExternalUuid() {
        // Test data
        final Long notificationId = 1L;
        final T notification = getInstance();
        notification.setId(notificationId);
        final String providerExternalUuId = "KUYFTIRUR^^GCKVLV:LYC^r8r58GCFJG";
        // Reset
        resetAll();
        // Expectations
        expect(getRepository().findById(eq(notificationId))).andReturn(Optional.of(notification)).once();
        expect(getRepository().save(isA(getInstanceClass()))).andAnswer(() -> (T) getCurrentArguments()[0]).once();
        // Replay
        replayAll();
        // Run test scenario
        final T result = getService().updateProviderExternalUuid(notificationId, providerExternalUuId);
        assertNotNull(result);
        assertEquals(providerExternalUuId, result.getProviderExternalUuId());
        // Verify
        verifyAll();
    }

    @Test
    public void testUpdateNotificationStateWithInvalidArguments() {
        // Test data
        final Long notificationId = 1L;
        final NotificationState state = NotificationState.PROCESSING;
        // Reset
        resetAll();
        // Replay
        replayAll();
        // Run test scenario
        try {
            getService().updateNotificationState(null, state);
            fail("Exception should be thrown");
        } catch (final IllegalArgumentException ex) {
            // Expected
        }
        try {
            getService().updateNotificationState(notificationId, null);
            fail("Exception should be thrown");
        } catch (final IllegalArgumentException ex) {
            // Expected
        }
        // Verify
        verifyAll();
    }

    @Test
    public void testUpdateNotificationStateWithNotExistingId() {
        // Test data
        final Long notificationId = 1L;
        final NotificationState state = NotificationState.PROCESSING;
        // Reset
        resetAll();
        // Expectations
        expect(getRepository().findById(eq(notificationId))).andReturn(Optional.empty()).once();
        // Replay
        replayAll();
        // Run test scenario
        try {
            getService().updateNotificationState(notificationId, state);
            fail("Exception should be thrown");
        } catch (final NotificationNotFoundForIdException ex) {
            // Expected
            assertNotificationNotFoundForIdException(ex, notificationId);
        }
        // Verify
        verifyAll();
    }

    @Test
    public void testUpdateNotificationState() {
        // Test data
        final Long notificationId = 1L;
        final T notification = getInstance();
        notification.setId(notificationId);
        final LocalDateTime notificationUpdated = notification.getUpdated();
        final NotificationState state = NotificationState.PROCESSING;
        // Reset
        resetAll();
        // Expectations
        expect(getRepository().findById(eq(notificationId))).andReturn(Optional.of(notification)).once();
        expect(getRepository().save(isA(getInstanceClass()))).andAnswer(() -> (T) getCurrentArguments()[0]).once();
        // Replay
        replayAll();
        // Run test scenario
        final T result = getService().updateNotificationState(notificationId, state);
        assertEquals(state, result.getState());
        assertTrue(result.getUpdated().compareTo(notificationUpdated) >= 0 && notificationUpdated != result.getUpdated());
        // Verify
        verifyAll();
    }

    @Test
    public void getNotificationByIdWithInvalidArguments() {
        // Reset
        resetAll();
        // Replay
        replayAll();
        // Run test scenario
        try {
            getService().getNotificationById(null);
            fail("Exception should be thrown");
        } catch (final IllegalArgumentException ex) {
            // Expected
        }
        // Verify
        verifyAll();
    }

    @Test
    public void getNotificationByIdWithNotExistingId() {
        // Test data
        final Long notificationId = 1L;
        // Reset
        resetAll();
        // Expectations
        expect(getRepository().findById(eq(notificationId))).andReturn(Optional.empty()).once();
        // Replay
        replayAll();
        // Run test scenario
        try {
            getService().getNotificationById(notificationId);
            fail("Exception should be thrown");
        } catch (final NotificationNotFoundForIdException ex) {
            // Expected
            assertNotificationNotFoundForIdException(ex, notificationId);
        }
        // Verify
        verifyAll();
    }

    @Test
    public void getNotificationById() {
        // Test data
        final Long notificationId = 1L;
        final T notification = getInstance();
        // Reset
        resetAll();
        // Expectations
        expect(getRepository().findById(eq(notificationId))).andReturn(Optional.of(notification)).once();
        // Replay
        replayAll();
        // Run test scenario
        final T result = getService().getNotificationById(notificationId);
        assertEquals(notification, result);
        // Verify
        verifyAll();
    }

    /* Abstract methods */
    protected abstract AbstractNotificationRepository<T> getRepository();

    protected abstract AbstractNotificationServiceImpl<T> getService();

    protected abstract Class<T> getInstanceClass();

    protected abstract T getInstance();

    /* Utility methods */
    protected void assertNotificationNotFoundForIdException(final NotificationNotFoundForIdException ex, final Long id) {
        Assert.assertEquals(id, ex.getId());
        Assert.assertEquals(getInstanceClass(), ex.getEntityClass());
    }
}
