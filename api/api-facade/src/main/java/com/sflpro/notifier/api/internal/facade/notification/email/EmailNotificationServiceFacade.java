package com.sflpro.notifier.api.internal.facade.notification.email;

import com.sflpro.notifier.core.api.internal.model.common.result.ResultResponseModel;
import com.sflpro.notifier.core.api.internal.model.email.request.CreateEmailNotificationRequest;
import com.sflpro.notifier.core.api.internal.model.email.response.CreateEmailNotificationResponse;

import javax.annotation.Nonnull;

/**
 * User: Ruben Dilanyan
 * Company: SFL LLC
 * Date: 1/12/16
 * Time: 7:34 PM
 */
@FunctionalInterface
public interface EmailNotificationServiceFacade {

    /**
     * Creates email notification for provided request
     *
     * @param request
     * @return response
     */
    @Nonnull
    ResultResponseModel<CreateEmailNotificationResponse> createEmailNotification(@Nonnull final CreateEmailNotificationRequest request);
}
