package com.sflpro.notifier.services.notification.impl.sms;

import com.sflpro.notifier.db.entities.notification.NotificationState;
import com.sflpro.notifier.db.entities.notification.sms.SmsNotification;
import com.sflpro.notifier.db.repositories.utility.PersistenceUtilityService;
import com.sflpro.notifier.services.common.exception.ServicesRuntimeException;
import com.sflpro.notifier.sms.SmsMessage;
import com.sflpro.notifier.sms.SmsMessageSendingResult;
import com.sflpro.notifier.sms.SmsSender;
import com.sflpro.notifier.services.notification.sms.SmsNotificationService;
import com.sflpro.notifier.services.test.AbstractServicesUnitTest;
import org.easymock.IAnswer;
import org.easymock.Mock;
import org.easymock.TestSubject;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Collections;
import java.util.Optional;

import static org.easymock.EasyMock.*;
import static org.junit.Assert.*;

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
    private SmsSenderProvider smsSenderProvider;

    @Mock
    private SmsSender smsSender;

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
            smsMessageProcessingService.processNotification(null, Collections.emptyMap());
            fail("Exception");
        } catch (final IllegalArgumentException e) {
            //Exception
        }
        try {
            final Long notificationId = 1L;
            smsMessageProcessingService.processNotification(notificationId, null);
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
            smsMessageProcessingService.processNotification(notificationId, Collections.emptyMap());
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
        expect(smsNotificationService.updateNotificationState(eq(notificationId), eq(NotificationState.PROCESSING))).andReturn(smsNotification).once();
        expect(smsSenderProvider.lookupSenderFor(smsNotification.getProviderType().name().toLowerCase())).andReturn(Optional.of(smsSender));
        expect(smsSender.send(isA(SmsMessage.class))).andAnswer(new IAnswer<SmsMessageSendingResult>() {
            @Override
            public SmsMessageSendingResult answer() {
                final SmsMessage message = (SmsMessage) getCurrentArguments()[0];
                assertSendMessageRequest(message, smsNotification);
                throw new ServicesRuntimeException("Failed to send message");
            }
        }).once();
        expect(smsNotificationService.updateNotificationState(eq(notificationId), eq(NotificationState.FAILED))).andReturn(smsNotification).once();
        /* Replay mocks */
        replayAll();
        /* Run test cases */
        try {
            smsMessageProcessingService.processNotification(notificationId, Collections.emptyMap());
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
        expect(smsNotificationService.updateNotificationState(eq(notificationId), eq(NotificationState.PROCESSING))).andReturn(smsNotification).once();
        expect(smsSenderProvider.lookupSenderFor(smsNotification.getProviderType().name().toLowerCase())).andReturn(Optional.of(smsSender));
        expect(smsSender.send(isA(SmsMessage.class))).andAnswer(new IAnswer<SmsMessageSendingResult>() {
            @Override
            public SmsMessageSendingResult answer() {
                final SmsMessage message = (SmsMessage) getCurrentArguments()[0];
                assertSendMessageRequest(message, smsNotification);
                return SmsMessageSendingResult.of(messageId);
            }
        }).once();
        expect(smsNotificationService.updateProviderExternalUuid(eq(notificationId), eq(messageId))).andReturn(smsNotification).once();
        expect(smsNotificationService.updateNotificationState(eq(notificationId), eq(NotificationState.SENT))).andReturn(smsNotification).once();
        /* Replay mocks */
        replayAll();
        /* Run test cases */
        smsMessageProcessingService.processNotification(notificationId, Collections.emptyMap());
        verifyAll();
    }

    /* Utility methods */
    private static void assertSendMessageRequest(final SmsMessage sendMessageRequest, final SmsNotification smsNotification) {
        assertNotNull(sendMessageRequest);
        Assert.assertEquals(sendMessageRequest.messageBody(), smsNotification.getContent());
        assertEquals(sendMessageRequest.recipientNumber(), smsNotification.getRecipientMobileNumber());
    }
}
