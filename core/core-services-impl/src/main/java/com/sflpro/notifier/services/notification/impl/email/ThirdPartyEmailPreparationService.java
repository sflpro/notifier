package com.sflpro.notifier.services.notification.impl.email;

import com.sflpro.notifier.db.entities.notification.Notification;
import com.sflpro.notifier.db.entities.notification.NotificationProviderType;
import com.sflpro.notifier.db.entities.notification.email.EmailNotification;
import com.sflpro.notifier.db.entities.notification.email.ThirdPartyEmailNotification;
import com.sflpro.notifier.services.notification.dto.email.ThirdPartyEmailNotificationDto;
import com.sflpro.notifier.services.notification.dto.email.ThirdPartyEmailNotificationPropertyDto;
import com.sflpro.notifier.services.notification.email.EmailPreparationService;
import com.sflpro.notifier.services.notification.email.ThirdPartyEmailNotificationService;
import com.sflpro.notifier.services.notification.email.template.model.NotificationTemplateType;
import com.sflpro.notifier.services.notification.email.template.model.forgotpassword.ResetPasswordEmailTemplateModel;
import com.sflpro.notifier.services.notification.event.sms.StartSendingNotificationEvent;
import com.sflpro.notifier.services.system.event.ApplicationEventDistributionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.annotation.Nonnull;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Martirosyan
 * on 6/15/16.
 */
@Service
public class ThirdPartyEmailPreparationService implements EmailPreparationService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ThirdPartyEmailPreparationService.class);

    /* Dependencies */
    @Autowired
    private ApplicationEventDistributionService applicationEventDistributionService;

    @Autowired
    private ThirdPartyEmailNotificationService thirdPartyEmailNotificationService;

    /* Constructor */
    public ThirdPartyEmailPreparationService() {
        LOGGER.debug("Initializing third party email preparation service");
    }

    @Nonnull
    @Override
    public EmailNotification prepareAndSendResetPasswordEmail(@Nonnull final String toAddress, @Nonnull final ResetPasswordEmailTemplateModel resetPasswordEmailTemplateModel) {
         /*asserts*/
        assertToEmailAddress(toAddress);
        assertResetPasswordEmailTemplateModel(resetPasswordEmailTemplateModel);
        LOGGER.debug("Sending email toAddress - {},reset password email template model - {}",
                toAddress, resetPasswordEmailTemplateModel);
        //addThirdPartyEmailNotificationPropertyDtoToList list for third party email notification dto's
        final List<ThirdPartyEmailNotificationPropertyDto> propertyDtos = new LinkedList<>();
        //addThirdPartyEmailNotificationPropertyDtoToList new third party email notification dto's and add linked list
        addThirdPartyEmailNotificationPropertyDtoToList(propertyDtos, "firstname", resetPasswordEmailTemplateModel.getName());
        addThirdPartyEmailNotificationPropertyDtoToList(propertyDtos, "reset_email", resetPasswordEmailTemplateModel.getEmail());
        addThirdPartyEmailNotificationPropertyDtoToList(propertyDtos, "redirect_uri", resetPasswordEmailTemplateModel.getRedirectUri());
        addThirdPartyEmailNotificationPropertyDtoToList(propertyDtos, "reset_token", resetPasswordEmailTemplateModel.getVerificationToken());
        addThirdPartyEmailNotificationPropertyDtoToList(propertyDtos, "corporate_customer", resetPasswordEmailTemplateModel.isCorporateCustomer() ? "y" : "n");
        LOGGER.debug("Reset password email template model successfully converted to list of third party email notification property dto's -{} ", propertyDtos);
        //save notification
        final ThirdPartyEmailNotification notification = saveNotification(toAddress, NotificationTemplateType.FORGOT_PASSWORD.getTemplateName(), propertyDtos);
        // Publish event
        publishNotificationEvent(notification);
        return notification;
    }

    /*Utility methods*/
    private ThirdPartyEmailNotification saveNotification(@Nonnull final String toEmail, final String templateName,
                                                         @Nonnull final List<ThirdPartyEmailNotificationPropertyDto> propertyDtos) {
        final ThirdPartyEmailNotificationDto dto = new ThirdPartyEmailNotificationDto(toEmail, null, NotificationProviderType.MANDRILL, templateName, null, null, null);
        return thirdPartyEmailNotificationService.createEmailNotification(dto, propertyDtos);
    }

    private static void addThirdPartyEmailNotificationPropertyDtoToList(final List<ThirdPartyEmailNotificationPropertyDto> propertyDtos,
                                                                        final String key, final String value) {
        propertyDtos.add(new ThirdPartyEmailNotificationPropertyDto(key, value));
    }

    private static void assertResetPasswordEmailTemplateModel(@Nonnull final ResetPasswordEmailTemplateModel model) {
        Assert.notNull(model, "Reset password email template model should not be null");
    }

    private void publishNotificationEvent(final Notification notification) {
        applicationEventDistributionService.publishAsynchronousEvent(new StartSendingNotificationEvent(notification.getId()));
    }

    private static void assertToEmailAddress(@Nonnull final String toAddress) {
        Assert.hasText(toAddress, "Customer address should not be null");
    }

    public ApplicationEventDistributionService getApplicationEventDistributionService() {
        return applicationEventDistributionService;
    }

    public void setApplicationEventDistributionService(final ApplicationEventDistributionService applicationEventDistributionService) {
        this.applicationEventDistributionService = applicationEventDistributionService;
    }

    public ThirdPartyEmailNotificationService getThirdPartyEmailNotificationService() {
        return thirdPartyEmailNotificationService;
    }

    public void setThirdPartyEmailNotificationService(final ThirdPartyEmailNotificationService thirdPartyEmailNotificationService) {
        this.thirdPartyEmailNotificationService = thirdPartyEmailNotificationService;
    }
}
