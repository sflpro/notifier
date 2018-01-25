package com.sflpro.notifier.api.internal.rest.resources.notification.sms;

import com.sflpro.notifier.api.internal.facade.notification.sms.SmsNotificationServiceFacade;
import com.sflpro.notifier.core.api.internal.model.common.result.ResultResponseModel;
import com.sflpro.notifier.core.api.internal.model.sms.request.CreateSmsNotificationRequest;
import com.sflpro.notifier.core.api.internal.model.sms.response.CreateSmsNotificationResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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
@Component
@Path("notification/sms")
@Produces("application/json")
public class SmsNotificationResource {

    private static final Logger LOGGER = LoggerFactory.getLogger(SmsNotificationResource.class);

    /* Dependencies */
    @Autowired
    private SmsNotificationServiceFacade smsNotificationServiceFacade;

    /* Constructors */
    public SmsNotificationResource() {
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