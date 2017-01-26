package com.sfl.nms.services.notification.impl.push;

import com.sfl.nms.services.device.UserDeviceService;
import com.sfl.nms.services.device.model.UserDevice;
import com.sfl.nms.services.device.model.mobile.DeviceOperatingSystemType;
import com.sfl.nms.services.notification.dto.push.PushNotificationRecipientSearchParameters;
import com.sfl.nms.services.notification.dto.push.PushNotificationSubscriptionDto;
import com.sfl.nms.services.notification.dto.push.PushNotificationSubscriptionProcessingParameters;
import com.sfl.nms.services.notification.exception.push.PushNotificationSubscriptionInvalidDeviceUserException;
import com.sfl.nms.services.notification.model.push.PushNotificationProviderType;
import com.sfl.nms.services.notification.model.push.PushNotificationRecipient;
import com.sfl.nms.services.notification.model.push.PushNotificationSubscription;
import com.sfl.nms.services.notification.push.PushNotificationRecipientService;
import com.sfl.nms.services.test.AbstractServicesUnitTest;
import com.sfl.nms.services.user.UserService;
import com.sfl.nms.services.user.model.User;
import com.sfl.nms.services.notification.impl.push.sns.PushNotificationUserDeviceTokenSnsProcessor;
import com.sfl.nms.services.notification.model.push.PushNotificationRecipientStatus;
import com.sfl.nms.services.notification.push.PushNotificationSubscriptionService;
import static org.easymock.EasyMock.*;
import org.easymock.Mock;
import org.easymock.TestSubject;
import static org.junit.Assert.*;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * User: Ruben Dilanyan
 * Company: SFL LLC
 * Date: 8/20/15
 * Time: 2:31 PM
 */
public class PushNotificationSubscriptionProcessingServiceImplTest extends AbstractServicesUnitTest {

    /* Test subject and mocks */
    @TestSubject
    private PushNotificationSubscriptionProcessingServiceImpl pushNotificationSubscriptionProcessingService = new PushNotificationSubscriptionProcessingServiceImpl();

    @Mock
    private UserDeviceService userMobileDeviceService;

    @Mock
    private UserService userService;

    @Mock
    private PushNotificationSubscriptionService pushNotificationSubscriptionService;

    @Mock
    private PushNotificationUserDeviceTokenSnsProcessor pushNotificationUserDeviceTokenSnsProcessor;

    @Mock
    private PushNotificationRecipientService pushNotificationRecipientService;

    private PushNotificationProviderType activeProvider = PushNotificationProviderType.SNS;

    /* Constructors */
    public PushNotificationSubscriptionProcessingServiceImplTest() {
    }

    public void prepare() {
        pushNotificationSubscriptionProcessingService.setActiveProvider(activeProvider);
    }

    /* Test methods */
    @Test
    public void testProcessPushNotificationSubscriptionChangeWithInvalidArguments() {
        // Test data
        final PushNotificationSubscriptionProcessingParameters parameters = createPushNotificationSubscriptionProcessingParameters();
        // Reset
        resetAll();
        // Replay
        replayAll();
        // Run test scenario
        try {
            pushNotificationSubscriptionProcessingService.processPushNotificationSubscriptionChange(null);
            fail("Exception should be thrown");
        } catch (final IllegalArgumentException ex) {
            // Expected
        }
        try {
            pushNotificationSubscriptionProcessingService.processPushNotificationSubscriptionChange(new PushNotificationSubscriptionProcessingParameters(null, parameters.getUserMobileDeviceId(), parameters.getDeviceToken(), parameters.getCurrentPushNotificationProviderType(), parameters.getCurrentProviderToken(), parameters.isSubscribe(), parameters.getApplicationType()));
            fail("Exception should be thrown");
        } catch (final IllegalArgumentException ex) {
            // Expected
        }
        try {
            pushNotificationSubscriptionProcessingService.processPushNotificationSubscriptionChange(new PushNotificationSubscriptionProcessingParameters(parameters.getUserId(), null, parameters.getDeviceToken(), parameters.getCurrentPushNotificationProviderType(), parameters.getCurrentProviderToken(), parameters.isSubscribe(), parameters.getApplicationType()));
            fail("Exception should be thrown");
        } catch (final IllegalArgumentException ex) {
            // Expected
        }
        try {
            pushNotificationSubscriptionProcessingService.processPushNotificationSubscriptionChange(new PushNotificationSubscriptionProcessingParameters(parameters.getUserId(), parameters.getUserMobileDeviceId(), null, parameters.getCurrentPushNotificationProviderType(), parameters.getCurrentProviderToken(), parameters.isSubscribe(), parameters.getApplicationType()));
            fail("Exception should be thrown");
        } catch (final IllegalArgumentException ex) {
            // Expected
        }
        try {
            pushNotificationSubscriptionProcessingService.processPushNotificationSubscriptionChange(new PushNotificationSubscriptionProcessingParameters(parameters.getUserId(), parameters.getUserMobileDeviceId(), parameters.getDeviceToken(), parameters.getCurrentPushNotificationProviderType(), parameters.getCurrentProviderToken(), parameters.isSubscribe(), null));
            fail("Exception should be thrown");
        } catch (final IllegalArgumentException ex) {
            // Expected
        }
        // Verify
        verifyAll();
    }

