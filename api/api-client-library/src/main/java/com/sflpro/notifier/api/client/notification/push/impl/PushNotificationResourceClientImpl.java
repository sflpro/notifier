package com.sflpro.notifier.api.client.notification.push.impl;

import com.sflpro.notifier.api.client.common.AbstractResourceClient;
import com.sflpro.notifier.api.client.notification.push.PushNotificationResourceClient;
import com.sflpro.notifier.api.model.common.result.ResultResponseModel;
import com.sflpro.notifier.api.model.push.request.CreatePushNotificationRequest;
import com.sflpro.notifier.api.model.push.request.UpdatePushNotificationSubscriptionRequest;
import com.sflpro.notifier.api.model.push.response.CreatePushNotificationResponse;
import com.sflpro.notifier.api.model.push.response.UpdatePushNotificationSubscriptionResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;

/**
 * User: Ruben Dilanyan
 * Company: SFL LLC
 * Date: 1/14/16
 * Time: 2:17 PM
 */
public class PushNotificationResourceClientImpl extends AbstractResourceClient implements PushNotificationResourceClient {

    private static final Logger LOGGER = LoggerFactory.getLogger(PushNotificationResourceClientImpl.class);

    /* Constants */
    private static final String PATH_PUSH_CREATE = "rest/notification/push/create";

    private static final String PATH_PUSH_SUBSCRIBE = "rest/notification/subscribe";

    /* Constructors */
    public PushNotificationResourceClientImpl() {
        //default constructor
    }

    public PushNotificationResourceClientImpl(final Client client, final String apiPath) {
        super(client, apiPath);
    }

    @Nonnull
    @Override
    public ResultResponseModel<CreatePushNotificationResponse> createPushNotification(@Nonnull final CreatePushNotificationRequest request) {
        assertCreatePushNotificationRequest(request);
        LOGGER.debug("Executing create push notification call, request - {}", request);
        final ResultResponseModel<CreatePushNotificationResponse> response = getClient().target(getApiPath())
                .path(PATH_PUSH_CREATE)
                .request(MediaType.APPLICATION_JSON_TYPE)
                .post(Entity.entity(request, MediaType.APPLICATION_JSON_TYPE), new GenericType<ResultResponseModel<CreatePushNotificationResponse>>() {
                });
        LOGGER.debug("Successfully executed create push notification call, request - {}, response -  {}", request, response);
        return response;
    }

    @Nonnull
    @Override
    public ResultResponseModel<UpdatePushNotificationSubscriptionResponse> updatePushNotificationSubscription(@Nonnull final UpdatePushNotificationSubscriptionRequest request) {
        assertUpdatePushNotificationSubscriptionRequest(request);
        LOGGER.debug("Executing update push notification subscription request - {}", request);
        final ResultResponseModel<UpdatePushNotificationSubscriptionResponse> response = getClient().target(getApiPath())
                .path(PATH_PUSH_SUBSCRIBE)
                .request(MediaType.APPLICATION_JSON_TYPE)
                .post(Entity.entity(request, MediaType.APPLICATION_JSON_TYPE), new GenericType<ResultResponseModel<UpdatePushNotificationSubscriptionResponse>>() {
                });
        LOGGER.debug("Successfully executed update push notification subscription call, request - {}, response -  {}", request, response);
        return response;
    }

    /* Utility methods */
    private static void assertCreatePushNotificationRequest(final CreatePushNotificationRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("Create push notification request should not be null");
        }
    }

    private static void assertUpdatePushNotificationSubscriptionRequest(final UpdatePushNotificationSubscriptionRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("Update push notification subscription request should not be null");
        }
    }

}
