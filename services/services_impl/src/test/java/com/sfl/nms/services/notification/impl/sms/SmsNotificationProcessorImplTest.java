package com.sfl.nms.services.notification.impl.sms;

import com.sfl.nms.externalclients.sms.twillio.communicator.TwillioApiCommunicator;
import com.sfl.nms.externalclients.sms.twillio.exception.TwillioClientRuntimeException;
import com.sfl.nms.externalclients.sms.twillio.model.request.SendMessageRequest;
import com.sfl.nms.externalclients.sms.twillio.model.response.SendMessageResponse;
import com.sfl.nms.persistence.utility.PersistenceUtilityService;
import com.sfl.nms.services.common.exception.ServicesRuntimeException;
import com.sfl.nms.services.notification.model.NotificationState;
import com.sfl.nms.services.notification.model.sms.SmsNotification;
import com.sfl.nms.services.notification.sms.SmsNotificationService;
import com.sfl.nms.services.test.AbstractServicesUnitTest;
import com.twilio.sdk.TwilioRestException;
import static org.easymock.EasyMock.*;
import org.easymock.IAnswer;
import org.easymock.Mock;
import org.easymock.TestSubject;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

/**
 * User: Mher Sargsyan
 * Company: SFL LLC
 * Date: 4/10/15
 * Time: 1:12 PM
 */
public class SmsNotificationProcessorImplTest extends AbstractServicesUnitTest {

    /* Constants */
    private static final String SENDER_PHONE_NUMBER = "+105050509";

    /* Test subject and mocks */
    @TestSubject
    private SmsNotificationProcessorImpl smsMessageProcessingService = new SmsNotificationProcessorImpl();

    @Mock
    private SmsNotificationService smsNotificationService;

    @Mock
    private PersistenceUtilityService persistenceUtilityService;

    @Mock
    private TwillioApiCommunicator twillioApiCommunicator;

    /* Constructors */
    public SmsNotificationProcessorImplTest() {
    }

    @Before
    public void setTestSubjectProperties() {
        smsMessageProcessingService.setAccountSenderNumber(SENDER_PHONE_NUMBER);
    }

    /* Test methods */
    @Test
    public void testProcessSmsMessageWithInvalidArguments() {
        /* Test data */
        /* Reset mocks */
        resetAll();
        /* replay mocks */
        replayAll();
        /* Run test cases */
        try {
            smsMessageProcessingService.processNotification(null);
            fail("Exception");
        } catch (final IllegalArgumentException e) {
            //Exception
        }
        verifyAll();
    }

    @Test
    public void testProcessSmsMessageWithInvalidNotificationState() {
        /* Test data */
        final Long notificationId = 1l;
        final SmsNotification smsNotification = getServicesImplTestHelper().createSmsNotification();
        smsNotification.setId(notificationId);
        smsNotification.setState(NotificationState.PROCESSING);
        /* Reset mocks */
        resetAll();
        /* Register expectations */
        expect(smsNotificationService.getNotificationById(eq(notificationId))).andReturn(smsNotification).once();
        /* Replay mocks */
        replayAll();
        /* Run test cases */
        try {
            smsMessageProcessingService.processNotification(notificationId);
            fail("Exception");
        } catch (final IllegalArgumentException e) {
            //Exception
        }
        verifyAll();
    }

    @Test
    public void testProcessSmsMessageWithSmsClientRuntimeException() {
        /* Test data */
        final Long notificationId = 1l;
        final SmsNotification smsNotification = getServicesImplTestHelper().createSmsNotification();
        smsNotification.setId(notificationId);
        /* Reset mocks */
        resetAll();
        /* Register expectations */
        expect(smsNotificationService.getNotificationById(eq(notificationId))).andReturn(smsNotification).once();
        persistenceUtilityService.runInNewTransaction(isA(Runnable.class));
        expectLastCall().andAnswer(() -> {
            final Runnable runnable = (Runnable) getCurrentArguments()[0];
            runnable.run();
            return null;
        }).times(2);
        expect(smsNotificationService.updateNotificationState(eq(notificationId), eq(NotificationState.PROCESSING))).andReturn(smsNotification).once();
        expect(twillioApiCommunicator.sendMessage(isA(SendMessageRequest.class))).andAnswer(new IAnswer<SendMessageResponse>() {
            @Override
            public SendMessageResponse answer() {
                final SendMessageRequest sendMessageRequest = (SendMessageRequest) getCurrentArguments()[0];
                assertSendMessageRequest(sendMessageRequest, smsNotification);
                throw new TwillioClientRuntimeException(sendMessageRequest.getSenderNumber(), sendMessageRequest.getRecipientNumber(), sendMessageRequest.getMessageBody(), new TwilioRestException("message", 1));
            }
        }).once();
        expect(smsNotificationService.updateNotificationState(eq(notificationId), eq(NotificationState.FAILED))).andReturn(smsNotification).once();
        /* Replay mocks */
        replayAll();
        /* Run test cases */
        try {
            smsMessageProcessingService.processNotification(notificationId);
            fail("Exception");
        } catch (final ServicesRuntimeException e) {
            //Exception
        }
        verifyAll();
    }

    @Test
    public void testProcessSmsMessage() {
        /* Test data */
        final Long notificationId = 1l;
        final SmsNotification smsNotification = getServicesImplTestHelper().createSmsNotification();
        smsNotification.setId(notificationId);
        final String messageId = "UG&IYTCTCYCG:H*YYFUTYRFGJ";
        /* Reset mocks */
        resetAll();
        /* Register expectations */
        expect(smsNotificationService.getNotificationById(eq(notificationId))).andReturn(smsNotification).once();
        persistenceUtilityService.runInNewTransaction(isA(Runnable.class));
        expectLastCall().andAnswer(() -> {
            final Runnable runnable = (Runnable) getCurrentArguments()[0];
            runnable.run();
            return null;
        }).times(3);
        expect(smsNotificationService.updateNotificationState(eq(notificationId), eq(NotificationState.PROCESSING))).andReturn(smsNotification).once();
        expect(twillioApiCommunicator.sendMessage(isA(SendMessageRequest.class))).andAnswer(new IAnswer<SendMessageResponse>() {
            @Override
            public SendMessageResponse answer() {
                final SendMessageRequest sendMessageRequest = (SendMessageRequest) getCurrentArguments()[0];
                assertSendMessageRequest(sendMessageRequest, smsNotification);
                return new SendMessageResponse(messageId, sendMessageRequest.getRecipientNumber(), sendMessageRequest.getMessageBody());
            }
        }).once();
        expect(smsNotificationService.updateProviderExternalUuid(eq(notificationId), eq(messageId))).andReturn(smsNotification).once();
        expect(smsNotificationService.updateNotificationState(eq(notificationId), eq(NotificationState.SENT))).andReturn(smsNotification).once();
        /* Replay mocks */
        replayAll();
        /* Run test cases */
        smsMessageProcessingService.processNotification(notificationId);
        verifyAll();
    }

    /* Utility methods */
    private void assertSendMessageRequest(final SendMessageRequest sendMessageRequest, final SmsNotification smsNotification) {
        assertNotNull(sendMessageRequest);
        assertEquals(sendMessageRequest.getMessageBody(), smsNotification.getContent());
        assertEquals(sendMessageRequest.getRecipientNumber(), smsNotification.getRecipientMobileNumber());
    }
}
