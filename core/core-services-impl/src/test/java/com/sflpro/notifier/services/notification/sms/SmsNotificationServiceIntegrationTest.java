package com.sflpro.notifier.services.notification.sms;

import com.sflpro.notifier.db.entities.notification.SmsNotification;
import com.sflpro.notifier.services.notification.AbstractNotificationService;
import com.sflpro.notifier.services.notification.AbstractNotificationServiceIntegrationTest;
import com.sflpro.notifier.services.notification.dto.sms.SmsNotificationDto;
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
