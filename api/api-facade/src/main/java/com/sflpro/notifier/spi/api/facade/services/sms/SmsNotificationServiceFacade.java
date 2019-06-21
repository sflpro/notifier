package com.sflpro.notifier.api.facade.services.sms;

import com.sflpro.notifier.api.model.common.result.ResultResponseModel;
import com.sflpro.notifier.api.model.sms.request.CreateSmsNotificationRequest;
import com.sflpro.notifier.api.model.sms.response.CreateSmsNotificationResponse;

import javax.annotation.Nonnull;

/**
 * User: Ruben Dilanyan
 * Company: SFL LLC
 * Date: 1/13/16
 * Time: 5:13 PM
 */
public interface SmsNotificationServiceFacade {

    /**
     * Creates email notification for provided request
     *
     * @param request
     * @return response
     */
    @Nonnull
    ResultResponseModel<CreateSmsNotificationResponse> createSmsNotification(@Nonnull final CreateSmsNotificationRequest request);
}
