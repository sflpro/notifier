package com.sflpro.notifier.api.facade.services.push;

import com.sflpro.notifier.api.model.common.result.ResultResponseModel;
import com.sflpro.notifier.api.model.push.request.CreatePushNotificationRequest;
import com.sflpro.notifier.api.model.push.request.CreateTemplatedPushNotificationRequest;
import com.sflpro.notifier.api.model.push.request.UpdatePushNotificationSubscriptionRequest;
import com.sflpro.notifier.api.model.push.response.CreatePushNotificationResponse;
import com.sflpro.notifier.api.model.push.response.UpdatePushNotificationSubscriptionResponse;

import javax.annotation.Nonnull;

/**
 * User: Ruben Dilanyan
 * Company: SFL LLC
 * Date: 1/13/16
 * Time: 6:13 PM
 */
public interface PushNotificationServiceFacade {

    /**
     * Create push notifications
     *
     * @param request CreatePushNotificationRequest
     * @return CreatePushNotificationResponse
     */
    @Nonnull
    ResultResponseModel<CreatePushNotificationResponse> createPushNotifications(@Nonnull final CreatePushNotificationRequest request);

    /**
     * Create templated push notifications
     *
     * @param request CreateTemplatedPushNotificationRequest
     * @return CreatePushNotificationResponse
     */
    @Nonnull
    ResultResponseModel<CreatePushNotificationResponse> createPushNotifications(@Nonnull final CreateTemplatedPushNotificationRequest request);

    /**
     * Updates push notification subscription
     *
     * @param request UpdatePushNotificationSubscriptionRequest
     * @return UpdatePushNotificationSubscriptionResponse
     */
    @Nonnull
    ResultResponseModel<UpdatePushNotificationSubscriptionResponse> updatePushNotificationSubscription(@Nonnull final UpdatePushNotificationSubscriptionRequest request);
}
