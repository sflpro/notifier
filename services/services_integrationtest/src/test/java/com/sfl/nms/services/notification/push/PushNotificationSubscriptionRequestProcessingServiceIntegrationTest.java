package com.sfl.nms.services.notification.push;

import com.sfl.nms.services.device.dto.UserDeviceDto;
import com.sfl.nms.services.device.model.UserDevice;
import com.sfl.nms.services.device.model.mobile.DeviceOperatingSystemType;
import com.sfl.nms.services.notification.model.push.PushNotificationRecipient;
import com.sfl.nms.services.notification.model.push.PushNotificationSubscriptionRequest;
import com.sfl.nms.services.notification.model.push.PushNotificationSubscriptionRequestState;
import com.sfl.nms.services.test.AbstractServiceIntegrationTest;
import com.sfl.nms.services.user.model.User;
import com.sfl.nms.services.notification.dto.push.PushNotificationSubscriptionRequestDto;
import com.sfl.nms.services.notification.model.push.PushNotificationRecipientStatus;
import org.junit.Assert;
import static org.junit.Assert.*;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

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
        assertEquals(recipient.getId(), request.getRecipient().getId());
    }
}
