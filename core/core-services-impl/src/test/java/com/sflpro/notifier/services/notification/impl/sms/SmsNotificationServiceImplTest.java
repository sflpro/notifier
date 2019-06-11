package com.sflpro.notifier.services.notification.impl.sms;

import com.sflpro.notifier.db.entities.notification.sms.SmsNotification;
import com.sflpro.notifier.db.repositories.repositories.notification.AbstractNotificationRepository;
import com.sflpro.notifier.db.repositories.repositories.notification.sms.SmsNotificationRepository;
import com.sflpro.notifier.services.notification.dto.sms.SmsNotificationDto;
import com.sflpro.notifier.services.notification.dto.sms.SmsNotificationPropertyDto;
import com.sflpro.notifier.services.notification.impl.AbstractNotificationServiceImpl;
import com.sflpro.notifier.services.notification.impl.AbstractNotificationServiceImplTest;
import org.easymock.Mock;
import org.easymock.TestSubject;
import org.junit.Test;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.easymock.EasyMock.*;

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
        final List<SmsNotificationPropertyDto> properties = getServicesImplTestHelper().createSmsNotificationPropertyDtos(2);
        // Reset
        resetAll();
        // Replay
        replayAll();
        // Run test scenario
        assertThatThrownBy(() -> smsNotificationService.createSmsNotification(null, properties)).isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> smsNotificationService.createSmsNotification(smsNotificationDto, null)).isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> smsNotificationService.createSmsNotification(new SmsNotificationDto(
                        null,
                        smsNotificationDto.getProviderType(),
                        smsNotificationDto.getContent(),
                        smsNotificationDto.getClientIpAddress(),
                        smsNotificationDto.getTemplateName()),
                properties)
        ).isInstanceOf(IllegalArgumentException.class);

        assertThatThrownBy(() -> smsNotificationService.createSmsNotification(new SmsNotificationDto(
                        smsNotificationDto.getRecipientMobileNumber(),
                        null,
                        smsNotificationDto.getContent(),
                        smsNotificationDto.getClientIpAddress(),
                        smsNotificationDto.getTemplateName()),
                properties)
        ).isInstanceOf(IllegalArgumentException.class);
        // Verify
        verifyAll();
    }

    @Test
    public void testCreateEmailNotification() {
        // Test data
        final SmsNotificationDto notificationDto = getServicesImplTestHelper().createSmsNotificationDto();
        final List<SmsNotificationPropertyDto> properties = getServicesImplTestHelper().createSmsNotificationPropertyDtos(2);
        // Reset
        resetAll();
        // Expectations
        expect(smsNotificationRepository.save(isA(SmsNotification.class))).andAnswer(() -> (SmsNotification) getCurrentArguments()[0]).once();
        // Replay
        replayAll();
        // Run test scenario
        final SmsNotification notification = smsNotificationService.createSmsNotification(notificationDto, properties);
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