    @Test
    public void testProcessPushNotificationSubscriptionChangeWithDifferentProvidedAndDeviceUsers() {
        // Test data
        final PushNotificationSubscriptionProcessingParameters parameters = createPushNotificationSubscriptionProcessingParameters();
        final Long userId = parameters.getUserId();
        final Long deviceId = parameters.getUserMobileDeviceId();
        final Long deviceUserId = 5L;
        final User deviceUser = getServicesImplTestHelper().createUser();
        deviceUser.setId(deviceUserId);
        final User user = getServicesImplTestHelper().createUser();
        user.setId(userId);
        final UserDevice userMobileDevice = getServicesImplTestHelper().createUserDevice();
        userMobileDevice.setId(deviceId);
        userMobileDevice.setUser(deviceUser);
        // Reset
        resetAll();
        // Expectations
        expect(userService.getUserById(eq(userId))).andReturn(user).once();
        expect(userMobileDeviceService.getUserDeviceById(eq(deviceId))).andReturn(userMobileDevice).once();
        // Replay
        replayAll();
        // Run test scenario
        try {
            pushNotificationSubscriptionProcessingService.processPushNotificationSubscriptionChange(parameters);
            fail("Exception should be thrown");
        } catch (final PushNotificationSubscriptionInvalidDeviceUserException ex) {
            // Expected
            assertPushNotificationSubscriptionInvalidDeviceUserException(ex, userId, deviceId, deviceUserId);
        }
        // Verify
        verifyAll();
    }

    @Test
    public void testProcessPushNotificationSubscriptionChangeByEnablingSubscription() {
        testProcessPushNotificationSubscriptionChange(true, false, false, true, false);
    }

    @Test
    public void testProcessPushNotificationSubscriptionChangeByDisablingSubscription() {
        testProcessPushNotificationSubscriptionChange(false, false, false, true, false);
    }

    @Test
    public void testProcessPushNotificationSubscriptionChangeWhenRecipientAlreadyExists() {
        testProcessPushNotificationSubscriptionChange(true, true, false, true, false);
    }

    @Test
    public void testProcessPushNotificationSubscriptionChangeWhenSubscriptionAlreadyExists() {
        testProcessPushNotificationSubscriptionChange(true, false, true, true, false);
    }

    @Test
    public void testProcessPushNotificationSubscriptionChangeWhenOldAndNewProviderTokensAreSame() {
        testProcessPushNotificationSubscriptionChange(true, false, false, false, false);
    }

    @Test
    public void testProcessPushNotificationSubscriptionChangeWhenOldAndNewProviderTypesAreDifferent() {
        testProcessPushNotificationSubscriptionChange(true, false, false, true, true);
    }

