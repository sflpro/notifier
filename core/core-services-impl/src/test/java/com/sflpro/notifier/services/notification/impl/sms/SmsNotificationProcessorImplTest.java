package com.sflpro.notifier.services.notification.impl.sms;

import com.google.common.collect.Lists;
import com.sflpro.notifier.db.entities.notification.NotificationState;
import com.sflpro.notifier.db.entities.notification.email.NotificationProperty;
import com.sflpro.notifier.db.entities.notification.sms.SmsNotification;
import com.sflpro.notifier.db.repositories.utility.PersistenceUtilityService;
import com.sflpro.notifier.services.common.exception.ServicesRuntimeException;
import com.sflpro.notifier.services.notification.sms.SmsNotificationService;
import com.sflpro.notifier.services.test.AbstractServicesUnitTest;
import com.sflpro.notifier.spi.sms.*;
import org.easymock.Mock;
import org.junit.Before;
import org.junit.Test;

import java.util.Collections;
import java.util.Optional;
import java.util.stream.Collectors;

import static junit.framework.TestCase.assertEquals;
import static org.assertj.core.api.Assertions.assertThat;
import static org.easymock.EasyMock.*;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;


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

    @Mock
    private TemplatedSmsSender templatedSmsSender;

    private final String senderName = uuid();

    @Before
    public void prepare() {
        smsMessageProcessingService = new SmsNotificationProcessorImpl(
                smsNotificationService,
                smsSenderProvider,
                persistenceUtilityService,
                senderName
        );
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
            checkSmsMessageBasicProps(message, smsNotification);
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
    public void testProcessSimpleSmsMessage() {
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
            checkSmsMessageBasicProps(message, smsNotification);
            assertEquals(message.messageBody(), smsNotification.getContent());
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

    @Test
    public void testProcessTemplatedSmsMessage() {
        /* Test data */
        final Long notificationId = 1l;
        final SmsNotification smsNotification = getServicesImplTestHelper().createSmsNotification();
        smsNotification.setProperties(
                Lists.newArrayList(
                        new NotificationProperty(uuid(), uuid()),
                        new NotificationProperty(uuid(), uuid())
                )
        );
        smsNotification.setTemplateName(uuid());
        smsNotification.setId(notificationId);
        final String messageId = "UG&IYTCTCYCG:H*YYFUTYRFGJ";
        /* Reset mocks */
        resetAll();
        /* Register expectations */
        expect(smsNotificationService.getNotificationById(notificationId)).andReturn(smsNotification).once();
        expect(persistenceUtilityService.initializeAndUnProxy(smsNotification)).andReturn(smsNotification);
        expect(smsNotificationService.updateNotificationState(notificationId, NotificationState.PROCESSING)).andReturn(smsNotification).once();
        expect(smsSenderProvider.lookupTemplatedSmsMessageSenderFor(smsNotification.getProviderType().name().toLowerCase())).andReturn(Optional.of(templatedSmsSender));
        expect(templatedSmsSender.send(isA(TemplatedSmsMessage.class))).andAnswer(() -> {
            final TemplatedSmsMessage message = (TemplatedSmsMessage) getCurrentArguments()[0];
            checkSmsMessageBasicProps(message, smsNotification);
            assertEquals(message.templateId(), smsNotification.getTemplateName());
            assertThat(message.variables()).isEqualTo(smsNotification.getProperties().stream().collect(
                    Collectors.toMap(NotificationProperty::getPropertyKey, NotificationProperty::getPropertyValue))
            );
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

    private void checkSmsMessageBasicProps(final SmsMessage message, final SmsNotification smsNotification) {
        assertNotNull(message);
        assertEquals(message.recipientNumber(), smsNotification.getRecipientMobileNumber());
        assertThat(message.internalId()).isEqualTo(smsNotification.getId());
        assertThat(message.sender()).isEqualTo(senderName);
    }

}
