package com.sflpro.notifier.api.facade.services.email;

import com.sflpro.notifier.api.model.common.result.ResultResponseModel;
import com.sflpro.notifier.api.model.email.request.CreateEmailNotificationRequest;
import com.sflpro.notifier.api.model.email.response.CreateEmailNotificationResponse;

/**
 * Created by Hayk Mkrtchyan.
 * Date: 7/1/19
 * Time: 2:43 PM
 */
public interface EmailNotificationServiceFacade {

    ResultResponseModel<CreateEmailNotificationResponse> createEmailNotification(final CreateEmailNotificationRequest request);
}
