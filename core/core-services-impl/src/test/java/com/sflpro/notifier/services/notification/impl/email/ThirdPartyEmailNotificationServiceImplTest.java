package com.sflpro.notifier.services.notification.impl.email;

import com.sflpro.notifier.db.entities.notification.email.ThirdPartyEmailNotification;
import com.sflpro.notifier.db.repositories.repositories.notification.AbstractNotificationRepository;
import com.sflpro.notifier.db.repositories.repositories.notification.email.ThirdPartyEmailNotificationRepository;
import com.sflpro.notifier.services.notification.dto.email.ThirdPartyEmailNotificationDto;
import com.sflpro.notifier.services.notification.dto.email.ThirdPartyEmailNotificationPropertyDto;
import com.sflpro.notifier.services.notification.impl.AbstractNotificationServiceImpl;
import com.sflpro.notifier.services.notification.impl.AbstractNotificationServiceImplTest;
import com.sflpro.notifier.services.notification.impl.ThirdPartyEmailNotificationServiceImpl;
import org.easymock.Mock;
import org.easymock.TestSubject;
import org.junit.Test;

import java.util.List;

import static org.easymock.EasyMock.*;
import static org.junit.Assert.fail;

/**
 * User: Davit Yeghiazaryan
 * Company: SFL LLC
 * Date 6/30/16
 * Time 7:32 PM
 */
public class ThirdPartyEmailNotificationServiceImplTest extends AbstractNotificationServiceImplTest<ThirdPartyEmailNotification> {
    /* Test subject and mocks */
    @TestSubject
    private ThirdPartyEmailNotificationServiceImpl thirdPartyEmailNotificationService = new ThirdPartyEmailNotificationServiceImpl();

    @Mock
    private ThirdPartyEmailNotificationRepository thirdPartyEmailNotificationRepository;

    /* Constructors */
    public ThirdPartyEmailNotificationServiceImplTest() {
    }

    /* Test methods */
    @Test
    public void testCreateEmailNotificationWithInvalidArguments() {
        // Test data
        final ThirdPartyEmailNotificationDto emailNotificationDto = getServicesImplTestHelper().createThirdPartyEmailNotificationDto();
        final List<ThirdPartyEmailNotificationPropertyDto> propertyDtos = getServicesImplTestHelper().createThirdPartyEmailNotificationPropertyDtos(10);
        final List<ThirdPartyEmailNotificationPropertyDto> nullPropertyDto = getServicesImplTestHelper().createThirdPartyEmailNotificationPropertyDtos(2);
        nullPropertyDto.set(0, null);
        final List<ThirdPartyEmailNotificationPropertyDto> nullKeyDto = getServicesImplTestHelper().createThirdPartyEmailNotificationPropertyDtos(2);
        nullKeyDto.get(0).setPropertyKey(null);
        final List<ThirdPartyEmailNotificationPropertyDto> nullValueDto = getServicesImplTestHelper().createThirdPartyEmailNotificationPropertyDtos(2);
        nullValueDto.get(0).setPropertyValue(null);
        // Reset
        resetAll();
        // Replay
        replayAll();
        // Run test scenario
        try {
            thirdPartyEmailNotificationService.createEmailNotification(null, propertyDtos);
            fail("Exception should be thrown");
        } catch (final IllegalArgumentException ex) {
            // Expected
        }
        try {
            thirdPartyEmailNotificationService.createEmailNotification(new ThirdPartyEmailNotificationDto(null, emailNotificationDto.getSenderEmail(), emailNotificationDto.getProviderType(), emailNotificationDto.getTemplateName(), emailNotificationDto.getContent(), emailNotificationDto.getSubject(), emailNotificationDto.getClientIpAddress()), propertyDtos);
            fail("Exception should be thrown");
        } catch (final IllegalArgumentException ex) {
            // Expected
        }
        try {
            thirdPartyEmailNotificationService.createEmailNotification(new ThirdPartyEmailNotificationDto(emailNotificationDto.getRecipientEmail(), emailNotificationDto.getSenderEmail(), null, emailNotificationDto.getTemplateName(), emailNotificationDto.getContent(), emailNotificationDto.getSubject(), emailNotificationDto.getClientIpAddress()), propertyDtos);
            fail("Exception should be thrown");
        } catch (final IllegalArgumentException ex) {
            // Expected
        }
        try {
            thirdPartyEmailNotificationService.createEmailNotification(emailNotificationDto, null);
            fail("Exception should be thrown");
        } catch (final IllegalArgumentException ex) {
            // Expected
        }
        try {
            thirdPartyEmailNotificationService.createEmailNotification(emailNotificationDto, nullPropertyDto);
            fail("Exception should be thrown");
        } catch (final IllegalArgumentException ex) {
            // Expected
        }
        try {
            thirdPartyEmailNotificationService.createEmailNotification(emailNotificationDto, nullKeyDto);
            fail("Exception should be thrown");
        } catch (final IllegalArgumentException ex) {
            // Expected
        }
    }

    @Test
    public void testCreateEmailNotification() {
        // Test data
        final ThirdPartyEmailNotificationDto emailNotificationDto = getServicesImplTestHelper().createThirdPartyEmailNotificationDto();
        final List<ThirdPartyEmailNotificationPropertyDto> propertyDtos = getServicesImplTestHelper().createThirdPartyEmailNotificationPropertyDtos(10);
        // Reset
        resetAll();
        // Expectations
        expect(thirdPartyEmailNotificationRepository.save(isA(ThirdPartyEmailNotification.class))).andAnswer(() -> (ThirdPartyEmailNotification) getCurrentArguments()[0]).once();
        // Replay
        replayAll();
        // Run test scenario
        final ThirdPartyEmailNotification emailNotification = thirdPartyEmailNotificationService.createEmailNotification(emailNotificationDto, propertyDtos);
        getServicesImplTestHelper().assertThirdPartyEmailNotification(emailNotification, emailNotificationDto);
        getServicesImplTestHelper().assertThirdPartyEmailNotificationProperties(emailNotification, propertyDtos);
        // Verify all
        verifyAll();
    }

    /* Utility methods */
    @Override
    protected AbstractNotificationRepository<ThirdPartyEmailNotification> getRepository() {
        return thirdPartyEmailNotificationRepository;
    }

    @Override
    protected AbstractNotificationServiceImpl<ThirdPartyEmailNotification> getService() {
        return thirdPartyEmailNotificationService;
    }

    @Override
    protected Class<ThirdPartyEmailNotification> getInstanceClass() {
        return ThirdPartyEmailNotification.class;
    }

    @Override
    protected ThirdPartyEmailNotification getInstance() {
        return getServicesImplTestHelper().createThirdPartyEmailNotification();
    }
}
