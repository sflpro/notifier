package com.sfl.nms.queue.consumer.notification.common;

import com.sfl.nms.queue.consumer.test.AbstractQueueConsumerIntegrationTest;
import com.sfl.nms.services.notification.model.NotificationState;
import com.sfl.nms.services.notification.model.sms.SmsNotification;
import com.sfl.nms.services.notification.sms.SmsNotificationService;
import static org.junit.Assert.*;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

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
        assertEquals(smsNotification.getState(), NotificationState.SENT);
    }
}
