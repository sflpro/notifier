package com.sflpro.notifier.services.notification.impl.push.sns;

import com.sflpro.notifier.db.entities.notification.push.PushNotificationSubscription;
import com.sflpro.notifier.db.entities.notification.push.sns.PushNotificationSnsRecipient;
import com.sflpro.notifier.db.repositories.repositories.notification.push.AbstractPushNotificationRecipientRepository;
import com.sflpro.notifier.db.repositories.repositories.notification.push.sns.PushNotificationSnsRecipientRepository;
import com.sflpro.notifier.services.notification.dto.push.sns.PushNotificationSnsRecipientDto;
import com.sflpro.notifier.services.notification.exception.push.PushNotificationRecipientAlreadyExistsException;
import com.sflpro.notifier.services.notification.impl.push.AbstractPushNotificationRecipientServiceImpl;
import com.sflpro.notifier.services.notification.impl.push.AbstractPushNotificationRecipientServiceImplTest;
import org.easymock.EasyMock;
import org.easymock.Mock;
import org.easymock.TestSubject;
import org.junit.Assert;
import org.junit.Test;

import static org.easymock.EasyMock.*;
import static org.junit.Assert.fail;

/**
 * User: Ruben Dilanyan
 * Company: SFL LLC
 * Date: 8/13/15
 * Time: 1:30 PM
 */
public class PushNotificationSnsRecipientServiceImplTest extends AbstractPushNotificationRecipientServiceImplTest<PushNotificationSnsRecipient> {

    /* Test subject and mocks */
    @TestSubject
    private PushNotificationSnsRecipientServiceImpl pushNotificationSnsRecipientService = new PushNotificationSnsRecipientServiceImpl();

    @Mock
    private PushNotificationSnsRecipientRepository pushNotificationSnsRecipientRepository;

    /* Constructors */
    public PushNotificationSnsRecipientServiceImplTest() {
    }

    /* Test methods */
    @Test
    public void testCreatePushNotificationRecipientWithInvalidArguments() {
        // Test data
        final Long subscriptionId = 1L;
        final PushNotificationSnsRecipientDto recipientDto = getServicesImplTestHelper().createPushNotificationSnsRecipientDto();
        // Reset
        resetAll();
        // Replay
        replayAll();
        // Run test scenario
        try {
            pushNotificationSnsRecipientService.createPushNotificationRecipient(null, recipientDto);
            fail("Exception should be thrown");
        } catch (final IllegalArgumentException ex) {
            // Expected
        }
        try {
            pushNotificationSnsRecipientService.createPushNotificationRecipient(subscriptionId, null);
            fail("Exception should be thrown");
        } catch (final IllegalArgumentException ex) {
            // Expected
        }
        try {
            pushNotificationSnsRecipientService.createPushNotificationRecipient(subscriptionId, new PushNotificationSnsRecipientDto(null, recipientDto.getDeviceOperatingSystemType(), recipientDto.getApplicationType(), recipientDto.getPlatformApplicationArn()));
            fail("Exception should be thrown");
        } catch (final IllegalArgumentException ex) {
            // Expected
        }
        try {
            pushNotificationSnsRecipientService.createPushNotificationRecipient(subscriptionId, new PushNotificationSnsRecipientDto(recipientDto.getDestinationRouteToken(), null, recipientDto.getApplicationType(), recipientDto.getPlatformApplicationArn()));
            fail("Exception should be thrown");
        } catch (final IllegalArgumentException ex) {
            // Expected
        }
        try {
            pushNotificationSnsRecipientService.createPushNotificationRecipient(subscriptionId, new PushNotificationSnsRecipientDto(recipientDto.getDestinationRouteToken(), recipientDto.getDeviceOperatingSystemType(), null, recipientDto.getPlatformApplicationArn()));
            fail("Exception should be thrown");
        } catch (final IllegalArgumentException ex) {
            // Expected
        }
        try {
            pushNotificationSnsRecipientService.createPushNotificationRecipient(subscriptionId, new PushNotificationSnsRecipientDto(recipientDto.getDestinationRouteToken(), recipientDto.getDeviceOperatingSystemType(), recipientDto.getApplicationType(), null));
            fail("Exception should be thrown");
        } catch (final IllegalArgumentException ex) {
            // Expected
        }
        // Verify
        verifyAll();
    }

