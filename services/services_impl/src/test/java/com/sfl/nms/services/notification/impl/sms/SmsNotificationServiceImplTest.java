package com.sfl.nms.services.notification.impl.sms;

import com.sfl.nms.services.notification.dto.sms.SmsNotificationDto;
import com.sfl.nms.services.notification.impl.AbstractNotificationServiceImpl;
import com.sfl.nms.services.notification.model.sms.SmsNotification;
import com.sfl.nms.persistence.repositories.notification.AbstractNotificationRepository;
import com.sfl.nms.persistence.repositories.notification.sms.SmsNotificationRepository;
import com.sfl.nms.services.notification.impl.AbstractNotificationServiceImplTest;
import static org.easymock.EasyMock.*;
import org.easymock.Mock;
import org.easymock.TestSubject;
import static org.junit.Assert.*;
import org.junit.Test;

/**
 * User: Ruben Dilanyan
 * Company: SFL LLC
 * Date: 3/21/15
 * Time: 8:30 PM
 */
public class SmsNotificationServiceImplTest extends AbstractNotificationServiceImplTest<SmsNotification> {

    /* Test subject and mocks */
    @TestSubject
    private SmsNotificationServiceImpl smsNotificationService = new SmsNotificationServiceImpl();

    @Mock
    private SmsNotificationRepository smsNotificationRepository;

    /* Constructors */
    public SmsNotificationServiceImplTest() {
    }

    /* Test methods */
    @Test
    public void testCreateEmailNotificationWithInvalidArguments() {
        // Test data
        final SmsNotificationDto smsNotificationDto = getServicesImplTestHelper().createSmsNotificationDto();
        // Reset
        resetAll();
        // Replay
        replayAll();
        // Run test scenario
        try {
            smsNotificationService.createSmsNotification(null);
            fail("Exception should be thrown");
        } catch (final IllegalArgumentException ex) {
            // Expected
        }
        try {
            smsNotificationService.createSmsNotification(new SmsNotificationDto(null, smsNotificationDto.getProviderType(), smsNotificationDto.getContent(), smsNotificationDto.getClientIpAddress()));
            fail("Exception should be thrown");
        } catch (final IllegalArgumentException ex) {
            // Expected
        }
        try {
            smsNotificationService.createSmsNotification(new SmsNotificationDto(smsNotificationDto.getRecipientMobileNumber(), null, smsNotificationDto.getContent(), smsNotificationDto.getClientIpAddress()));
            fail("Exception should be thrown");
        } catch (final IllegalArgumentException ex) {
            // Expected
        }
        try {
            smsNotificationService.createSmsNotification(new SmsNotificationDto(smsNotificationDto.getRecipientMobileNumber(), smsNotificationDto.getProviderType(), null, smsNotificationDto.getClientIpAddress()));
            fail("Exception should be thrown");
        } catch (final IllegalArgumentException ex) {
            // Expected
        }
        // Verify
        verifyAll();
    }

    @Test
    public void testCreateEmailNotification() {
        // Test data
        final SmsNotificationDto notificationDto = getServicesImplTestHelper().createSmsNotificationDto();
        // Reset
        resetAll();
        // Expectations
        expect(smsNotificationRepository.save(isA(SmsNotification.class))).andAnswer(() -> (SmsNotification) getCurrentArguments()[0]).once();
        // Replay
        replayAll();
        // Run test scenario
        final SmsNotification notification = smsNotificationService.createSmsNotification(notificationDto);
        getServicesImplTestHelper().assertSmsNotification(notification, notificationDto);
    }

    /* Utility methods */
    @Override
    protected AbstractNotificationRepository<SmsNotification> getRepository() {
        return smsNotificationRepository;
    }

    @Override
    protected AbstractNotificationServiceImpl<SmsNotification> getService() {
        return smsNotificationService;
    }

    @Override
    protected Class<SmsNotification> getInstanceClass() {
        return SmsNotification.class;
    }

    @Override
    protected SmsNotification getInstance() {
        return getServicesImplTestHelper().createSmsNotification();
    }

}
