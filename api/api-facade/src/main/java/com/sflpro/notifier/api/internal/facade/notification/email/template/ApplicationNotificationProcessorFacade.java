package com.sflpro.notifier.api.internal.facade.notification.email.template;

import com.sflpro.notifier.core.api.internal.model.common.result.ResultResponseModel;
import com.sflpro.notifier.core.api.internal.model.email.response.CreateEmailNotificationResponse;
import com.sflpro.notifier.core.api.internal.model.email.template.request.ForgotPasswordRequest;

import javax.annotation.Nonnull;

/**
 * User: Davit Yeghiazaryan
 * Company: SFL LLC
 * Date 6/15/16
 * Time 4:57 PM
 */
public interface ApplicationNotificationProcessorFacade {

    /**
     * Processes forgot password
     *
     * @param request
     * @return response
     */
    @Nonnull
    ResultResponseModel<CreateEmailNotificationResponse> processForgotPassword(@Nonnull final ForgotPasswordRequest request);
}
