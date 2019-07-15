package com.sflpro.notifier.api.client.notification.email;

import com.sflpro.notifier.api.model.common.result.ResultResponseModel;
import com.sflpro.notifier.api.model.email.request.CreateEmailNotificationRequest;
import com.sflpro.notifier.api.model.email.response.CreateEmailNotificationResponse;

import javax.annotation.Nonnull;

/**
 * User: Ruben Dilanyan
 * Company: SFL LLC
 * Date: 1/14/16
 * Time: 2:17 PM
 */
public interface EmailNotificationResourceClient {

    /**
     * Create email notification
     *
     * @param request
     * @return response
     */
    @Nonnull
    ResultResponseModel<CreateEmailNotificationResponse> createEmailNotification(@Nonnull final CreateEmailNotificationRequest request);

    /**
     * Create email notification
     *
     * @param request
     * @param authToken The Bearer token to be used for authentication/authorization
     * @return response
     */
    @Nonnull
    ResultResponseModel<CreateEmailNotificationResponse> createEmailNotification(@Nonnull final CreateEmailNotificationRequest request, @Nonnull final String authToken);
}
