package com.sflpro.notifier.api.client.notification.sms;

import com.sflpro.notifier.api.model.common.result.ResultResponseModel;
import com.sflpro.notifier.api.model.sms.request.CreateSmsNotificationRequest;
import com.sflpro.notifier.api.model.sms.response.CreateSmsNotificationResponse;

import javax.annotation.Nonnull;

/**
 * User: Ruben Dilanyan
 * Company: SFL LLC
 * Date: 1/14/16
 * Time: 2:17 PM
 */
@FunctionalInterface
public interface SmsNotificationResourceClient {

    /**
     * Create SMS notification
     *
     * @param request
     * @return response
     */
    @Nonnull
    ResultResponseModel<CreateSmsNotificationResponse> createSmsNotification(@Nonnull final CreateSmsNotificationRequest request);
}