    /* Utility methods */
    public void testProcessPushNotificationSubscriptionChange(final boolean subscribe, final boolean recipientAlreadyExists, final boolean subscriptionAlreadyExists, final boolean oldProviderTokenIsDifferentThenNewOne, final boolean currentTokenProviderIsDifferent) {
        // Test data
        final PushNotificationRecipientStatus recipientCurrentStatus = subscribe ? PushNotificationRecipientStatus.DISABLED : PushNotificationRecipientStatus.ENABLED;
        final PushNotificationRecipientStatus recipientTargetStatus = subscribe ? PushNotificationRecipientStatus.ENABLED : PushNotificationRecipientStatus.DISABLED;
        // Create search parameters
        final PushNotificationSubscriptionProcessingParameters parameters = createPushNotificationSubscriptionProcessingParameters();
        parameters.setSubscribe(subscribe);
        if (currentTokenProviderIsDifferent) {
            parameters.setCurrentPushNotificationProviderType(PushNotificationProviderType.GCM);
        } else {
            parameters.setCurrentPushNotificationProviderType(activeProvider);
        }
        final String applicationType = parameters.getApplicationType();
        final Long userId = parameters.getUserId();
        final Long deviceId = parameters.getUserMobileDeviceId();
        // Create user
        final User user = getServicesImplTestHelper().createUser();
        user.setId(userId);
        // Create user mobile device
        final UserDevice userMobileDevice = getServicesImplTestHelper().createUserDevice();
        userMobileDevice.setId(deviceId);
        userMobileDevice.setUser(user);
        // Create push notification subscription
        final Long subscriptionId = 10L;
        final PushNotificationSubscription subscription = getServicesImplTestHelper().createPushNotificationSubscription();
        subscription.setId(subscriptionId);
        // Create push notification recipient
        final Long recipientId = 11L;
        final PushNotificationRecipient recipient = getServicesImplTestHelper().createPushNotificationSnsRecipient();
        recipient.setId(recipientId);
        recipient.setSubscription(subscription);
        recipient.setStatus(recipientCurrentStatus);
        // Create list of recipients with same token and which has to be disabled
        final List<PushNotificationRecipient> recipientsWithSameNewlyRegisteredToken = createPushNotificationRecipients(10, 100);
        recipientsWithSameNewlyRegisteredToken.add(recipient);
        final List<PushNotificationRecipient> recipientsWithSameOldRegisteredToken = createPushNotificationRecipients(10, 1000);
        // Push notification provider token to be created
        final String newlyRegisteredPushNotificationProviderToken = oldProviderTokenIsDifferentThenNewOne ? "(*&*BYFTCFDJRCGHKRTDLFRUYJFTYDIRDTJRU" : parameters.getCurrentProviderToken();
        // Create push notification search parameters
        final PushNotificationRecipientSearchParameters searchParametersForSearchingRecipientsToBeDisabledWithNewProviderToken = createSearchParametersForSearchingRecipientsToBeDisabled(userMobileDevice.getOsType(), newlyRegisteredPushNotificationProviderToken, applicationType);
        final PushNotificationRecipientSearchParameters searchParametersForSearchingRecipientsToBeDisabledWithOldProviderToken = createSearchParametersForSearchingRecipientsToBeDisabled(userMobileDevice.getOsType(), parameters.getCurrentProviderToken(), applicationType);
        final PushNotificationRecipientSearchParameters searchParametersForSearchingRecipientWithNewProviderToken = createSearchParametersForSearchingRecipientWithNewProviderToken(userMobileDevice.getOsType(), newlyRegisteredPushNotificationProviderToken, subscriptionId, applicationType);
        // Current provider token to be used
        final String currentProviderTokenToBeUsed = currentTokenProviderIsDifferent ? null : parameters.getCurrentProviderToken();
        // Reset
        resetAll();
        // Expectations
        expect(userService.getUserById(eq(userId))).andReturn(user).once();
        expect(userMobileDeviceService.getUserDeviceById(eq(deviceId))).andReturn(userMobileDevice).once();
        expect(pushNotificationSubscriptionService.checkIfPushNotificationSubscriptionExistsForUser(eq(userId))).andReturn(subscriptionAlreadyExists).once();
        if (subscriptionAlreadyExists) {
            expect(pushNotificationSubscriptionService.getPushNotificationSubscriptionForUser(eq(userId))).andReturn(subscription).once();
        } else {
            expect(pushNotificationSubscriptionService.createPushNotificationSubscription(eq(userId), eq(new PushNotificationSubscriptionDto()))).andReturn(subscription).once();
        }
        expect(pushNotificationUserDeviceTokenSnsProcessor.registerUserDeviceToken(eq(parameters.getDeviceToken()), eq(userMobileDevice.getOsType()), eq(parameters.getApplicationType()), eq(currentProviderTokenToBeUsed))).andReturn(newlyRegisteredPushNotificationProviderToken).once();
        expect(pushNotificationRecipientService.getPushNotificationRecipientsForSearchParameters(eq(searchParametersForSearchingRecipientsToBeDisabledWithNewProviderToken), eq(Long.valueOf(0L)), eq(Integer.MAX_VALUE))).andReturn(recipientsWithSameNewlyRegisteredToken).once();
        recipientsWithSameNewlyRegisteredToken.forEach(currentRecipient -> {
            if (!currentRecipient.getSubscription().getId().equals(subscriptionId)) {
                expect(pushNotificationRecipientService.updatePushNotificationRecipientStatus(eq(currentRecipient.getId()), eq(PushNotificationRecipientStatus.DISABLED))).andReturn(currentRecipient).once();
            }
        });
        if (recipientAlreadyExists) {
            expect(pushNotificationRecipientService.getPushNotificationRecipientsForSearchParameters(eq(searchParametersForSearchingRecipientWithNewProviderToken), eq(Long.valueOf(0L)), eq(Integer.valueOf(1)))).andReturn(Arrays.asList(recipient)).once();
        } else {
            expect(pushNotificationRecipientService.getPushNotificationRecipientsForSearchParameters(eq(searchParametersForSearchingRecipientWithNewProviderToken), eq(Long.valueOf(0L)), eq(Integer.valueOf(1)))).andReturn(Collections.emptyList()).once();
            expect(pushNotificationUserDeviceTokenSnsProcessor.createPushNotificationRecipient(eq(subscriptionId), eq(newlyRegisteredPushNotificationProviderToken), eq(userMobileDevice.getOsType()), eq(applicationType))).andReturn(recipient).once();
        }
        if (currentProviderTokenToBeUsed != null && oldProviderTokenIsDifferentThenNewOne) {
            expect(pushNotificationRecipientService.getPushNotificationRecipientsForSearchParameters(eq(searchParametersForSearchingRecipientsToBeDisabledWithOldProviderToken), eq(Long.valueOf(0L)), eq(Integer.MAX_VALUE))).andReturn(recipientsWithSameOldRegisteredToken).once();
            recipientsWithSameOldRegisteredToken.forEach(currentRecipient -> {
                expect(pushNotificationRecipientService.updatePushNotificationRecipientStatus(eq(currentRecipient.getId()), eq(PushNotificationRecipientStatus.DISABLED))).andReturn(currentRecipient).once();
            });
        }
        expect(pushNotificationRecipientService.updatePushNotificationRecipientStatus(eq(recipientId), eq(recipientTargetStatus))).andReturn(recipient).once();
        expect(pushNotificationRecipientService.updatePushNotificationRecipientUserDevice(eq(recipientId), eq(userMobileDevice.getId()))).andReturn(recipient).once();
        // Replay
        replayAll();
        // Run test scenario
        final PushNotificationRecipient result = pushNotificationSubscriptionProcessingService.processPushNotificationSubscriptionChange(parameters);
        assertEquals(recipient, result);
        // Verify
        verifyAll();
    }

