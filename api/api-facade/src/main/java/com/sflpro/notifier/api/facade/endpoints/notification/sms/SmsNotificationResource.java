package com.sflpro.notifier.api.facade.endpoints.notification.sms;

import com.sflpro.notifier.api.facade.services.sms.SmsNotificationServiceFacade;
import com.sflpro.notifier.api.model.common.result.ResultResponseModel;
import com.sflpro.notifier.api.model.sms.request.CreateSmsNotificationRequest;
import com.sflpro.notifier.api.model.sms.response.CreateSmsNotificationResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

/**
 * User: Ruben Dilanyan
 * Company: SFL LLC
 * Date: 1/14/16
 * Time: 12:40 PM
 */
@Singleton
@Path("notification/sms")
@Produces("application/json")
public class SmsNotificationResource {

    private static final Logger LOGGER = LoggerFactory.getLogger(SmsNotificationResource.class);

    /* Dependencies */
    @Inject
    private SmsNotificationServiceFacade smsNotificationServiceFacade;

    /* Constructors */
    public SmsNotificationResource() {
        super();
    }

    @POST
    @Path("create")
    @Consumes("application/json")
    public Response createSmsNotification(final CreateSmsNotificationRequest request) {
        LOGGER.debug("Processing create SMS notification request - {}", request);
        final ResultResponseModel<CreateSmsNotificationResponse> response = smsNotificationServiceFacade.createSmsNotification(request);
        LOGGER.debug("Processed create SMS notification request - {}, response - {}", request, response);
        return Response.ok(response).build();
    }
}