package com.sflpro.notifier.services.notification.sms;

import com.sflpro.notifier.db.entities.notification.sms.SmsNotification;
import com.sflpro.notifier.services.notification.AbstractNotificationService;
import com.sflpro.notifier.services.notification.AbstractNotificationServiceIntegrationTest;
import com.sflpro.notifier.services.notification.dto.NotificationPropertyDto;
import com.sflpro.notifier.services.notification.dto.sms.SmsNotificationDto;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

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
        final List<NotificationPropertyDto> smsNotificationPropertyDtos = getServicesTestHelper().createSmsNotificationPropertyDtos(3);
        // Create notification
        SmsNotification smsNotification = smsNotificationService.createSmsNotification(notificationDto, smsNotificationPropertyDtos);
        getServicesTestHelper().assertSmsNotification(smsNotification, notificationDto, smsNotificationPropertyDtos);
        // Flush, clear, reload and assert
        flushAndClear();
        smsNotification = smsNotificationService.getNotificationById(smsNotification.getId());
        getServicesTestHelper().assertSmsNotification(smsNotification, notificationDto, smsNotificationPropertyDtos);
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
