package com.sfl.nms.services.notification.sms;

import com.sfl.nms.services.notification.AbstractNotificationServiceIntegrationTest;
import com.sfl.nms.services.notification.dto.sms.SmsNotificationDto;
import com.sfl.nms.services.notification.model.sms.SmsNotification;
import com.sfl.nms.services.notification.AbstractNotificationService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * User: Ruben Dilanyan
 * Company: SFL LLC
 * Date: 3/21/15
 * Time: 8:38 PM
 */
public class SmsNotificationServiceIntegrationTest extends AbstractNotificationServiceIntegrationTest<SmsNotification> {

    /* Dependencies */
    @Autowired
    private SmsNotificationService smsNotificationService;

    /* Constructors */
    public SmsNotificationServiceIntegrationTest() {
    }

    /* Test methods */
    @Test
    public void testCreateSmsNotification() {
        // Prepare data
        final SmsNotificationDto notificationDto = getServicesTestHelper().createSmsNotificationDto();
        // Create notification
        SmsNotification smsNotification = smsNotificationService.createSmsNotification(notificationDto);
        getServicesTestHelper().assertSmsNotification(smsNotification, notificationDto);
        // Flush, clear, reload and assert
        flushAndClear();
        smsNotification = smsNotificationService.getNotificationById(smsNotification.getId());
        getServicesTestHelper().assertSmsNotification(smsNotification, notificationDto);
    }

    /* Utility methods */
    @Override
    protected AbstractNotificationService<SmsNotification> getService() {
        return smsNotificationService;
    }

    @Override
    protected SmsNotification getInstance() {
        return getServicesTestHelper().createSmsNotification();
    }

    @Override
    protected Class<SmsNotification> getInstanceClass() {
        return SmsNotification.class;
    }
}
