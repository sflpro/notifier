package com.sfl.nms.api.internal.client.rest.notification.push;

import com.sfl.nms.core.api.internal.model.common.result.ResultResponseModel;
import com.sfl.nms.core.api.internal.model.push.request.CreatePushNotificationRequest;
import com.sfl.nms.core.api.internal.model.push.request.UpdatePushNotificationSubscriptionRequest;
import com.sfl.nms.core.api.internal.model.push.response.CreatePushNotificationResponse;
import com.sfl.nms.core.api.internal.model.push.response.UpdatePushNotificationSubscriptionResponse;

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
