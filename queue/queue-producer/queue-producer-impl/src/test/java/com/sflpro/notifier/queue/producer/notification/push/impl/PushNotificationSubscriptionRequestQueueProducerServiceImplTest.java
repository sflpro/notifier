package com.sflpro.notifier.queue.producer.notification.push.impl;

import com.sflpro.notifier.queue.amqp.model.notification.push.PushNotificationSubscriptionRequestRPCTransferModel;
import com.sflpro.notifier.queue.amqp.rpc.RPCCallType;
import com.sflpro.notifier.queue.producer.AbstractQueueProducerUnitTest;
import com.sflpro.notifier.queue.producer.connector.AmqpConnectorService;
import com.sflpro.notifier.queue.producer.connector.AmqpResponseHandler;
import org.easymock.Mock;
import org.easymock.TestSubject;
import org.junit.Test;

import static org.easymock.EasyMock.*;
import static org.junit.Assert.fail;

/**
 * User: Ruben Dilanyan
 * Company: SFL LLC
 * Date: 4/10/15
 * Time: 8:06 PM
 */
public class PushNotificationSubscriptionRequestQueueProducerServiceImplTest extends AbstractQueueProducerUnitTest {

    /* Test subject and mocks */
    @TestSubject
    private PushNotificationSubscriptionRequestQueueProducerServiceImpl pushNotificationSubscriptionRequestQueueProducerService = new PushNotificationSubscriptionRequestQueueProducerServiceImpl();

    @Mock
    private AmqpConnectorService amqpConnectorService;

    /* Constructors */
    public PushNotificationSubscriptionRequestQueueProducerServiceImplTest() {
    }

    /* Test methods */
    @Test
    public void testProcessPushNotificationSubscriptionRequestArguments() {
        /* Test data */
        /* Reset mocks */
        resetAll();
        /* Replay mocks */
        replayAll();
        /* Test cases */
        try {
            pushNotificationSubscriptionRequestQueueProducerService.processPushNotificationSubscriptionRequest(null);
            fail("Exception will be thrown");
        } catch (final IllegalArgumentException e) {
            // Exception
        }
        verifyAll();
    }

    @Test
    public void testProcessPushNotificationSubscriptionRequest() {
        /* Test data */
        final Long requestId = 1L;
        /* Reset mocks */
        resetAll();
        /* Register expectations */
        amqpConnectorService.publishMessage(eq(RPCCallType.START_PUSH_NOTIFICATION_SUBSCRIPTION_PROCESSING), isA(PushNotificationSubscriptionRequestRPCTransferModel.class), eq(PushNotificationSubscriptionRequestRPCTransferModel.class), isA(AmqpResponseHandler.class));
        expectLastCall().once();
        /* Replay mocks */
        replayAll();
        /* Test cases */
        pushNotificationSubscriptionRequestQueueProducerService.processPushNotificationSubscriptionRequest(requestId);
        verifyAll();
    }
}
