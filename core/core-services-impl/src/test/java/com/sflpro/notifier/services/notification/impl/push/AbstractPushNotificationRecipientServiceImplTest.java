package com.sflpro.notifier.services.notification.impl.push;

import com.sflpro.notifier.db.entities.device.UserDevice;
import com.sflpro.notifier.db.entities.notification.push.*;
import com.sflpro.notifier.db.entities.user.User;
import com.sflpro.notifier.db.repositories.repositories.notification.push.AbstractPushNotificationRecipientRepository;
import com.sflpro.notifier.db.repositories.repositories.notification.push.PushNotificationRecipientDeviceRepository;
import com.sflpro.notifier.db.repositories.repositories.notification.push.PushNotificationRecipientRepository;
import com.sflpro.notifier.services.device.UserDeviceService;
import com.sflpro.notifier.services.notification.exception.push.PushNotificationRecipientAlreadyExistsException;
import com.sflpro.notifier.services.notification.exception.push.PushNotificationRecipientInvalidDeviceUserException;
import com.sflpro.notifier.services.notification.exception.push.PushNotificationRecipientNotFoundForIdException;
import com.sflpro.notifier.services.notification.exception.push.PushNotificationRecipientNotFoundForLookupParametersException;
import com.sflpro.notifier.services.notification.push.PushNotificationSubscriptionService;
import com.sflpro.notifier.services.test.AbstractServicesUnitTest;
import com.sflpro.notifier.services.util.mutable.MutableHolder;
import org.easymock.Mock;
import org.junit.Assert;
import org.junit.Test;

import java.util.Date;
import java.util.Optional;

import static org.easymock.EasyMock.*;
import static org.junit.Assert.*;

/**
 * User: Ruben Dilanyan
 * Company: SFL LLC
 * Date: 8/13/15
 * Time: 1:26 PM
 */
public abstract class AbstractPushNotificationRecipientServiceImplTest<T extends PushNotificationRecipient> extends AbstractServicesUnitTest {

    /* Mocks */
    @Mock
    private PushNotificationSubscriptionService pushNotificationSubscriptionService;

    @Mock
    private PushNotificationRecipientRepository pushNotificationRecipientRepository;

    @Mock
    private PushNotificationRecipientDeviceRepository pushNotificationRecipientDeviceRepository;

    @Mock
    private UserDeviceService userDeviceService;

    /* Constructors */
    public AbstractPushNotificationRecipientServiceImplTest() {
    }

    /* Test methods */
    @Test
    public void testUpdatePushNotificationRecipientStatusWithInvalidArguments() {
        // Test data
        final Long recipientId = 1L;
        final PushNotificationRecipientStatus status = PushNotificationRecipientStatus.DISABLED;
        // Reset
        resetAll();
        // Replay
        replayAll();
        // Run test scenario
        try {
            getService().updatePushNotificationRecipientStatus(null, status);
            fail("Exception should be thrown");
        } catch (final IllegalArgumentException ex) {
            // Expected
        }
        try {
            getService().updatePushNotificationRecipientStatus(recipientId, null);
            fail("Exception should be thrown");
        } catch (final IllegalArgumentException ex) {
            // Expected
        }
        // Verify
        verifyAll();
    }

    @Test
    public void testUpdatePushNotificationRecipientStatusWithNotExistingRecipientId() {
        // Test data
        final Long recipientId = 1L;
        final PushNotificationRecipientStatus status = PushNotificationRecipientStatus.DISABLED;
        // Reset
        resetAll();
        // Expectations
        expect(getRepository().findById(eq(recipientId))).andReturn(Optional.empty()).once();
        // Replay
        replayAll();
        // Run test scenario
        try {
            getService().updatePushNotificationRecipientStatus(recipientId, status);
            fail("Exception should be thrown");
        } catch (final PushNotificationRecipientNotFoundForIdException ex) {
            // Expected
            assertPushNotificationRecipientNotFoundForIdException(ex, recipientId);
        }
        // Verify
        verifyAll();
    }

