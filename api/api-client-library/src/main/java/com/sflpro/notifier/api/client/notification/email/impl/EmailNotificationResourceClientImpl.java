package com.sflpro.notifier.api.client.notification.email.impl;

import com.sflpro.notifier.api.client.common.AbstractResourceClient;
import com.sflpro.notifier.api.client.notification.email.EmailNotificationResourceClient;
import com.sflpro.notifier.api.model.common.result.ResultResponseModel;
import com.sflpro.notifier.api.model.email.request.CreateEmailNotificationRequest;
import com.sflpro.notifier.api.model.email.response.CreateEmailNotificationResponse;
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
public class EmailNotificationResourceClientImpl extends AbstractResourceClient implements EmailNotificationResourceClient {

    private static final Logger logger = LoggerFactory.getLogger(EmailNotificationResourceClientImpl.class);

    /* Constants */
    private static final String PATH_EMAIL_CREATE = "notification/email/create";

    public EmailNotificationResourceClientImpl(final Client client, final String apiPath) {
        super(client, apiPath);
    }

    @Nonnull
    @Override
    public ResultResponseModel<CreateEmailNotificationResponse> createEmailNotification(@Nonnull final CreateEmailNotificationRequest request) {
        return createEmailNotificationInternal(request, null);
    }

    @Nonnull
    @Override
    public ResultResponseModel<CreateEmailNotificationResponse> createEmailNotification(@Nonnull CreateEmailNotificationRequest request, @Nonnull final String authToken) {
        asserValidAuthToken(authToken);
        return createEmailNotificationInternal(request, authToken);
    }

    private ResultResponseModel<CreateEmailNotificationResponse> createEmailNotificationInternal(@Nonnull CreateEmailNotificationRequest request, @Nullable final String authToken) {
        assertCreateEmailNotificationRequest(request);
        logger.debug("Executing create email notification call, request - {}", request);
        final Invocation.Builder requestBuilder = getClient().target(getApiPath())
                .path(PATH_EMAIL_CREATE)
                .request(MediaType.APPLICATION_JSON_TYPE);
        if (authToken != null) {
            addAutorizationHeader(requestBuilder, authToken);
        }
        final ResultResponseModel<CreateEmailNotificationResponse> response = requestBuilder.post(Entity.entity(request, MediaType.APPLICATION_JSON_TYPE), new GenericType<ResultResponseModel<CreateEmailNotificationResponse>>() {
        });
        logger.debug("Successfully executed create email notification call, request - {}, response -  {}", request, response);
        return response;
    }

    /* Utility methods */
    private static void assertCreateEmailNotificationRequest(final CreateEmailNotificationRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("Create email notification request should not be null");
        }
    }

}
