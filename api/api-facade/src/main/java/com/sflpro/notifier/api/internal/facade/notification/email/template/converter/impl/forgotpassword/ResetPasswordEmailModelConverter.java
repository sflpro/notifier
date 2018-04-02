package com.sflpro.notifier.api.internal.facade.notification.email.template.converter.impl.forgotpassword;

import com.sflpro.notifier.api.internal.facade.notification.email.template.converter.EmailTemplateModelConverter;
import com.sflpro.notifier.api.internal.facade.notification.email.template.converter.impl.NextEventAwareBaseEmailModelConverter;
import com.sflpro.notifier.core.api.internal.model.email.template.EmailTemplateClientType;
import com.sflpro.notifier.core.api.internal.model.email.template.forgotpassword.ResetPasswordEmailClientModel;
import com.sflpro.notifier.services.notification.email.template.model.BaseEmailTemplateModel;
import com.sflpro.notifier.services.notification.email.template.model.forgotpassword.ResetPasswordEmailTemplateModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import javax.annotation.Nonnull;

/**
 * User: Davit Yeghiazaryan
 * Company: SFL LLC
 * Date 6/16/16
 * Time 6:53 PM
 */
@Component
public class ResetPasswordEmailModelConverter implements EmailTemplateModelConverter<ResetPasswordEmailTemplateModel, ResetPasswordEmailClientModel> {
    private static final Logger LOGGER = LoggerFactory.getLogger(ResetPasswordEmailModelConverter.class);

    /* Dependencies */
    @Autowired
    private NextEventAwareBaseEmailModelConverter nextEventAwareBaseEmailModelConverter;

    /* Constructors */
    public ResetPasswordEmailModelConverter() {
        LOGGER.debug("Initializing reset password email model converter");
    }

    @Nonnull
    @Override
    public EmailTemplateClientType getEmailTemplateType() {
        return EmailTemplateClientType.FORGOT_PASSWORD;
    }

    @Nonnull
    @Override
    public BaseEmailTemplateModel createEmailTemplateModel() {
        return new ResetPasswordEmailTemplateModel();
    }

    @Nonnull
    @Override
    public void convertClientModelToTemplateModel(@Nonnull final ResetPasswordEmailClientModel clientModel, @Nonnull final ResetPasswordEmailTemplateModel templateModel) {
        Assert.notNull(clientModel, "Client Model should not be null");
        Assert.notNull(templateModel, "Template model should not be null");
        LOGGER.debug("Converting reset password email client model - {} into template model", clientModel);
        // Convert parent properties
        nextEventAwareBaseEmailModelConverter.convertClientModelToTemplateModel(clientModel, templateModel);
        templateModel.setName(clientModel.getName());
        templateModel.setCorporateCustomer(clientModel.isCorporateCustomer());
        templateModel.setRegisteredCustomer(clientModel.isRegisteredCustomer());
        templateModel.setToken(clientModel.getToken());
        templateModel.setRedirectUri(clientModel.getRedirectUri());
        templateModel.setEmail(clientModel.getEmail());
        templateModel.setVerificationToken(clientModel.getVerificationToken());
        LOGGER.debug("Successfully converted client model - {}, to template model - {}", clientModel, templateModel);
    }

    /* Properties getters and setters */
    public NextEventAwareBaseEmailModelConverter getNextEventAwareBaseEmailModelConverter() {
        return nextEventAwareBaseEmailModelConverter;
    }

    public void setNextEventAwareBaseEmailModelConverter(final NextEventAwareBaseEmailModelConverter nextEventAwareBaseEmailModelConverter) {
        this.nextEventAwareBaseEmailModelConverter = nextEventAwareBaseEmailModelConverter;
    }
}