    @Test
    public void testUpdatePushNotificationRecipientStatus() {
        // Test data
        final Long recipientId = 1L;
        final T recipient = getInstance();
        recipient.setStatus(PushNotificationRecipientStatus.ENABLED);
        final Date recipientUpdateDate = recipient.getUpdated();
        final PushNotificationRecipientStatus status = PushNotificationRecipientStatus.DISABLED;
        // Reset
        resetAll();
        // Expectations
        expect(getRepository().findById(eq(recipientId))).andReturn(Optional.of(recipient)).once();
        expect(getRepository().save(isA(getInstanceClass()))).andAnswer(() -> (T) getCurrentArguments()[0]).once();
        // Replay
        replayAll();
        // Run test scenario
        final T result = getService().updatePushNotificationRecipientStatus(recipientId, status);
        assertEquals(status, result.getStatus());
        assertTrue(result.getUpdated().compareTo(recipientUpdateDate) >= 0 && recipientUpdateDate != result.getUpdated());
        // Verify
        verifyAll();
    }

    @Test
    public void testUpdatePushNotificationRecipientUserDeviceWithInvalidArguments() {
        // Test data
        final Long recipientId = 1L;
        final Long userDeviceId = 2L;
        // Reset
        resetAll();
        // Replay
        replayAll();
        // Run test scenario
        try {
            getService().updatePushNotificationRecipientUserDevice(null, userDeviceId);
            fail("Exception should be thrown");
        } catch (final IllegalArgumentException ex) {
            // Expected
        }
        try {
            getService().updatePushNotificationRecipientUserDevice(recipientId, null);
            fail("Exception should be thrown");
        } catch (final IllegalArgumentException ex) {
            // Expected
        }
        // Verify
        verifyAll();
    }

    @Test
    public void testUpdatePushNotificationRecipientUserDeviceWithNotExistingRecipientId() {
        // Test data
        final Long recipientId = 1L;
        final Long userDeviceId = 2L;
        // Reset
        resetAll();
        // Expectations
        expect(getRepository().findById(eq(recipientId))).andReturn(Optional.empty()).once();
        // Replay
        replayAll();
        // Run test scenario
        try {
            getService().updatePushNotificationRecipientUserDevice(recipientId, userDeviceId);
            fail("Exception should be thrown");
        } catch (final PushNotificationRecipientNotFoundForIdException ex) {
            // Expected
            assertPushNotificationRecipientNotFoundForIdException(ex, recipientId);
        }
        // Verify
        verifyAll();
    }

    @Test
    public void testUpdatePushNotificationRecipientUserDeviceWhenDeviceAndRecipientHasDifferentUsers() {
        // Test data
        final Long subscriptionUserId = 3L;
        final User subscriptionUser = getServicesImplTestHelper().createUser();
        subscriptionUser.setId(subscriptionUserId);
        final Long subscriptionId = 4L;
        final PushNotificationSubscription subscription = getServicesImplTestHelper().createPushNotificationSubscription();
        subscription.setId(subscriptionId);
        subscription.setUser(subscriptionUser);
        final Long recipientId = 1L;
        final T recipient = getInstance();
        recipient.setId(recipientId);
        recipient.setSubscription(subscription);
        final Long userDeviceId = 2L;
        final UserDevice userDevice = getServicesImplTestHelper().createUserDevice();
        userDevice.setId(userDeviceId);
        final Long deviceUserId = 5L;
        final User deviceUser = getServicesImplTestHelper().createUser();
        deviceUser.setId(deviceUserId);
        userDevice.setUser(deviceUser);
        final MutableHolder<PushNotificationRecipientDevice> deviceMutableHolder = new MutableHolder<>(null);
        // Reset
        resetAll();
        // Expectations
        expect(getRepository().findById(eq(recipientId))).andReturn(Optional.of(recipient)).once();
        expect(userDeviceService.getUserDeviceById(eq(userDeviceId))).andReturn(userDevice).once();
        // Replay
        replayAll();
        // Run test scenario
        try {
            getService().updatePushNotificationRecipientUserDevice(recipientId, userDeviceId);
        } catch (final PushNotificationRecipientInvalidDeviceUserException ex) {
            // Expected
            assertPushNotificationRecipientInvalidDeviceUserException(ex, recipientId, subscriptionUserId, userDeviceId, deviceUserId);
        }
        // Verify
        verifyAll();
    }

