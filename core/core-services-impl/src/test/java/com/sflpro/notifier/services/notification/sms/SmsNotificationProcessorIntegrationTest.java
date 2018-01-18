package com.sflpro.notifier.services.notification.sms;

import com.sflpro.notifier.db.entities.notification.NotificationState;
import com.sflpro.notifier.db.entities.notification.SmsNotification;
import com.sflpro.notifier.services.test.AbstractServiceIntegrationTest;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.assertNotNull;

/**
 * User: Mher Sargsyan
 * Company: SFL LLC
 * Date: 4/10/15
 * Time: 5:34 PM
 */
@Ignore
public class SmsNotificationProcessorIntegrationTest extends AbstractServiceIntegrationTest {

    /* Dependencies */
    @Autowired
    private SmsNotificationProcessor smsNotificationProcessingService;

    @Autowired
    private SmsNotificationService smsNotificationService;

    /* Constructors */
    public SmsNotificationProcessorIntegrationTest() {
    }

    /* Test methods */
    @Test
    public void testProcessSmsMessage() {
        /* Create sms notification */
        SmsNotification smsNotification = getServicesTestHelper().createSmsNotification();
        /* Flush, clear  and process sending sms message */
        flushAndClear();
        smsNotificationProcessingService.processNotification(smsNotification.getId());
        /* Flush, clear, reload notification and check state */
        smsNotification = smsNotificationService.getNotificationById(smsNotification.getId());
        assertNotNull(smsNotification);
        Assert.assertEquals(smsNotification.getState(), NotificationState.SENT);
        assertNotNull(smsNotification.getProviderExternalUuId());
    }
}
