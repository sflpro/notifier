package com.sflpro.notifier.api.client.notification.sms.impl;

import com.sflpro.notifier.api.client.common.AbstractResourceClient;
import com.sflpro.notifier.api.client.notification.sms.SmsNotificationResourceClient;
import com.sflpro.notifier.api.model.common.result.ResultResponseModel;
import com.sflpro.notifier.api.model.sms.request.CreateSmsNotificationRequest;
import com.sflpro.notifier.api.model.sms.response.CreateSmsNotificationResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;

/**
 * User: Ruben Dilanyan
 * Company: SFL LLC
 * Date: 1/14/16
 * Time: 2:17 PM
 */
public class SmsNotificationResourceClientImpl extends AbstractResourceClient implements SmsNotificationResourceClient {

    private static final Logger LOGGER = LoggerFactory.getLogger(SmsNotificationResourceClientImpl.class);

    /* Constants */
    private static final String PATH_SMS_CREATE = "notification/sms/create";

    public SmsNotificationResourceClientImpl(final Client client, final String apiPath) {
        super(client, apiPath);
    }

    @Nonnull
    @Override
    public ResultResponseModel<CreateSmsNotificationResponse> createSmsNotification(@Nonnull final CreateSmsNotificationRequest request) {
        return createSmsNotificationInternal(request, null);
    }

    @Nonnull
    @Override
    public ResultResponseModel<CreateSmsNotificationResponse> createSmsNotification(@Nonnull final CreateSmsNotificationRequest request, final @Nonnull String authToken) {
        asserValidAuthToken(authToken);
        return createSmsNotificationInternal(request, authToken);
    }

    @Nonnull
    private ResultResponseModel<CreateSmsNotificationResponse> createSmsNotificationInternal(@Nonnull final CreateSmsNotificationRequest request, @Nullable final String authToken) {
        assertCreateSmsNotificationRequest(request);
        LOGGER.debug("Executing create SMS notification call, request - {}", request);
        final Invocation.Builder requestBuilder = getClient().target(getApiPath())
                .path(PATH_SMS_CREATE)
                .request(MediaType.APPLICATION_JSON_TYPE);
        if (authToken != null) {
            addAutorizationHeader(requestBuilder, authToken);
        }
        final ResultResponseModel<CreateSmsNotificationResponse> response = requestBuilder.post(Entity.entity(request, MediaType.APPLICATION_JSON_TYPE), new GenericType<ResultResponseModel<CreateSmsNotificationResponse>>() {
        });
        LOGGER.debug("Successfully executed create SMS notification call, request - {}, response -  {}", request, response);
        return response;
    }

    /* Utility methods */
    private static void assertCreateSmsNotificationRequest(final CreateSmsNotificationRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("Create SMS notification request should not be null");
        }
    }

}
