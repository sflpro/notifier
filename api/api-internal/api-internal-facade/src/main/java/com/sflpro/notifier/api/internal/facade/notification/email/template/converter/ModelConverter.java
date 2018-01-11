package com.sflpro.notifier.api.internal.facade.notification.email.template.converter;

import javax.annotation.Nonnull;

/**
 * User: Davit Yeghiazaryan
 * Company: SFL LLC
 * Date 6/15/16
 * Time 5:24 PM
 */
@FunctionalInterface
public interface ModelConverter<T, M> {

    /**
     * Converts provided client model into template model
     *
     * @param clientModel
     * @param templateModel
     * @return Template model
     */
    void convertClientModelToTemplateModel(@Nonnull final M clientModel, @Nonnull final T templateModel);
}
