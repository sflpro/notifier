package com.sflpro.notifier.services.notification.impl.email;

import com.sflpro.notifier.services.notification.dto.email.ThirdPartyEmailNotificationDto;
import com.sflpro.notifier.services.notification.dto.email.ThirdPartyEmailNotificationPropertyDto;
import com.sflpro.notifier.services.notification.email.ThirdPartyEmailNotificationService;
import com.sflpro.notifier.services.notification.email.template.model.forgotpassword.ResetPasswordEmailTemplateModel;
import com.sflpro.notifier.services.notification.event.sms.StartSendingNotificationEvent;
import com.sflpro.notifier.db.entities.notification.email.EmailNotification;
import com.sflpro.notifier.db.entities.notification.email.ThirdPartyEmailNotification;
import com.sflpro.notifier.services.system.event.ApplicationEventDistributionService;
import com.sflpro.notifier.services.test.AbstractServicesUnitTest;
import org.apache.commons.lang.StringUtils;
import org.easymock.Mock;
import org.easymock.TestSubject;
import org.junit.Test;

import javax.annotation.Nonnull;
import java.util.LinkedList;
import java.util.List;

import static org.easymock.EasyMock.*;
import static org.junit.Assert.*;

/**
 * Created by Martirosyan
 * on 6/15/16.
 */
public class ThirdPartyEmailPreparationServiceTest extends AbstractServicesUnitTest {

    /* Test subject and mocks */
    @TestSubject
    private final ThirdPartyEmailPreparationService thirdPartyEmailPreparationService = new ThirdPartyEmailPreparationService();

    @Mock
    private ApplicationEventDistributionService applicationEventDistributionService;

    @Mock
    private ThirdPartyEmailNotificationService thirdPartyEmailNotificationService;

    /*constructor*/
    public ThirdPartyEmailPreparationServiceTest() {
    }

    /*Test methods*/
    @Test
    public void testPrepareAndSendResetPasswordEmailWithInvalidArguments() {
        //test data
        final Long channelId = 2L;
        final String toAddress = "aram.martirosyan@sflpro.com";
        //create reset password email template model
        final ResetPasswordEmailTemplateModel model = createResetPasswordEmailTemplateModel();
        //reset
        resetAll();
        //replay
        replayAll();
        try {
            thirdPartyEmailPreparationService.prepareAndSendResetPasswordEmail(null, model);
            fail("Exception should be thrown");
        } catch (final IllegalArgumentException e) {
            //expected
        }
        try {
            thirdPartyEmailPreparationService.prepareAndSendResetPasswordEmail(StringUtils.EMPTY, model);
            fail("Exception should be thrown");
        } catch (final IllegalArgumentException e) {
            //expected
        }
        try {
            thirdPartyEmailPreparationService.prepareAndSendResetPasswordEmail(toAddress, null);
            fail("Exception should be thrown");
        } catch (final IllegalArgumentException e) {
            //expected
        }
        //verify
        verifyAll();
    }

    @Test
    public void testPrepareAndSendResetPasswordEmail() {
        //test data
        final Long channelId = 2L;
        final String toAddress = "aram.martirosyan@sflpro.com";
        //create reset password email template model
        final ResetPasswordEmailTemplateModel model = createResetPasswordEmailTemplateModel();
        //convert data
        final List<ThirdPartyEmailNotificationPropertyDto> propertyDtos = convertResetPasswordEmailTemplateModel(model);
        final ThirdPartyEmailNotification notification = new ThirdPartyEmailNotification();
        notification.setId(11L);
        final StartSendingNotificationEvent event = new StartSendingNotificationEvent(notification.getId());
        //reset
        resetAll();
        expect(thirdPartyEmailNotificationService.createEmailNotification(isA(ThirdPartyEmailNotificationDto.class), isA(List.class))).andReturn(notification).once();
        applicationEventDistributionService.publishAsynchronousEvent(event);
        expectLastCall().once();
        //replay
        replayAll();
        final EmailNotification result = thirdPartyEmailPreparationService.prepareAndSendResetPasswordEmail(toAddress, model);
        assertNotNull(result);
        assertEquals(notification, result);
        //verify
        verifyAll();
    }

    /* Utility methods */
    private static void addThirdPartyEmailNotificationPropertyDtoToList(@Nonnull final List<ThirdPartyEmailNotificationPropertyDto> propertyDtos,
                                                                        @Nonnull final String key, @Nonnull final String value) {
        propertyDtos.add(new ThirdPartyEmailNotificationPropertyDto(key, value));
    }

    private static ResetPasswordEmailTemplateModel createResetPasswordEmailTemplateModel() {
        final ResetPasswordEmailTemplateModel model = new ResetPasswordEmailTemplateModel();
        model.setName("name");
        model.setToken("token");
        model.setEmail("test@test.com");
        model.setVerificationToken("verificationToken");
        return model;
    }

    private static List<ThirdPartyEmailNotificationPropertyDto> convertResetPasswordEmailTemplateModel(@Nonnull final ResetPasswordEmailTemplateModel model) {
        final List<ThirdPartyEmailNotificationPropertyDto> propertyDtos = new LinkedList<>();
        addThirdPartyEmailNotificationPropertyDtoToList(propertyDtos, "firstname", model.getName());
        addThirdPartyEmailNotificationPropertyDtoToList(propertyDtos, "rest_email", model.getEmail());
        addThirdPartyEmailNotificationPropertyDtoToList(propertyDtos, "rest_token", model.getVerificationToken());
        return propertyDtos;
    }
}