    @Test
    public void testCreatePushNotificationRecipientWhenItAlreadyExists() {
        // Test data
        final Long subscriptionId = 1L;
        final PushNotificationSubscription subscription = getServicesImplTestHelper().createPushNotificationSubscription();
        subscription.setId(subscriptionId);
        final PushNotificationSnsRecipientDto recipientDto = getServicesImplTestHelper().createPushNotificationSnsRecipientDto();
        final Long existingRecipientId = 2L;
        final PushNotificationSnsRecipient existingRecipient = getServicesImplTestHelper().createPushNotificationSnsRecipient();
        existingRecipient.setId(existingRecipientId);
        // Reset
        resetAll();
        // Expectations
        expect(getPushNotificationSubscriptionService().getPushNotificationSubscriptionById(eq(subscriptionId))).andReturn(subscription).once();
        expect(getPushNotificationRecipientRepository().findByTypeAndSubscriptionAndDestinationRouteTokenAndApplicationType(EasyMock.eq(recipientDto.getType()), eq(subscription), EasyMock.eq(recipientDto.getDestinationRouteToken()), EasyMock.eq(recipientDto.getApplicationType()))).andReturn(existingRecipient).once();
        // Replay
        replayAll();
        // Run test scenario
        try {
            pushNotificationSnsRecipientService.createPushNotificationRecipient(subscriptionId, recipientDto);
            fail("Exception should be thrown");
        } catch (final PushNotificationRecipientAlreadyExistsException ex) {
            // Expected
            assertPushNotificationRecipientAlreadyExistsException(ex, existingRecipientId, recipientDto.getType(), recipientDto.getDestinationRouteToken(), subscriptionId);
        }
        // Verify
        verifyAll();
    }

    @Test
    public void testCreatePushNotificationRecipient() {
        // Test data
        final Long subscriptionId = 1L;
        final PushNotificationSubscription subscription = getServicesImplTestHelper().createPushNotificationSubscription();
        subscription.setId(subscriptionId);
        final PushNotificationSnsRecipientDto recipientDto = getServicesImplTestHelper().createPushNotificationSnsRecipientDto();
        // Reset
        resetAll();
        // Expectations
        expect(getPushNotificationSubscriptionService().getPushNotificationSubscriptionById(eq(subscriptionId))).andReturn(subscription).once();
        expect(getPushNotificationRecipientRepository().findByTypeAndSubscriptionAndDestinationRouteTokenAndApplicationType(EasyMock.eq(recipientDto.getType()), eq(subscription), EasyMock.eq(recipientDto.getDestinationRouteToken()), EasyMock.eq(recipientDto.getApplicationType()))).andReturn(null).once();
        expect(pushNotificationSnsRecipientRepository.save(isA(PushNotificationSnsRecipient.class))).andAnswer(() -> (PushNotificationSnsRecipient) getCurrentArguments()[0]).once();
        // Replay
        replayAll();
        // Run test scenario
        final PushNotificationSnsRecipient result = pushNotificationSnsRecipientService.createPushNotificationRecipient(subscriptionId, recipientDto);
        getServicesImplTestHelper().assertPushNotificationSnsRecipient(result, recipientDto);
        Assert.assertEquals(subscription, result.getSubscription());
        // Verify
        verifyAll();
    }

    /* Utility methods */
    @Override
    protected PushNotificationSnsRecipient getInstance() {
        return getServicesImplTestHelper().createPushNotificationSnsRecipient();
    }

    @Override
    protected Class<PushNotificationSnsRecipient> getInstanceClass() {
        return PushNotificationSnsRecipient.class;
    }

    @Override
    protected AbstractPushNotificationRecipientServiceImpl<PushNotificationSnsRecipient> getService() {
        return pushNotificationSnsRecipientService;
    }

    @Override
    protected AbstractPushNotificationRecipientRepository<PushNotificationSnsRecipient> getRepository() {
        return pushNotificationSnsRecipientRepository;
    }

}
