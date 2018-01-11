package com.sflpro.notifier.api.internal.client.rest.notification.email.template.impl;

import com.sflpro.notifier.api.internal.client.rest.common.AbstractResourceClient;
import com.sflpro.notifier.api.internal.client.rest.notification.email.template.ApplicationNotificationProcessorResourceClient;
import com.sflpro.notifier.core.api.internal.model.common.result.ResultResponseModel;
import com.sflpro.notifier.core.api.internal.model.email.response.CreateEmailNotificationResponse;
import com.sflpro.notifier.core.api.internal.model.email.template.request.AbstractTemplatedEmailRequest;
import com.sflpro.notifier.core.api.internal.model.email.template.request.ForgotPasswordRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;

/**
 * User: Davit Yeghiazaryan
 * Company: SFL LLC
 * Date 7/6/16
 * Time 12:07 PM
 */
public class ApplicationNotificationProcessorResourceClientImpl extends AbstractResourceClient implements ApplicationNotificationProcessorResourceClient {
    private static final Logger LOGGER = LoggerFactory.getLogger(ApplicationNotificationProcessorResourceClientImpl.class);

    /* Constants */
    private static final String PATH_EMAIL_TEMPLATE = "notification/event/";

    private static final String PATH_FORGOT_PASSWORD = "forgotpassword";

    /* Constructors */
    public ApplicationNotificationProcessorResourceClientImpl() {
        LOGGER.debug("Initializing templated email notification resource client");
    }

    public ApplicationNotificationProcessorResourceClientImpl(final Client client, final String apiPath) {
        super(client, apiPath);
    }

    @Nonnull
    @Override
    public ResultResponseModel<CreateEmailNotificationResponse> processForgotPassword(@Nonnull final ForgotPasswordRequest request) {
        assertRequest(request);
        LOGGER.debug("Executing forgot password call, request - {}", request);
        final ResultResponseModel<CreateEmailNotificationResponse> response = getClient().target(getApiPath())
                .path(PATH_EMAIL_TEMPLATE + PATH_FORGOT_PASSWORD)
                .request(MediaType.APPLICATION_JSON_TYPE)
                .post(Entity.entity(request, MediaType.APPLICATION_JSON_TYPE), new GenericType<ResultResponseModel<CreateEmailNotificationResponse>>() {
                });
        LOGGER.debug("Successfully executed forgot password call, request - {}, response -  {}", request, response);
        return response;
    }

    /* Utility methods */
    private static void assertRequest(final AbstractTemplatedEmailRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("Request should not be null");
        }
    }
}
