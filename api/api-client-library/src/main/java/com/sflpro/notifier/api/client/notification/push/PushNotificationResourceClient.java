package com.sflpro.notifier.api.client.notification.push;

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
     * Create push notification
     *
     * @param request
     * @param authToken The Bearer token to be used for authentication/authorization
     * @return response
     */
    @Nonnull
    ResultResponseModel<CreatePushNotificationResponse> createPushNotification(@Nonnull final CreatePushNotificationRequest request, @Nonnull final String authToken);

    /**
     * Create templated push notification
     *
     * @param request CreateTemplatedPushNotificationRequest
     * @return response
     */
    @Nonnull
    ResultResponseModel<CreatePushNotificationResponse> createPushNotification(@Nonnull final CreateTemplatedPushNotificationRequest request);

    /**
     * Create templated push notification
     *
     * @param request   CreateTemplatedPushNotificationRequest
     * @param authToken The Bearer token to be used for authentication/authorization
     * @return response
     */
    @Nonnull
    ResultResponseModel<CreatePushNotificationResponse> createPushNotification(@Nonnull final CreateTemplatedPushNotificationRequest request, @Nonnull final String authToken);

    /**
     * Update push notification subscription
     *
     * @param request
     * @return response
     */
    @Nonnull
    ResultResponseModel<UpdatePushNotificationSubscriptionResponse> updatePushNotificationSubscription(@Nonnull final UpdatePushNotificationSubscriptionRequest request);
}
