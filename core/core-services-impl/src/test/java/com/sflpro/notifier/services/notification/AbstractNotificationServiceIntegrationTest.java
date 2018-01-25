package com.sflpro.notifier.services.notification;

import com.sflpro.notifier.db.entities.notification.Notification;
import com.sflpro.notifier.db.entities.notification.NotificationState;
import com.sflpro.notifier.services.test.AbstractServiceIntegrationTest;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * User: Ruben Dilanyan
 * Company: SFL LLC
 * Date: 3/21/15
 * Time: 8:35 PM
 */
@Ignore
public abstract class AbstractNotificationServiceIntegrationTest<T extends Notification> extends AbstractServiceIntegrationTest {


    /* Constructors */
    public AbstractNotificationServiceIntegrationTest() {
    }

    /* Test methods */
    @Test
    public void testUpdateProviderExternalUuid() {
        // Prepare data
        final String externalUuid = "{)&T)^&IGCTCUDYXYED:UFTCFUXU";
        T notification = getInstance();
        assertNull(notification.getProviderExternalUuId());
        flushAndClear();
        // Update notification uuid
        notification = getService().updateProviderExternalUuid(notification.getId(), externalUuid);
        assertNotNull(notification);
        assertEquals(externalUuid, notification.getProviderExternalUuId());
        // Flush, clear, reload and assert
        flushAndClear();
        notification = getService().getNotificationById(notification.getId());
        assertEquals(externalUuid, notification.getProviderExternalUuId());
    }

    // TODO fix this test failing @mikron
    @Ignore
    @Test
    public void testGetNotificationById() {
        // Prepare data
        final T notification = getInstance();
        // Load and assert
        T result = getService().getNotificationById(notification.getId());
        assertEquals(notification, result);
        // Flush, clear, reload and assert again
        flushAndClear();
        result = getService().getNotificationById(notification.getId());
        assertEquals(notification, result);
    }

    @Test
    public void testUpdateNotificationState() {
        // Prepare data
        T notification = getInstance();
        Assert.assertEquals(NotificationState.CREATED, notification.getState());
        final NotificationState newState = NotificationState.PROCESSING;
        flushAndClear();
        // Update notification state
        notification = getService().updateNotificationState(notification.getId(), newState);
        assertEquals(newState, notification.getState());
        // Flush, clear, reload and assert
        flushAndClear();
        notification = getService().getNotificationById(notification.getId());
        assertEquals(newState, notification.getState());
    }

    /* Abstract methods */
    protected abstract AbstractNotificationService<T> getService();

    protected abstract T getInstance();

    protected abstract Class<T> getInstanceClass();
}
