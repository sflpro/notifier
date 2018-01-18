package com.sflpro.notifier.api.internal.client.rest.notification.email.impl;

import com.sflpro.notifier.api.internal.client.rest.common.AbstractResourceClient;
import com.sflpro.notifier.api.internal.client.rest.notification.email.EmailNotificationResourceClient;
import com.sflpro.notifier.core.api.internal.model.common.result.ResultResponseModel;
import com.sflpro.notifier.core.api.internal.model.email.request.CreateEmailNotificationRequest;
import com.sflpro.notifier.core.api.internal.model.email.response.CreateEmailNotificationResponse;
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
public class EmailNotificationResourceClientImpl extends AbstractResourceClient implements EmailNotificationResourceClient {

    private static final Logger LOGGER = LoggerFactory.getLogger(EmailNotificationResourceClientImpl.class);

    /* Constants */
    private static final String PATH_EMAIL_CREATE = "notification/email/create";

    /* Constructors */
    public EmailNotificationResourceClientImpl() {
        //default constructor
    }

    public EmailNotificationResourceClientImpl(final Client client, final String apiPath) {
        super(client, apiPath);
    }

    @Nonnull
    @Override
    public ResultResponseModel<CreateEmailNotificationResponse> createEmailNotification(@Nonnull final CreateEmailNotificationRequest request) {
        assertCreateEmailNotificationRequest(request);
        LOGGER.debug("Executing create email notification call, request - {}", request);
        final ResultResponseModel<CreateEmailNotificationResponse> response = getClient().target(getApiPath())
                .path(PATH_EMAIL_CREATE)
                .request(MediaType.APPLICATION_JSON_TYPE)
                .post(Entity.entity(request, MediaType.APPLICATION_JSON_TYPE), new GenericType<ResultResponseModel<CreateEmailNotificationResponse>>() {
                });
        LOGGER.debug("Successfully executed create email notification call, request - {}, response -  {}", request, response);
        return response;
    }

    /* Utility methods */
    private static void assertCreateEmailNotificationRequest(final CreateEmailNotificationRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("Create email notification request should not be null");
        }
    }

}
