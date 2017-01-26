package com.sfl.nms.services.notification.impl.push.sns;

import com.sfl.nms.services.notification.dto.push.sns.PushNotificationSnsRecipientDto;
import com.sfl.nms.services.notification.impl.push.AbstractPushNotificationRecipientServiceImpl;
import com.sfl.nms.services.notification.model.push.PushNotificationSubscription;
import com.sfl.nms.services.notification.model.push.sns.PushNotificationSnsRecipient;
import com.sfl.nms.persistence.repositories.notification.push.AbstractPushNotificationRecipientRepository;
import com.sfl.nms.persistence.repositories.notification.push.sns.PushNotificationSnsRecipientRepository;
import com.sfl.nms.services.notification.exception.push.PushNotificationRecipientAlreadyExistsException;
import com.sfl.nms.services.notification.impl.push.AbstractPushNotificationRecipientServiceImplTest;
import static org.easymock.EasyMock.*;
import org.easymock.Mock;
import org.easymock.TestSubject;
import org.junit.Assert;
import static org.junit.Assert.*;
import org.junit.Test;

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
        expect(getPushNotificationRecipientRepository().findByTypeAndSubscriptionAndDestinationRouteTokenAndApplicationType(eq(recipientDto.getType()), eq(subscription), eq(recipientDto.getDestinationRouteToken()), eq(recipientDto.getApplicationType()))).andReturn(existingRecipient).once();
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
        expect(getPushNotificationRecipientRepository().findByTypeAndSubscriptionAndDestinationRouteTokenAndApplicationType(eq(recipientDto.getType()), eq(subscription), eq(recipientDto.getDestinationRouteToken()), eq(recipientDto.getApplicationType()))).andReturn(null).once();
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
