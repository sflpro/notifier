package com.sflpro.notifier.queue.producer.notification.common.impl;

import com.sflpro.notifier.db.entities.notification.NotificationSendingPriority;
import com.sflpro.notifier.queue.amqp.model.notification.NotificationRPCTransferModel;
import com.sflpro.notifier.queue.amqp.rpc.RPCCallType;
import com.sflpro.notifier.queue.producer.AbstractQueueProducerUnitTest;
import com.sflpro.notifier.queue.producer.connector.AmqpConnectorService;
import com.sflpro.notifier.queue.producer.connector.AmqpResponseHandler;
import org.easymock.Mock;
import org.easymock.TestSubject;
import org.junit.Test;

import java.util.Collections;

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
            smsNotificationQueueProducerService.processStartSendingNotificationEvent(null, NotificationSendingPriority.NORMAL, Collections.emptyMap());
            fail("Exception will be thrown");
        } catch (final IllegalArgumentException e) {
            // Exception
        }
        try {
            final Long notificationId = 1L;
            smsNotificationQueueProducerService.processStartSendingNotificationEvent(notificationId, NotificationSendingPriority.NORMAL, null);
            fail("Exception will be thrown");
        } catch (final IllegalArgumentException e) {
            // Exception
        }
        try {
            final Long notificationId = 1L;
            smsNotificationQueueProducerService.processStartSendingNotificationEvent(notificationId, null, Collections.emptyMap());
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
        final NotificationSendingPriority sendingPriority = NotificationSendingPriority.HIGH;
        /* Reset mocks */
        resetAll();
        /* Register expectations */
        amqpConnectorService.publishMessage(eq(RPCCallType.START_NOTIFICATION_PROCESSING), isA(NotificationRPCTransferModel.class), eq(sendingPriority), eq(NotificationRPCTransferModel.class), isA(AmqpResponseHandler.class));
        expectLastCall().once();
        /* Replay mocks */
        replayAll();
        /* Test cases */
        smsNotificationQueueProducerService.processStartSendingNotificationEvent(notificationId, sendingPriority, Collections.emptyMap());
        verifyAll();
    }
}
