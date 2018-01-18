package com.sflpro.notifier.services.notification.push;


import com.sflpro.notifier.services.device.dto.UserDeviceDto;
import com.sflpro.notifier.services.device.model.UserDevice;
import com.sflpro.notifier.db.entities.device.mobile.DeviceOperatingSystemType;
import com.sflpro.notifier.services.helper.ServicesTestHelper;
import com.sflpro.notifier.services.notification.dto.push.PushNotificationSubscriptionProcessingParameters;
import com.sflpro.notifier.db.entities.notification.push.PushNotificationRecipient;
import com.sflpro.notifier.db.entities.notification.push.PushNotificationRecipientStatus;
import com.sflpro.notifier.services.test.AbstractServiceIntegrationTest;
import com.sflpro.notifier.services.user.model.User;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * User: Ruben Dilanyan
 * Company: SFL LLC
 * Date: 8/22/15
 * Time: 11:12 PM
 */
@Ignore
public class PushNotificationSubscriptionProcessingServiceIntegrationTest extends AbstractServiceIntegrationTest {

    /* Dependencies */
    @Autowired
    private PushNotificationSubscriptionProcessingService pushNotificationSubscriptionProcessingService;

    @Autowired
    private PushNotificationRecipientService pushNotificationRecipientService;

    /* Constructors */
    public PushNotificationSubscriptionProcessingServiceIntegrationTest() {
    }

    /* Test methods */
    @Test
    public void testProcessPushNotificationSubscriptionChangeByEnablingSubscription() {
        testProcessPushNotificationSubscriptionChange(true);
    }

    @Test
    public void testProcessPushNotificationSubscriptionChangeByDisablingSubscription() {
        testProcessPushNotificationSubscriptionChange(false);
    }

    @Test
    public void testProcessPushNotificationSubscriptionChangeWhenCurrentTokenDataIsProvided() {
        // Create user mobile device
        final User user = getServicesTestHelper().createUser();
        final String iOSDeviceToken = getServicesTestHelper().generateIOSToken();
        final UserDeviceDto userMobileDeviceDto = getServicesTestHelper().createUserDeviceDto();
        userMobileDeviceDto.setOsType(DeviceOperatingSystemType.IOS);
        final UserDevice userMobileDevice = getServicesTestHelper().createUserDevice(user, userMobileDeviceDto);
        final PushNotificationRecipientStatus expectedStatus = PushNotificationRecipientStatus.ENABLED;
        // Create token processing parameters
        final PushNotificationSubscriptionProcessingParameters parameters = createPushNotificationSubscriptionProcessingParameters(user, userMobileDevice, iOSDeviceToken, true);
        // Process subscription
        final PushNotificationRecipient firstRecipient = pushNotificationSubscriptionProcessingService.processPushNotificationSubscriptionChange(parameters);
        assertNotNull(firstRecipient);
        assertNotNull(firstRecipient.getLastDevice());
        assertEquals(userMobileDevice.getId(), firstRecipient.getLastDevice().getId());
        assertEquals(expectedStatus, firstRecipient.getStatus());
        // Process again using same parameters but with different token same recipient should be returned
        final String newIOSDeviceToken = getServicesTestHelper().generateIOSToken();
        parameters.setDeviceToken(newIOSDeviceToken);
        parameters.setCurrentPushNotificationProviderType(firstRecipient.getType());
        parameters.setCurrentProviderToken(firstRecipient.getDestinationRouteToken());
        final PushNotificationRecipient secondRecipient = pushNotificationSubscriptionProcessingService.processPushNotificationSubscriptionChange(parameters);
        assertNotNull(secondRecipient);
        assertEquals(firstRecipient.getId(), secondRecipient.getId());
    }