    private PushNotificationRecipientSearchParameters createSearchParametersForSearchingRecipientsToBeDisabled(final DeviceOperatingSystemType operatingSystemType, final String pushNotificationProviderToken, final String applicationType) {
        final PushNotificationRecipientSearchParameters parameters = new PushNotificationRecipientSearchParameters();
        parameters.setProviderType(activeProvider);
        parameters.setDeviceOperatingSystemType(operatingSystemType);
        parameters.setStatus(PushNotificationRecipientStatus.ENABLED);
        parameters.setDestinationRouteToken(pushNotificationProviderToken);
        parameters.setApplicationType(applicationType);
        return parameters;
    }

    private PushNotificationRecipientSearchParameters createSearchParametersForSearchingRecipientWithNewProviderToken(final DeviceOperatingSystemType operatingSystemType, final String pushNotificationProviderToken, final Long subscriptionId, final String applicationType) {
        final PushNotificationRecipientSearchParameters parameters = new PushNotificationRecipientSearchParameters();
        parameters.setProviderType(activeProvider);
        parameters.setDeviceOperatingSystemType(operatingSystemType);
        parameters.setSubscriptionId(subscriptionId);
        parameters.setDestinationRouteToken(pushNotificationProviderToken);
        parameters.setApplicationType(applicationType);
        return parameters;
    }

    private List<PushNotificationRecipient> createPushNotificationRecipients(final int count, final int idAddition) {
        final List<PushNotificationRecipient> recipients = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            final Long subscriptionId = Long.valueOf(idAddition * count + i);
            final PushNotificationSubscription subscription = getServicesImplTestHelper().createPushNotificationSubscription();
            subscription.setId(subscriptionId);
            final Long recipientId = Long.valueOf(idAddition + i);
            final PushNotificationRecipient recipient = getServicesImplTestHelper().createPushNotificationSnsRecipient();
            recipient.setId(recipientId);
            recipient.setSubscription(subscription);
        }
        return recipients;
    }

    private void assertPushNotificationSubscriptionInvalidDeviceUserException(final PushNotificationSubscriptionInvalidDeviceUserException ex, final Long subscriptionUserId, final Long deviceId, final Long deviceUserId) {
        assertEquals(ex.getSubscriptionUserId(), subscriptionUserId);
        assertEquals(ex.getDeviceId(), deviceId);
        assertEquals(ex.getDeviceUserId(), deviceUserId);
    }

    private PushNotificationSubscriptionProcessingParameters createPushNotificationSubscriptionProcessingParameters() {
        final PushNotificationSubscriptionProcessingParameters parameters = new PushNotificationSubscriptionProcessingParameters();
        parameters.setCurrentProviderToken("HYTIDRDLGYFTIFVMCJFJJT");
        parameters.setCurrentPushNotificationProviderType(activeProvider);
        parameters.setDeviceToken("*^&T)TF%*D$DRU$S$OFKTDRDU*%RIDR%TIFTD%**I");
        parameters.setSubscribe(true);
        parameters.setUserId(1L);
        parameters.setUserMobileDeviceId(2L);
        parameters.setApplicationType("customer");
        return parameters;
    }
}