    @Test
    public void testUpdatePushNotificationRecipientUserDeviceWhenDeviceIsNotAssociatedYet() {
        // Test data
        final Long userId = 3L;
        final User user = getServicesImplTestHelper().createUser();
        user.setId(userId);
        final Long subscriptionId = 4L;
        final PushNotificationSubscription subscription = getServicesImplTestHelper().createPushNotificationSubscription();
        subscription.setId(subscriptionId);
        subscription.setUser(user);
        final Long recipientId = 1L;
        final T recipient = getInstance();
        recipient.setId(recipientId);
        recipient.setSubscription(subscription);
        final Date recipientUpdateDate = recipient.getUpdated();
        final Long userDeviceId = 2L;
        final UserDevice userDevice = getServicesImplTestHelper().createUserDevice();
        userDevice.setId(userDeviceId);
        userDevice.setUser(user);
        final MutableHolder<PushNotificationRecipientDevice> deviceMutableHolder = new MutableHolder<>(null);
        // Reset
        resetAll();
        // Expectations
        expect(getRepository().findById(eq(recipientId))).andReturn(Optional.of(recipient)).once();
        expect(userDeviceService.getUserDeviceById(eq(userDeviceId))).andReturn(userDevice).once();
        expect(pushNotificationRecipientDeviceRepository.findByRecipientAndDevice(eq(recipient), eq(userDevice))).andReturn(null).once();
        expect(pushNotificationRecipientDeviceRepository.save(isA(PushNotificationRecipientDevice.class))).andAnswer(() -> {
            final PushNotificationRecipientDevice recipientDevice = (PushNotificationRecipientDevice) getCurrentArguments()[0];
            assertEquals(recipient, recipientDevice.getRecipient());
            Assert.assertEquals(userDevice, recipientDevice.getDevice());
            deviceMutableHolder.setValue(recipientDevice);
            return recipientDevice;
        }).once();
        expect(getRepository().save(isA(getInstanceClass()))).andAnswer(() -> (T) getCurrentArguments()[0]).once();
        // Replay
        replayAll();
        // Run test scenario
        final T result = getService().updatePushNotificationRecipientUserDevice(recipientId, userDeviceId);
        Assert.assertEquals(userDevice, result.getLastDevice());
        assertNotNull(result.getDevices());
        assertTrue(result.getDevices().contains(deviceMutableHolder.getValue()));
        assertTrue(result.getUpdated().compareTo(recipientUpdateDate) >= 0 && recipientUpdateDate != result.getUpdated());
        // Verify
        verifyAll();
    }

    @Test
    public void testUpdatePushNotificationRecipientUserDeviceWhenDeviceIsAlreadyAssociated() {
        // Test data
        final Long userId = 3L;
        final User user = getServicesImplTestHelper().createUser();
        user.setId(userId);
        final Long subscriptionId = 4L;
        final PushNotificationSubscription subscription = getServicesImplTestHelper().createPushNotificationSubscription();
        subscription.setId(subscriptionId);
        subscription.setUser(user);
        final Long recipientId = 1L;
        final T recipient = getInstance();
        recipient.setId(recipientId);
        recipient.setSubscription(subscription);
        final Date recipientUpdateDate = recipient.getUpdated();
        final Long userDeviceId = 2L;
        final UserDevice userDevice = getServicesImplTestHelper().createUserDevice();
        userDevice.setId(userDeviceId);
        userDevice.setUser(user);
        final Long recipientDeviceId = 3L;
        final PushNotificationRecipientDevice recipientDevice = new PushNotificationRecipientDevice();
        recipientDevice.setId(recipientDeviceId);
        recipientDevice.setDevice(userDevice);
        recipientDevice.setRecipient(recipient);
        // Reset
        resetAll();
        // Expectations
        expect(getRepository().findById(eq(recipientId))).andReturn(Optional.of(recipient)).once();
        expect(userDeviceService.getUserDeviceById(eq(userDeviceId))).andReturn(userDevice).once();
        expect(pushNotificationRecipientDeviceRepository.findByRecipientAndDevice(eq(recipient), eq(userDevice))).andReturn(recipientDevice).once();
        expect(getRepository().save(isA(getInstanceClass()))).andAnswer(() -> (T) getCurrentArguments()[0]).once();
        // Replay
        replayAll();
        // Run test scenario
        final T result = getService().updatePushNotificationRecipientUserDevice(recipientId, userDeviceId);
        Assert.assertEquals(userDevice, result.getLastDevice());
        assertTrue(result.getUpdated().compareTo(recipientUpdateDate) >= 0 && recipientUpdateDate != result.getUpdated());
        // Verify
        verifyAll();
    }

