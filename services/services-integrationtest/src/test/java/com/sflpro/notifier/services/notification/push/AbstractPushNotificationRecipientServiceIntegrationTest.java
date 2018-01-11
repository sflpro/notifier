package com.sflpro.notifier.services.notification.push;

import com.sflpro.notifier.services.device.dto.UserDeviceDto;
import com.sflpro.notifier.services.device.model.UserDevice;
import com.sflpro.notifier.services.notification.dto.push.PushNotificationRecipientDto;
import com.sflpro.notifier.services.notification.model.push.PushNotificationRecipient;
import com.sflpro.notifier.services.notification.model.push.PushNotificationRecipientStatus;
import com.sflpro.notifier.services.notification.model.push.PushNotificationSubscription;
import com.sflpro.notifier.services.test.AbstractServiceIntegrationTest;
import com.sflpro.notifier.services.user.model.User;
import com.sflpro.notifier.services.util.mutable.MutableHolder;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * User: Ruben Dilanyan
 * Company: SFL LLC
 * Date: 8/13/15
 * Time: 1:36 PM
 */
@Ignore
public abstract class AbstractPushNotificationRecipientServiceIntegrationTest<T extends PushNotificationRecipient, K extends PushNotificationRecipientDto> extends AbstractServiceIntegrationTest {

    /* Constructors */
    public AbstractPushNotificationRecipientServiceIntegrationTest() {
    }

    /* Test methods */
    @Test
    public void testUpdatePushNotificationRecipientStatus() {
        // Prepare data
        T recipient = getInstance();
        Assert.assertEquals(PushNotificationRecipientStatus.ENABLED, recipient.getStatus());
        final PushNotificationRecipientStatus newStatus = PushNotificationRecipientStatus.DISABLED;
        flushAndClear();
        // Update status
        recipient = getService().updatePushNotificationRecipientStatus(recipient.getId(), newStatus);
        assertEquals(newStatus, recipient.getStatus());
        // Flush, clear, reload and assert
        flushAndClear();
        recipient = getService().getPushNotificationRecipientById(recipient.getId());
        assertEquals(newStatus, recipient.getStatus());
    }

    @Test
    public void testUpdatePushNotificationRecipientUserDevice() {
        // Prepare data
        final User customer = getServicesTestHelper().createUser();
        T recipient = getInstance(customer);
        assertNull(recipient.getLastDevice());
        assertEquals(0, recipient.getDevices().size());
        // Create user device DTO
        final UserDeviceDto deviceDto = getServicesTestHelper().createUserDeviceDto();
        final UserDevice userMobileDevice = getServicesTestHelper().createUserDevice(customer, deviceDto);
        flushAndClear();
        // Associate user device with recipient
        recipient = getService().updatePushNotificationRecipientUserDevice(recipient.getId(), userMobileDevice.getId());
        assertNotNull(recipient.getLastDevice());
        assertEquals(1, recipient.getDevices().size());
        assertRecipientIsAssociatedWithDevice(userMobileDevice, recipient);
        // Flush, clear , reload and assert
        flushAndClear();
        recipient = getService().getPushNotificationRecipientById(recipient.getId());
        assertNotNull(recipient.getLastDevice());
        assertEquals(1, recipient.getDevices().size());
        assertRecipientIsAssociatedWithDevice(userMobileDevice, recipient);
    }

    // TODO fix this test failing @mikron
    @Ignore
    @Test
    public void testGetPushNotificationRecipientById() {
        // Prepare data
        final T recipient = getInstance();
        // Load recipient by id and assert
        T result = getService().getPushNotificationRecipientById(recipient.getId());
        assertEquals(recipient, result);
        // Flush, clear, reload and assert again
        flushAndClear();
        result = getService().getPushNotificationRecipientById(recipient.getId());
        assertEquals(recipient, result);
    }

    /* Abstract methods */
    protected abstract AbstractPushNotificationRecipientService<T> getService();

    protected abstract T getInstance();

    protected abstract T getInstance(final User user);

    protected abstract T getInstance(final PushNotificationSubscription subscription, final K recipientDto);

    protected abstract K getInstanceDto();

    protected abstract Class<T> getInstanceClass();


    /* Utility methods */
    private void assertRecipientIsAssociatedWithDevice(final UserDevice device, final T recipient) {
        final MutableHolder<Boolean> mutableHolder = new MutableHolder<>(Boolean.FALSE);
        recipient.getDevices().forEach(recipientDevice -> {
            if (device.getId().equals(recipientDevice.getDevice().getId())) {
                mutableHolder.setValue(Boolean.TRUE);
            }
        });
        assertTrue(mutableHolder.getValue().booleanValue());
    }
}
