package com.sflpro.notifier.api.facade.endpoints.notification.email;

import com.sflpro.notifier.api.facade.services.email.EmailNotificationServiceFacade;
import com.sflpro.notifier.api.model.common.result.ResultResponseModel;
import com.sflpro.notifier.api.model.email.request.CreateEmailNotificationRequest;
import com.sflpro.notifier.api.model.email.response.CreateEmailNotificationResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * User: Ruben Dilanyan
 * Company: SFL LLC
 * Date: 1/14/16
 * Time: 12:40 PM
 */
@Component
@Path("notification/email")
@Produces(MediaType.APPLICATION_JSON)
public class EmailNotificationResource {

    private static final Logger LOGGER = LoggerFactory.getLogger(EmailNotificationResource.class);

    /* Dependencies */
    @Autowired
    private EmailNotificationServiceFacade emailNotificationServiceFacade;

    /* Constructors */
    public EmailNotificationResource() {
        //default constructor
    }

    @POST
    @Path("create")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createEmailNotification(final CreateEmailNotificationRequest request) {
        LOGGER.debug("Processing create email notification request - {}", request);
        final ResultResponseModel<CreateEmailNotificationResponse> response = emailNotificationServiceFacade.createEmailNotification(request);
        LOGGER.debug("Processed create email notification request - {}, response - {}", request, response);
        return Response.ok(response).build();
    }
}
