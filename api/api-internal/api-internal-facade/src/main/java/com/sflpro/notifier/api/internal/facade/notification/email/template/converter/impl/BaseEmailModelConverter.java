package com.sflpro.notifier.api.internal.facade.notification.email.template.converter.impl;

import com.sflpro.notifier.api.internal.facade.notification.email.template.converter.ModelConverter;
import com.sflpro.notifier.core.api.internal.model.email.template.BaseEmailClientModel;
import com.sflpro.notifier.services.notification.email.template.model.BaseEmailTemplateModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import javax.annotation.Nonnull;

/**
 * User: Davit Yeghiazaryan
 * Company: SFL LLC
 * Date 6/16/16
 * Time 12:28 PM
 */
@Component
public class BaseEmailModelConverter implements ModelConverter<BaseEmailTemplateModel, BaseEmailClientModel> {
    private static final Logger LOGGER = LoggerFactory.getLogger(BaseEmailModelConverter.class);

    /* Dependencies */

    /* Constructors */
    public BaseEmailModelConverter() {
        LOGGER.debug("Initialzing base email model converter");
    }

    @Nonnull
    @Override
    public void convertClientModelToTemplateModel(@Nonnull final BaseEmailClientModel clientModel, @Nonnull final BaseEmailTemplateModel templateModel) {
        Assert.notNull(clientModel, "Client Model should not be null");
        Assert.notNull(templateModel, "Template model should not be null");
        LOGGER.debug("Converting base email client model - {} into template model", clientModel);
        templateModel.setEmailCode(clientModel.getEmailCode());
        templateModel.setUrl(clientModel.getUrl());
        templateModel.setCustomerSupportUrl(clientModel.getUrl());
        templateModel.setRecipientEmail(clientModel.getRecipientEmail());
        LOGGER.debug("Successfully converted client model - {}, to template model - {}", clientModel, templateModel);
    }
}
