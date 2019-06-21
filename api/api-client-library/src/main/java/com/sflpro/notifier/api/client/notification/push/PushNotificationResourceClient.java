package com.sflpro.notifier.api.client.notification.push;

import com.sflpro.notifier.api.model.common.result.ResultResponseModel;
import com.sflpro.notifier.api.model.push.request.CreatePushNotificationRequest;
import com.sflpro.notifier.api.model.push.request.UpdatePushNotificationSubscriptionRequest;
import com.sflpro.notifier.api.model.push.response.CreatePushNotificationResponse;
import com.sflpro.notifier.api.model.push.response.UpdatePushNotificationSubscriptionResponse;

import javax.annotation.Nonnull;

/**
 * User: Ruben Dilanyan
 * Company: SFL LLC
 * Date: 1/14/16
 * Time: 2:17 PM
 */
public interface PushNotificationResourceClient {

    /**
     * Create push notification
     *
     * @param request
     * @return response
     */
    @Nonnull
    ResultResponseModel<CreatePushNotificationResponse> createPushNotification(@Nonnull final CreatePushNotificationRequest request);

    /**
     * Update push notification subscription
     *
     * @param request
     * @return response
     */
    @Nonnull
    ResultResponseModel<UpdatePushNotificationSubscriptionResponse> updatePushNotificationSubscription(@Nonnull final UpdatePushNotificationSubscriptionRequest request);
}
