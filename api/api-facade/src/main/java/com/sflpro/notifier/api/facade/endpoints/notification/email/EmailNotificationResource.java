package com.sflpro.notifier.api.facade.endpoints.notification.email;

import com.sflpro.notifier.api.facade.services.email.EmailNotificationServiceFacade;
import com.sflpro.notifier.api.model.common.result.ErrorResponseModel;
import com.sflpro.notifier.api.model.common.result.ResultResponseModel;
import com.sflpro.notifier.api.model.email.request.CreateEmailNotificationRequest;
import com.sflpro.notifier.api.model.email.response.CreateEmailNotificationResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.SwaggerDefinition;
import io.swagger.annotations.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

/**
 * User: Ruben Dilanyan
 * Company: SFL LLC
 * Date: 1/14/16
 * Time: 12:40 PM
 */
@SwaggerDefinition(tags = {@Tag(name = "email", description = "Email-notification operations")})
@Api(tags = {"email"})
@Singleton
@Path("notification/email")
@Produces(MediaType.APPLICATION_JSON)
public class EmailNotificationResource {

    private static final Logger LOGGER = LoggerFactory.getLogger(EmailNotificationResource.class);

    /* Dependencies */
    @Inject
    private EmailNotificationServiceFacade emailNotificationServiceFacade;

    /* Constructors */
    public EmailNotificationResource() {
        //default constructor
    }

    @ApiOperation("Creates email notification")
    @POST
    @Path("create")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createEmailNotification(final CreateEmailNotificationRequest request) {
        LOGGER.debug("Processing create email notification request - {}", request);
        Assert.notNull(request, "Request model should not be null");
        LOGGER.debug("Creating email notification, request - {}", request);
        // Validate request
        final List<ErrorResponseModel> errors = request.validateRequiredFields();
        if (!errors.isEmpty()) {
            return Response.ok(new ResultResponseModel<CreateEmailNotificationResponse>(errors)).build();
        }
        final ResultResponseModel<CreateEmailNotificationResponse> response = emailNotificationServiceFacade.createEmailNotification(request);
        LOGGER.debug("Processed create email notification request - {}, response - {}", request, response);
        return Response.ok(response).build();
    }


}
