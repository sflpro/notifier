package com.sflpro.notifier.api.internal.client.rest.notification.sms.impl;

import com.sflpro.notifier.api.internal.client.rest.common.AbstractResourceClient;
import com.sflpro.notifier.api.internal.client.rest.notification.sms.SmsNotificationResourceClient;
import com.sflpro.notifier.core.api.internal.model.common.result.ResultResponseModel;
import com.sflpro.notifier.core.api.internal.model.sms.request.CreateSmsNotificationRequest;
import com.sflpro.notifier.core.api.internal.model.sms.response.CreateSmsNotificationResponse;
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
public class SmsNotificationResourceClientImpl extends AbstractResourceClient implements SmsNotificationResourceClient {

    private static final Logger LOGGER = LoggerFactory.getLogger(SmsNotificationResourceClientImpl.class);

    /* Constants */
    private static final String PATH_SMS_CREATE = "rest/notification/sms/create";

    /* Constructors */
    public SmsNotificationResourceClientImpl() {
        //default constructor
    }

    public SmsNotificationResourceClientImpl(final Client client, final String apiPath) {
        super(client, apiPath);
    }

    @Nonnull
    @Override
    public ResultResponseModel<CreateSmsNotificationResponse> createSmsNotification(@Nonnull final CreateSmsNotificationRequest request) {
        assertCreateSmsNotificationRequest(request);
        LOGGER.debug("Executing create SMS notification call, request - {}", request);
        final ResultResponseModel<CreateSmsNotificationResponse> response = getClient().target(getApiPath())
                .path(PATH_SMS_CREATE)
                .request(MediaType.APPLICATION_JSON_TYPE)
                .post(Entity.entity(request, MediaType.APPLICATION_JSON_TYPE), new GenericType<ResultResponseModel<CreateSmsNotificationResponse>>() {
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
