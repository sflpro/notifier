package com.sflpro.notifier.services.notification.push;

import com.sflpro.notifier.services.device.dto.UserDeviceDto;
import com.sflpro.notifier.services.device.model.UserDevice;
import com.sflpro.notifier.db.entities.device.mobile.DeviceOperatingSystemType;
import com.sflpro.notifier.services.notification.dto.push.PushNotificationSubscriptionRequestDto;
import com.sflpro.notifier.db.entities.notification.push.PushNotificationRecipient;
import com.sflpro.notifier.db.entities.notification.push.PushNotificationRecipientStatus;
import com.sflpro.notifier.db.entities.notification.push.PushNotificationSubscriptionRequest;
import com.sflpro.notifier.db.entities.notification.push.PushNotificationSubscriptionRequestState;
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
 * Date: 8/21/15
 * Time: 11:13 AM
 */
@Ignore
public class PushNotificationSubscriptionRequestProcessingServiceIntegrationTest extends AbstractServiceIntegrationTest {

    /* Dependencies */
    @Autowired
    private PushNotificationSubscriptionRequestProcessingService pushNotificationSubscriptionRequestProcessingService;

    @Autowired
    private PushNotificationSubscriptionRequestService pushNotificationSubscriptionRequestService;

    /* Constructors */
    public PushNotificationSubscriptionRequestProcessingServiceIntegrationTest() {
    }

    /* Test methods */
    @Test
    public void testProcessPushNotificationSubscriptionRequestByEnablingSubscription() {
        testProcessPushNotificationSubscriptionRequest(true);
    }

    @Test
    public void testProcessPushNotificationSubscriptionRequestByDisablingSubscription() {
        testProcessPushNotificationSubscriptionRequest(false);
    }

    /* Utility methods */
    private void testProcessPushNotificationSubscriptionRequest(final boolean subscribe) {
        // Prepare data
        final User user = getServicesTestHelper().createUser();
        final String iOSDeviceToken = getServicesTestHelper().generateIOSToken();
        final UserDeviceDto userMobileDeviceDto = getServicesTestHelper().createUserDeviceDto();
        userMobileDeviceDto.setOsType(DeviceOperatingSystemType.IOS);
        final UserDevice userMobileDevice = getServicesTestHelper().createUserDevice(user, userMobileDeviceDto);
        final PushNotificationRecipientStatus expectedStatus = (subscribe ? PushNotificationRecipientStatus.ENABLED : PushNotificationRecipientStatus.DISABLED);
        // Create subscription request
        final PushNotificationSubscriptionRequestDto requestDto = getServicesTestHelper().createPushNotificationSubscriptionRequestDto();
        requestDto.setUserDeviceToken(iOSDeviceToken);
        requestDto.setSubscribe(subscribe);
        requestDto.setPreviousSubscriptionRequestUuId(null);
        PushNotificationSubscriptionRequest request = getServicesTestHelper().createPushNotificationSubscriptionRequest(user, userMobileDevice, requestDto);
        Assert.assertEquals(PushNotificationSubscriptionRequestState.CREATED, request.getState());
        flushAndClear();
        // Process subscription request
        final PushNotificationRecipient recipient = pushNotificationSubscriptionRequestProcessingService.processPushNotificationSubscriptionRequest(request.getId());
        assertNotNull(recipient);
        assertNotNull(recipient.getLastDevice());
        Assert.assertEquals(userMobileDevice.getId(), recipient.getLastDevice().getId());
        assertEquals(expectedStatus, recipient.getStatus());
        // Reload request and assert state
        request = pushNotificationSubscriptionRequestService.getPushNotificationSubscriptionRequestById(request.getId());
        assertEquals(PushNotificationSubscriptionRequestState.PROCESSED, request.getState());
        assertNotNull(request.getRecipient());
        Assert.assertEquals(recipient.getId(), request.getRecipient().getId());
    }
}
