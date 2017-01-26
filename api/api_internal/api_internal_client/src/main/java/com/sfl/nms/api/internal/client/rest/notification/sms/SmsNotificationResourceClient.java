package com.sfl.nms.api.internal.client.rest.notification.sms;

import com.sfl.nms.core.api.internal.model.common.result.ResultResponseModel;
import com.sfl.nms.core.api.internal.model.sms.request.CreateSmsNotificationRequest;
import com.sfl.nms.core.api.internal.model.sms.response.CreateSmsNotificationResponse;

import javax.annotation.Nonnull;

/**
 * User: Ruben Dilanyan
 * Company: SFL LLC
 * Date: 1/14/16
 * Time: 2:17 PM
 */
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
