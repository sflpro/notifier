package com.sfl.nms.queue.consumer.notification.push.impl;

import com.sfl.nms.queue.consumer.test.AbstractQueueConsumerUnitTest;
import com.sfl.nms.services.notification.push.PushNotificationSubscriptionRequestProcessingService;
import static org.easymock.EasyMock.*;
import org.easymock.Mock;
import org.easymock.TestSubject;
import static org.junit.Assert.*;
import org.junit.Test;

/**
 * User: Ruben Dilanyan
 * Company: SFL LLC
 * Date: 8/22/15
 * Time: 8:22 PM
 */
public class PushNotificationSubscriptionRequestQueueConsumerServiceImplTest extends AbstractQueueConsumerUnitTest {

    /* Test subject and mocks */
    @TestSubject
    private PushNotificationSubscriptionRequestQueueConsumerServiceImpl pushNotificationSubscriptionRequestQueueConsumerService = new PushNotificationSubscriptionRequestQueueConsumerServiceImpl();

    @Mock
    private PushNotificationSubscriptionRequestProcessingService pushNotificationSubscriptionRequestProcessingService;

    /* Constructors */
    public PushNotificationSubscriptionRequestQueueConsumerServiceImplTest() {
    }

    /* Test methods */
    @Test
    public void testProcessPushNotificationSubscriptionRequestWithInvalidArguments() {
        // Reset
        resetAll();
        // Replay
        replayAll();
        // Run test scenario
        try {
            pushNotificationSubscriptionRequestQueueConsumerService.processPushNotificationSubscriptionRequest(null);
            fail("Exception should be thrown");
        } catch (final IllegalArgumentException ex) {
            // Expected
        }
        // Verify
        verifyAll();
    }

    @Test
    public void testProcessPushNotificationSubscriptionRequest() {
        // Test data
        final Long requestId = 1L;
        // Reset
        resetAll();
        // Expectations
        expect(pushNotificationSubscriptionRequestProcessingService.processPushNotificationSubscriptionRequest(requestId)).andReturn(null).once();
        // Replay
        replayAll();
        // Run test scenario
        pushNotificationSubscriptionRequestQueueConsumerService.processPushNotificationSubscriptionRequest(requestId);
        // Verify
        verifyAll();
    }
}
