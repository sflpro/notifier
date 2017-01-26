package com.sfl.nms.queue.producer.notification.common.impl;

import com.sfl.nms.queue.amqp.model.notification.NotificationRPCTransferModel;
import com.sfl.nms.queue.amqp.rpc.RPCCallType;
import com.sfl.nms.queue.producer.AbstractQueueProducerUnitTest;
import com.sfl.nms.queue.producer.connector.AmqpConnectorService;
import com.sfl.nms.queue.producer.connector.AmqpResponseHandler;
import org.easymock.Mock;
import org.easymock.TestSubject;
import org.junit.Test;

import static org.easymock.EasyMock.*;
import static org.junit.Assert.fail;

/**
 * User: Mher Sargsyan
 * Company: SFL LLC
 * Date: 4/10/15
 * Time: 8:06 PM
 */
public class NotificationQueueProducerServiceImplTest extends AbstractQueueProducerUnitTest {

    /* Test subject and mocks */
    @TestSubject
    private NotificationQueueProducerServiceImpl smsNotificationQueueProducerService = new NotificationQueueProducerServiceImpl();

    @Mock
    private AmqpConnectorService amqpConnectorService;

    /* Constructors */
    public NotificationQueueProducerServiceImplTest() {
    }

    /* Test methods */
    @Test
    public void testProcessStartSendingSmsMessageEventWithInvalidArguments() {
        /* Test data */
        /* Reset mocks */
        resetAll();
        /* Replay mocks */
        replayAll();
        /* Test cases */
        try {
            smsNotificationQueueProducerService.processStartSendingNotificationEvent(null);
            fail("Exception will be thrown");
        } catch (final IllegalArgumentException e) {
            // Exception
        }
        verifyAll();
    }

    @Test
    public void testProcessStartSendingSmsMessageEvent() {
        /* Test data */
        final Long notificationId = 1L;
        /* Reset mocks */
        resetAll();
        /* Register expectations */
        amqpConnectorService.publishMessage(eq(RPCCallType.START_NOTIFICATION_PROCESSING), isA(NotificationRPCTransferModel.class), eq(NotificationRPCTransferModel.class), isA(AmqpResponseHandler.class));
        expectLastCall().once();
        /* Replay mocks */
        replayAll();
        /* Test cases */
        smsNotificationQueueProducerService.processStartSendingNotificationEvent(notificationId);
        verifyAll();
    }
}
