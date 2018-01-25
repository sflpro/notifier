package com.sflpro.notifier.api.internal.facade.notification.email.template.converter.impl;

import com.sflpro.notifier.api.internal.facade.notification.email.template.converter.ModelConverter;
import com.sflpro.notifier.core.api.internal.model.email.template.NextEventAwareBaseEmailClientModel;
import com.sflpro.notifier.services.notification.email.template.model.NextEventAwareBaseEmailTemplateModel;
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
 * Time 12:18 PM
 */
@Component
public class NextEventAwareBaseEmailModelConverter implements ModelConverter<NextEventAwareBaseEmailTemplateModel, NextEventAwareBaseEmailClientModel> {
    private static final Logger LOGGER = LoggerFactory.getLogger(NextEventAwareBaseEmailModelConverter.class);

    /* Dependencies */
    @Autowired
    private BaseEmailModelConverter baseEmailModelConverter;

    /* Constructors */
    public NextEventAwareBaseEmailModelConverter() {
        LOGGER.debug("Initializing next event aware base email model converter");
    }

    @Nonnull
    @Override
    public void convertClientModelToTemplateModel(@Nonnull final NextEventAwareBaseEmailClientModel clientModel, @Nonnull final NextEventAwareBaseEmailTemplateModel templateModel) {
        Assert.notNull(clientModel, "Client model should not be null");
        Assert.notNull(templateModel, "Template model should not be null");
        LOGGER.debug("Converting free next event aware base email client model - {} into template model", clientModel);
        // Convert parent properties
        baseEmailModelConverter.convertClientModelToTemplateModel(clientModel, templateModel);
        templateModel.setEventDate(clientModel.getEventDate());
        templateModel.setEventType(clientModel.getEventType());
        templateModel.setEventDescription(clientModel.getEventDescription());
        LOGGER.debug("Successfully converted client model - {}, to template model - {}", clientModel, templateModel);
    }

    /* Properties getters and setters */
}
