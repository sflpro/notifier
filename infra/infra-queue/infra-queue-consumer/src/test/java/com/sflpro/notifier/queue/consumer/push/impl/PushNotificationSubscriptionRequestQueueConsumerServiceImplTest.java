package com.sflpro.notifier.queue.consumer.push.impl;

import com.sflpro.notifier.queue.consumer.test.AbstractQueueConsumerUnitTest;
import com.sflpro.notifier.services.notification.push.PushNotificationSubscriptionRequestProcessingService;
import org.easymock.Mock;
import org.easymock.TestSubject;
import org.junit.Test;

import static org.easymock.EasyMock.expect;
import static org.junit.Assert.fail;

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
