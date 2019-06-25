package com.sflpro.notifier.services.notification.impl.sms;

import com.sflpro.notifier.db.entities.notification.NotificationState;
import com.sflpro.notifier.db.entities.notification.sms.SmsNotification;
import com.sflpro.notifier.db.repositories.utility.PersistenceUtilityService;
import com.sflpro.notifier.services.common.exception.ServicesRuntimeException;
import com.sflpro.notifier.services.notification.sms.SmsNotificationService;
import com.sflpro.notifier.services.test.AbstractServicesUnitTest;
import com.sflpro.notifier.spi.sms.SimpleSmsMessage;
import com.sflpro.notifier.spi.sms.SimpleSmsSender;
import com.sflpro.notifier.spi.sms.SmsMessageSendingResult;
import org.easymock.Mock;
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
    private SmsNotificationProcessorImpl smsMessageProcessingService;

    @Mock
    private SmsNotificationService smsNotificationService;

    @Mock
    private PersistenceUtilityService persistenceUtilityService;

    @Mock
    private SmsSenderProvider smsSenderProvider;

    @Mock
    private SimpleSmsSender simpleSmsSender;

    /* Constructors */
    public SmsNotificationProcessorImplTest() {
    }

    @Before
    public void prepare() {
        smsMessageProcessingService = new SmsNotificationProcessorImpl(smsNotificationService,
                smsSenderProvider,
                persistenceUtilityService,
                "Test");
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
        expect(smsNotificationService.getNotificationById(notificationId)).andReturn(smsNotification).once();
        expect(persistenceUtilityService.initializeAndUnProxy(smsNotification)).andReturn(smsNotification);
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
        expect(smsNotificationService.getNotificationById(notificationId)).andReturn(smsNotification).once();
        expect(persistenceUtilityService.initializeAndUnProxy(smsNotification)).andReturn(smsNotification);
        expect(smsNotificationService.updateNotificationState(notificationId, NotificationState.PROCESSING)).andReturn(smsNotification).once();
        expect(smsSenderProvider.lookupSimpleSmsMessageSenderFor(smsNotification.getProviderType().name().toLowerCase())).andReturn(Optional.of(simpleSmsSender));
        expect(simpleSmsSender.send(isA(SimpleSmsMessage.class))).andAnswer(() -> {
            final SimpleSmsMessage message = (SimpleSmsMessage) getCurrentArguments()[0];
            assertSendMessageRequest(message, smsNotification);
            throw new ServicesRuntimeException("Failed to send message");
        }).once();
        expect(smsNotificationService.updateNotificationState(notificationId, NotificationState.FAILED)).andReturn(smsNotification).once();
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
        expect(smsNotificationService.getNotificationById(notificationId)).andReturn(smsNotification).once();
        expect(persistenceUtilityService.initializeAndUnProxy(smsNotification)).andReturn(smsNotification);
        expect(smsNotificationService.updateNotificationState(notificationId, NotificationState.PROCESSING)).andReturn(smsNotification).once();
        expect(smsSenderProvider.lookupSimpleSmsMessageSenderFor(smsNotification.getProviderType().name().toLowerCase())).andReturn(Optional.of(simpleSmsSender));
        expect(simpleSmsSender.send(isA(SimpleSmsMessage.class))).andAnswer(() -> {
            final SimpleSmsMessage message = (SimpleSmsMessage) getCurrentArguments()[0];
            assertSendMessageRequest(message, smsNotification);
            return SmsMessageSendingResult.of(messageId);
        }).once();
        expect(smsNotificationService.updateProviderExternalUuid(notificationId, messageId)).andReturn(smsNotification).once();
        expect(smsNotificationService.updateNotificationState(notificationId, NotificationState.SENT)).andReturn(smsNotification).once();
        /* Replay mocks */
        replayAll();
        /* Run test cases */
        smsMessageProcessingService.processNotification(notificationId, Collections.emptyMap());
        verifyAll();
    }

    /* Utility methods */
    private static void assertSendMessageRequest(final SimpleSmsMessage sendMessageRequest, final SmsNotification smsNotification) {
        assertNotNull(sendMessageRequest);
        assertEquals(sendMessageRequest.messageBody(), smsNotification.getContent());
        assertEquals(sendMessageRequest.recipientNumber(), smsNotification.getRecipientMobileNumber());
    }
}
