package com.sflpro.notifier.api.internal.client.rest.notification.email;

import com.sflpro.notifier.core.api.internal.model.common.result.ResultResponseModel;
import com.sflpro.notifier.core.api.internal.model.email.request.CreateEmailNotificationRequest;
import com.sflpro.notifier.core.api.internal.model.email.response.CreateEmailNotificationResponse;

import javax.annotation.Nonnull;

/**
 * User: Ruben Dilanyan
 * Company: SFL LLC
 * Date: 1/14/16
 * Time: 2:17 PM
 */
@FunctionalInterface
public interface EmailNotificationResourceClient {

    /**
     * Create email notification
     *
     * @param request
     * @return response
     */
    @Nonnull
    ResultResponseModel<CreateEmailNotificationResponse> createEmailNotification(@Nonnull final CreateEmailNotificationRequest request);
}