    @Test
    public void testGetPushNotificationRecipientByIdWithInvalidArguments() {
        // Reset
        resetAll();
        // Replay
        replayAll();
        // Run test scenario
        try {
            getService().getPushNotificationRecipientById(null);
            fail("Exception should be thrown");
        } catch (final IllegalArgumentException ex) {
            // Expected
        }
        // Verify
        verifyAll();
    }

    @Test
    public void testGetPushNotificationRecipientByIdWithNotExistingId() {
        // Test data
        final Long recipientId = 1L;
        // Reset
        resetAll();
        // Expectations
        expect(getRepository().findById(eq(recipientId))).andReturn(Optional.empty()).once();
        // Replay
        replayAll();
        // Run test scenario
        try {
            getService().getPushNotificationRecipientById(recipientId);
            fail("Exception should be thrown");
        } catch (final PushNotificationRecipientNotFoundForIdException ex) {
            // Expected
            assertPushNotificationRecipientNotFoundForIdException(ex, recipientId);
        }
        // Verify
        verifyAll();
    }

    @Test
    public void testGetPushNotificationRecipientById() {
        // Test data
        final Long recipientId = 1L;
        final T recipient = getInstance();
        recipient.setId(recipientId);
        // Reset
        resetAll();
        // Expectations
        expect(getRepository().findById(eq(recipientId))).andReturn(Optional.of(recipient)).once();
        // Replay
        replayAll();
        // Run test scenario
        final T result = getService().getPushNotificationRecipientById(recipientId);
        assertEquals(recipient, result);
        // Verify
        verifyAll();
    }

    /* Abstract methods */
    protected abstract T getInstance();

    protected abstract Class<T> getInstanceClass();

    protected abstract AbstractPushNotificationRecipientServiceImpl<T> getService();

    protected abstract AbstractPushNotificationRecipientRepository<T> getRepository();

    /* Utility methods */
    protected void assertPushNotificationRecipientNotFoundForLookupParametersException(final PushNotificationRecipientNotFoundForLookupParametersException ex, final Long subscriptionId, final PushNotificationProviderType type, final String destinationRouteToken) {
        assertEquals(subscriptionId, ex.getSubscriptionId());
        assertEquals(destinationRouteToken, ex.getDestinationRouteToken());
        Assert.assertEquals(type, ex.getType());
    }

    protected void assertPushNotificationRecipientNotFoundForIdException(final PushNotificationRecipientNotFoundForIdException ex, final Long id) {
        Assert.assertEquals(id, ex.getId());
        Assert.assertEquals(getInstanceClass(), ex.getEntityClass());
    }

    protected void assertPushNotificationRecipientAlreadyExistsException(final PushNotificationRecipientAlreadyExistsException ex, final Long existingRecipientId, final PushNotificationProviderType type, final String destinationRouteToken, final Long subscriptionId) {
        assertEquals(existingRecipientId, ex.getExistingRecipientId());
        Assert.assertEquals(type, ex.getType());
        assertEquals(destinationRouteToken, ex.getDestinationRouteToken());
        assertEquals(subscriptionId, ex.getSubscriptionId());
    }

    private void assertPushNotificationRecipientInvalidDeviceUserException(final PushNotificationRecipientInvalidDeviceUserException ex, final Long pushNotificationRecipientId, final Long pushNotificationRecipientUserId, final Long deviceId, final Long deviceUserId) {
        assertEquals(pushNotificationRecipientId, ex.getPushNotificationRecipientId());
        assertEquals(pushNotificationRecipientUserId, ex.getPushNotificationRecipientUserId());
        assertEquals(deviceId, ex.getDeviceId());
        assertEquals(deviceUserId, ex.getDeviceUserId());
    }

    /* Properties getters and setters */
    public PushNotificationSubscriptionService getPushNotificationSubscriptionService() {
        return pushNotificationSubscriptionService;
    }

    public PushNotificationRecipientRepository getPushNotificationRecipientRepository() {
        return pushNotificationRecipientRepository;
    }
}
