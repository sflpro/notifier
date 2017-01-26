package com.sfl.nms.queue.consumer.notification.push;

import com.sfl.nms.queue.consumer.test.AbstractQueueConsumerIntegrationTest;
import com.sfl.nms.services.device.dto.UserDeviceDto;
import com.sfl.nms.services.device.model.UserDevice;
import com.sfl.nms.services.device.model.mobile.DeviceOperatingSystemType;
import com.sfl.nms.services.notification.dto.push.PushNotificationSubscriptionRequestDto;
import com.sfl.nms.services.notification.model.push.PushNotificationSubscriptionRequest;
import com.sfl.nms.services.notification.model.push.PushNotificationSubscriptionRequestState;
import com.sfl.nms.services.notification.push.PushNotificationSubscriptionRequestService;
import com.sfl.nms.services.user.model.User;
import static org.junit.Assert.*;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * User: Ruben Dilanyan
 * Company: SFL LLC
 * Date: 8/23/15
 * Time: 12:33 AM
 */
@Ignore
public class PushNotificationSubscriptionRequestQueueConsumerServiceIntegrationTest extends AbstractQueueConsumerIntegrationTest {

    /* Dependencies */
    @Autowired
    private PushNotificationSubscriptionRequestQueueConsumerService pushNotificationSubscriptionRequestQueueConsumerService;

    @Autowired
    private PushNotificationSubscriptionRequestService pushNotificationSubscriptionRequestService;

    /* Constructors */
    public PushNotificationSubscriptionRequestQueueConsumerServiceIntegrationTest() {
    }

    /* Test methods */
    @Test
    public void testProcessPushNotificationSubscriptionRequest() {
        // Prepare data
        final User user = getServicesTestHelper().createUser();
        final String iOSDeviceToken = getServicesTestHelper().generateIOSToken();
        final UserDeviceDto userMobileDeviceDto = getServicesTestHelper().createUserDeviceDto();
        userMobileDeviceDto.setOsType(DeviceOperatingSystemType.IOS);
        final UserDevice userMobileDevice = getServicesTestHelper().createUserDevice(user, userMobileDeviceDto);
        // Create subscription request
        final PushNotificationSubscriptionRequestDto requestDto = getServicesTestHelper().createPushNotificationSubscriptionRequestDto();
        requestDto.setUserDeviceToken(iOSDeviceToken);
        requestDto.setSubscribe(true);
        requestDto.setPreviousSubscriptionRequestUuId(null);
        PushNotificationSubscriptionRequest request = getServicesTestHelper().createPushNotificationSubscriptionRequest(user, userMobileDevice, requestDto);
        assertEquals(PushNotificationSubscriptionRequestState.CREATED, request.getState());
        flushAndClear();
        // Process subscription request
        pushNotificationSubscriptionRequestQueueConsumerService.processPushNotificationSubscriptionRequest(request.getId());
        // Reload request and assert state
        request = pushNotificationSubscriptionRequestService.getPushNotificationSubscriptionRequestById(request.getId());
        assertEquals(PushNotificationSubscriptionRequestState.PROCESSED, request.getState());
    }
}
