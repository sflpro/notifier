package com.sflpro.notifier.queue.consumer.notification.common;

import com.sflpro.notifier.db.entities.notification.NotificationState;
import com.sflpro.notifier.db.entities.notification.sms.SmsNotification;
import com.sflpro.notifier.queue.consumer.test.AbstractQueueConsumerIntegrationTest;
import com.sflpro.notifier.services.notification.sms.SmsNotificationService;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * User: Mher Sargsyan
 * Company: SFL LLC
 * Date: 4/10/15
 * Time: 7:17 PM
 */
@Ignore
public class NotificationQueueConsumerServiceIntegrationTest extends AbstractQueueConsumerIntegrationTest {

    /* Dependencies */
    @Autowired
    private NotificationQueueConsumerService notificationQueueConsumerService;

    @Autowired
    private SmsNotificationService smsNotificationService;

    /* Constructors */
    public NotificationQueueConsumerServiceIntegrationTest() {
    }

    /* Test methods */
    @Test
    public void testProcessSmsNotificationMessage() {
        /* Create sms notification */
        SmsNotification smsNotification = getServicesTestHelper().createSmsNotification();
        /* Flush, clear  and process sending sms message */
        flushAndClear();
        notificationQueueConsumerService.processNotification(smsNotification.getId());
        /* Flush, clear, reload notification and check state */
        smsNotification = smsNotificationService.getNotificationById(smsNotification.getId());
        assertNotNull(smsNotification);
        assertEquals(NotificationState.SENT, smsNotification.getState());
    }
}
