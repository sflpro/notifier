package com.sflpro.notifier.api.internal.facade.notification.email.template.converter;

import com.sflpro.notifier.core.api.internal.model.email.template.EmailTemplateClientType;
import com.sflpro.notifier.services.notification.email.template.model.BaseEmailTemplateModel;

import javax.annotation.Nonnull;

/**
 * User: Davit Yeghiazaryan
 * Company: SFL LLC
 * Date 6/15/16
 * Time 5:32 PM
 */
public interface EmailTemplateModelConverter<T, M> extends ModelConverter<T, M> {

    /**
     * Gets email template type
     * @return EmailTemplateClientType
     */
    @Nonnull
    EmailTemplateClientType getEmailTemplateType();

    /**
     * Creates an empty email template model
     *
     * @return BaseEmailTemplateModel
     */
    @Nonnull
    BaseEmailTemplateModel createEmailTemplateModel();
}