    @Test
    public void testProcessPushNotificationSubscriptionChangeWhenMultipleRecipientsExistForSameUserDeviceToken() {
        /* Prepare data */
        // Create user mobile device
        final String iOSDeviceToken = getServicesTestHelper().generateIOSToken();
        // First user data
        final User firstUser = getServicesTestHelper().createUser();
        final UserDeviceDto firstUserMobileDeviceDto = getServicesTestHelper().createUserDeviceDto();
        firstUserMobileDeviceDto.setOsType(DeviceOperatingSystemType.IOS);
        final UserDevice firstUserMobileDevice = getServicesTestHelper().createUserDevice(firstUser, firstUserMobileDeviceDto);
        // Second user data
        final User secondUser = getServicesTestHelper().createUser();
        final UserDeviceDto secondUserMobileDeviceDto = getServicesTestHelper().createUserDeviceDto();
        secondUserMobileDeviceDto.setOsType(DeviceOperatingSystemType.IOS);
        final UserDevice secondUserMobileDevice = getServicesTestHelper().createUserDevice(secondUser, secondUserMobileDeviceDto);
        // Create token processing parameters
        PushNotificationSubscriptionProcessingParameters parameters = createPushNotificationSubscriptionProcessingParameters(firstUser, firstUserMobileDevice, iOSDeviceToken, true);
        // Process subscription
        PushNotificationRecipient firstRecipient = pushNotificationSubscriptionProcessingService.processPushNotificationSubscriptionChange(parameters);
        assertNotNull(firstRecipient);
        assertEquals(PushNotificationRecipientStatus.ENABLED, firstRecipient.getStatus());
        // Create another recipient with same token
        parameters = createPushNotificationSubscriptionProcessingParameters(secondUser, secondUserMobileDevice, iOSDeviceToken, true);
        final PushNotificationRecipient secondRecipient = pushNotificationSubscriptionProcessingService.processPushNotificationSubscriptionChange(parameters);
        assertNotNull(secondRecipient);
        assertEquals(PushNotificationRecipientStatus.ENABLED, secondRecipient.getStatus());
        flushAndClear();
        // Reload first recipient and verify that it is now disabled
        firstRecipient = pushNotificationRecipientService.getPushNotificationRecipientById(firstRecipient.getId());
        assertEquals(PushNotificationRecipientStatus.DISABLED, firstRecipient.getStatus());
    }

    /* Utility method */
    private void testProcessPushNotificationSubscriptionChange(final boolean subscribe) {
        // Create user mobile device
        final User user = getServicesTestHelper().createUser();
        final String iOSDeviceToken = getServicesTestHelper().generateIOSToken();
        final UserDeviceDto userMobileDeviceDto = getServicesTestHelper().createUserDeviceDto();
        userMobileDeviceDto.setOsType(DeviceOperatingSystemType.IOS);
        final UserDevice userMobileDevice = getServicesTestHelper().createUserDevice(user, userMobileDeviceDto);
        final PushNotificationRecipientStatus expectedStatus = (subscribe ? PushNotificationRecipientStatus.ENABLED : PushNotificationRecipientStatus.DISABLED);
        // Create token processing parameters
        final PushNotificationSubscriptionProcessingParameters parameters = createPushNotificationSubscriptionProcessingParameters(user, userMobileDevice, iOSDeviceToken, subscribe);
        // Process subscription
        final PushNotificationRecipient firstRecipient = pushNotificationSubscriptionProcessingService.processPushNotificationSubscriptionChange(parameters);
        assertNotNull(firstRecipient);
        assertNotNull(firstRecipient.getLastDevice());
        assertEquals(userMobileDevice.getId(), firstRecipient.getLastDevice().getId());
        assertEquals(expectedStatus, firstRecipient.getStatus());
        // Process again using same parameters, same recipient should be returned
        final PushNotificationRecipient secondRecipient = pushNotificationSubscriptionProcessingService.processPushNotificationSubscriptionChange(parameters);
        assertNotNull(secondRecipient);
        assertEquals(firstRecipient.getId(), secondRecipient.getId());
    }


    private PushNotificationSubscriptionProcessingParameters createPushNotificationSubscriptionProcessingParameters(final User user, final UserDevice userMobileDevice, final String iOSDeviceToken, final boolean subscribe) {
        final PushNotificationSubscriptionProcessingParameters parameters = new PushNotificationSubscriptionProcessingParameters();
        parameters.setUserId(user.getId());
        parameters.setDeviceToken(iOSDeviceToken);
        parameters.setSubscribe(subscribe);
        parameters.setUserMobileDeviceId(userMobileDevice.getId());
        parameters.setCurrentPushNotificationProviderType(null);
        parameters.setCurrentProviderToken(null);
        parameters.setApplicationType(ServicesTestHelper.APPLICATION_TYPE);
        return parameters;
    }

}
