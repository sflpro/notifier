package com.sflpro.notifier.queue.consumer.notification.common.impl;

import com.sflpro.notifier.queue.consumer.test.AbstractQueueConsumerUnitTest;
import com.sflpro.notifier.services.notification.NotificationProcessingService;
import org.easymock.Mock;
import org.easymock.TestSubject;
import org.junit.Test;

import static org.easymock.EasyMock.eq;
import static org.easymock.EasyMock.expectLastCall;
import static org.junit.Assert.fail;

/**
 * User: Mher Sargsyan
 * Company: SFL LLC
 * Date: 4/10/15
 * Time: 7:12 PM
 */
public class NotificationQueueConsumerServiceImplTest extends AbstractQueueConsumerUnitTest {

    /* Test subject and mocks */
    @TestSubject
    private NotificationQueueConsumerServiceImpl smsNotificationQueueConsumerService = new NotificationQueueConsumerServiceImpl();

    @Mock
    private NotificationProcessingService notificationProcessingService;


    /* Constructors */
    public NotificationQueueConsumerServiceImplTest() {
    }

    /* Test methods */
    @Test
    public void testProcessSmsNotificationMessageWithInvalidArguments() {
        /* Test data */
        /* Reset mocks */
        resetAll();
        /* Replay mocks */
        replayAll();
        /* Test cases */
        try {
            smsNotificationQueueConsumerService.processNotification(null);
            fail("Exception will be thrown");
        } catch (final IllegalArgumentException e) {
            // Exception
        }
        verifyAll();
    }

    @Test
    public void testProcessSmsNotificationMessage() {
        /* Test data */
        final Long notificationId = 1L;
        /* Reset mocks */
        resetAll();
        /* Register expectations */
        notificationProcessingService.processNotification(eq(notificationId));
        expectLastCall().once();
        /* Replay mocks */
        replayAll();
        /* Test cases */
        smsNotificationQueueConsumerService.processNotification(notificationId);
        verifyAll();
    }
}