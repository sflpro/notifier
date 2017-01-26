package com.sfl.nms.queue.producer.notification.push.impl;

import com.sfl.nms.queue.amqp.model.notification.push.PushNotificationSubscriptionRequestRPCTransferModel;
import com.sfl.nms.queue.amqp.rpc.RPCCallType;
import com.sfl.nms.queue.producer.AbstractQueueProducerUnitTest;
import com.sfl.nms.queue.producer.connector.AmqpConnectorService;
import com.sfl.nms.queue.producer.connector.AmqpResponseHandler;
import static org.easymock.EasyMock.*;
import org.easymock.Mock;
import org.easymock.TestSubject;
import static org.junit.Assert.*;
import org.junit.Test;

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
