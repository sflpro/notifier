package com.sflpro.notifier.services.notification.impl.sms;

import com.sflpro.notifier.db.entities.notification.UserNotification;
import com.sflpro.notifier.db.entities.notification.sms.SmsNotification;
import com.sflpro.notifier.db.entities.user.User;
import com.sflpro.notifier.db.repositories.repositories.notification.AbstractNotificationRepository;
import com.sflpro.notifier.db.repositories.repositories.notification.sms.SmsNotificationRepository;
import com.sflpro.notifier.services.notification.UserNotificationService;
import com.sflpro.notifier.services.notification.dto.UserNotificationDto;
import com.sflpro.notifier.services.notification.dto.sms.SmsNotificationDto;
import com.sflpro.notifier.services.notification.dto.sms.SmsNotificationPropertyDto;
import com.sflpro.notifier.services.notification.event.sms.StartSendingNotificationEvent;
import com.sflpro.notifier.services.notification.impl.AbstractNotificationServiceImpl;
import com.sflpro.notifier.services.notification.impl.AbstractNotificationServiceImplTest;
import com.sflpro.notifier.services.system.event.ApplicationEventDistributionService;
import com.sflpro.notifier.services.user.UserService;
import org.easymock.Mock;
import org.easymock.TestSubject;
import org.junit.Test;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.isA;

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

    @Mock
    private ApplicationEventDistributionService applicationEventDistributionService;

    @Mock
    private UserService userService;

    @Mock
    private UserNotificationService userNotificationService;

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
    public void testCreateSmsNotification_WhenUserUuidNull() {
        // Test data
        final SmsNotificationDto notificationDto = getServicesImplTestHelper().createSmsNotificationDto();
        final List<SmsNotificationPropertyDto> properties = getServicesImplTestHelper().createSmsNotificationPropertyDtos(2);
        final SmsNotification smsNotification = getServicesImplTestHelper().createSmsNotification();
        smsNotification.setId(2L);
        // Reset
        resetAll();
        // Expectations
        expect(smsNotificationRepository.save(isA(SmsNotification.class))).andReturn(smsNotification).once();
        applicationEventDistributionService.publishAsynchronousEvent(new StartSendingNotificationEvent(smsNotification.getId(), notificationDto.getSecureProperties()));
        // Replay
        replayAll();
        // Run test scenario
        final SmsNotification notification = smsNotificationService.createSmsNotification(notificationDto, properties);
        getServicesImplTestHelper().assertSmsNotification(notification, notificationDto);
    }

    @Test
    public void testCreateSmsNotification() {
        // Test data
        final String userUuid = UUID.randomUUID().toString();
        final SmsNotificationDto notificationDto = getServicesImplTestHelper().createSmsNotificationDto();
        notificationDto.setUserUuid(userUuid);
        final List<SmsNotificationPropertyDto> properties = getServicesImplTestHelper().createSmsNotificationPropertyDtos(2);
        final SmsNotification smsNotification = getServicesImplTestHelper().createSmsNotification();
        smsNotification.setId(2L);
        final User user = getServicesImplTestHelper().createUser();
        final UserNotification userNotification = getServicesImplTestHelper().createUserNotification();
        // Reset
        resetAll();
        // Expectations
        expect(smsNotificationRepository.save(isA(SmsNotification.class))).andReturn(smsNotification).once();
        expect(userService.getOrCreateUserForUuId(userUuid)).andReturn(user);
        expect(userNotificationService.createUserNotification(user.getId(), smsNotification.getId(), new UserNotificationDto())).andReturn(userNotification);
        applicationEventDistributionService.publishAsynchronousEvent(new StartSendingNotificationEvent(smsNotification.getId(), notificationDto.getSecureProperties()));
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
