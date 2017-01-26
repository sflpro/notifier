package com.sfl.nms.api.internal.rest.resources.notification.email;

import com.sfl.nms.api.internal.facade.notification.email.EmailNotificationServiceFacade;
import com.sfl.nms.core.api.internal.model.common.result.ResultResponseModel;
import com.sfl.nms.core.api.internal.model.email.request.CreateEmailNotificationRequest;
import com.sfl.nms.core.api.internal.model.email.response.CreateEmailNotificationResponse;
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
@Path("notification/email")
@Produces("application/json")
public class EmailNotificationResource {

    private static final Logger LOGGER = LoggerFactory.getLogger(EmailNotificationResource.class);

    /* Dependencies */
    @Autowired
    private EmailNotificationServiceFacade emailNotificationServiceFacade;

    /* Constructors */
    public EmailNotificationResource() {
    }

    @POST
    @Path("create")
    @Consumes("application/json")
    public Response createEmailNotification(final CreateEmailNotificationRequest request) {
        LOGGER.debug("Processing create email notification request - {}", request);
        final ResultResponseModel<CreateEmailNotificationResponse> response = emailNotificationServiceFacade.createEmailNotification(request);
        LOGGER.debug("Processed create email notification request - {}, response - {}", request, response);
        return Response.ok(response).build();
    }
}
