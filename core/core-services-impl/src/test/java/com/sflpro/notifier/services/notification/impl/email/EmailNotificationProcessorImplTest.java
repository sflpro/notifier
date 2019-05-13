package com.sflpro.notifier.services.notification.impl.email;

import com.sflpro.notifier.db.entities.notification.NotificationProviderType;
import com.sflpro.notifier.db.entities.notification.NotificationState;
import com.sflpro.notifier.db.entities.notification.email.EmailNotification;
import com.sflpro.notifier.db.repositories.utility.PersistenceUtilityService;
import com.sflpro.notifier.services.common.exception.ServicesRuntimeException;
import com.sflpro.notifier.services.notification.email.EmailNotificationService;
import com.sflpro.notifier.services.notification.impl.email.mandrill.EmailNotificationMandrillProviderProcessor;
import com.sflpro.notifier.services.notification.impl.email.smtp.EmailNotificationSmtpProviderProcessor;
import com.sflpro.notifier.services.test.AbstractServicesUnitTest;
import org.easymock.Mock;
import org.easymock.TestSubject;
import org.junit.Test;

import static org.easymock.EasyMock.*;
import static org.junit.Assert.fail;

/**
 * User: Ruben Dilanyan
 * Company: SFL LLC
 * Date: 1/11/16
 * Time: 2:27 PM
 */
public class EmailNotificationProcessorImplTest extends AbstractServicesUnitTest {

    /* Test subject and mocks */
    @TestSubject
    private EmailNotificationProcessorImpl emailNotificationProcessor = new EmailNotificationProcessorImpl();

    @Mock
    private EmailNotificationService emailNotificationService;

    @Mock
    private PersistenceUtilityService persistenceUtilityService;

    @Mock
    private EmailNotificationSmtpProviderProcessor emailNotificationSmtpProviderProcessor;

    @Mock
    private EmailNotificationMandrillProviderProcessor emailNotificationMandrillProviderProcessor;


    /* Constructors */
    public EmailNotificationProcessorImplTest() {
    }

    /* Test methods */
    @Test
    public void testProcessNotificationWithInvalidArguments() {
        // Reset
        resetAll();
        // Replay
        replayAll();
        // Run test scenario
        try {
            emailNotificationProcessor.processNotification(null);
            fail("Exception should be thrown");
        } catch (final IllegalArgumentException ex) {
            // Expected
        }
        // Verify
        verifyAll();
    }

    @Test
    public void testProcessNotificationWithInvalidNotificationState() {
        // Test data
        final Long notificationId = 1L;
        final EmailNotification notification = getServicesImplTestHelper().createEmailNotification();
        notification.setId(notificationId);
        notification.setState(NotificationState.PROCESSING);
        notification.setProviderType(NotificationProviderType.SMTP_SERVER);
        // Reset
        resetAll();
        // Expectations
        expect(emailNotificationService.getNotificationById(eq(notificationId))).andReturn(notification).once();
        // Replay
        replayAll();
        // Run test scenario
        try {
            emailNotificationProcessor.processNotification(notificationId);
            fail("Exception should be thrown");
        } catch (final IllegalArgumentException ex) {
            // Expected
        }
        // Verify
        verifyAll();
    }

    @Test
    public void testProcessNotificationForWhenProviderTypeIsSmtpAndSuccess() {
        // Test data
        final Long notificationId = 1L;
        final EmailNotification notification = getServicesImplTestHelper().createEmailNotification();
        notification.setId(notificationId);
        notification.setState(NotificationState.CREATED);
        notification.setProviderType(NotificationProviderType.SMTP_SERVER);
        // Reset
        resetAll();
        // Expectations
        expect(emailNotificationService.getNotificationById(notificationId)).andReturn(notification).once();
        expect(emailNotificationService.updateNotificationState(notificationId, NotificationState.PROCESSING)).andReturn(notification).once();
        expect(emailNotificationSmtpProviderProcessor.processEmailNotification(notification)).andReturn(true).once();
        expect(emailNotificationService.updateNotificationState(notificationId, NotificationState.SENT)).andReturn(notification).once();
        persistenceUtilityService.runInNewTransaction(isA(Runnable.class));
        expectLastCall().andAnswer(() -> {
            final Runnable runnable = (Runnable) getCurrentArguments()[0];
            runnable.run();
            return null;
        }).anyTimes();
        // Replay
        replayAll();
        // Run test scenario
        emailNotificationProcessor.processNotification(notificationId);
        // Verify
        verifyAll();
    }

    @Test
    public void testProcessNotificationForWhenProviderTypeIsMandrillAndFail() {
        // Test data
        final Long notificationId = 1L;
        final EmailNotification notification = getServicesImplTestHelper().createEmailNotification();
        notification.setId(notificationId);
        notification.setState(NotificationState.CREATED);
        notification.setProviderType(NotificationProviderType.MANDRILL);
        // Reset
        resetAll();
        // Expectations
        expect(emailNotificationService.getNotificationById(notificationId)).andReturn(notification).once();
        expect(emailNotificationService.updateNotificationState(notificationId, NotificationState.PROCESSING)).andReturn(notification).once();
        expect(emailNotificationMandrillProviderProcessor.processEmailNotification(notification)).andReturn(false).once();
        expect(emailNotificationService.updateNotificationState(notificationId, NotificationState.FAILED)).andReturn(notification).once();
        persistenceUtilityService.runInNewTransaction(isA(Runnable.class));
        expectLastCall().andAnswer(() -> {
            final Runnable runnable = (Runnable) getCurrentArguments()[0];
            runnable.run();
            return null;
        }).anyTimes();
        // Replay
        replayAll();
        // Run test scenario
        emailNotificationProcessor.processNotification(notificationId);
        // Verify
        verifyAll();
    }

    @Test
    public void testProcessNotificationWhenErrorOccursDuringProcessing() {
        // Test data
        final Long notificationId = 1L;
        final EmailNotification notification = getServicesImplTestHelper().createEmailNotification();
        notification.setId(notificationId);
        notification.setState(NotificationState.CREATED);
        // Reset
        resetAll();
        // Expectations
        expect(emailNotificationService.getNotificationById(notificationId)).andReturn(notification).once();
        expect(emailNotificationService.updateNotificationState(notificationId, NotificationState.PROCESSING)).andReturn(notification).once();
        expect(emailNotificationSmtpProviderProcessor.processEmailNotification(notification)).andThrow(new ServicesRuntimeException("Runtime exception")).once();
        expect(emailNotificationService.updateNotificationState(notificationId, NotificationState.FAILED)).andReturn(notification).once();
        persistenceUtilityService.runInNewTransaction(isA(Runnable.class));
        expectLastCall().andAnswer(() -> {
            final Runnable runnable = (Runnable) getCurrentArguments()[0];
            runnable.run();
            return null;
        }).anyTimes();
        // Replay
        replayAll();
        try {
            // Run test scenario
            emailNotificationProcessor.processNotification(notificationId);
            fail("Exception should be thrown");
        } catch (final ServicesRuntimeException ex) {
            // Expected
        }
        // Verify
        verifyAll();
    }
}
