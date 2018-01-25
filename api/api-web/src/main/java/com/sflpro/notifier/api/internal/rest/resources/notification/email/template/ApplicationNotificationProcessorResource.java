package com.sflpro.notifier.api.internal.rest.resources.notification.email.template;

import com.sflpro.notifier.api.internal.facade.notification.email.template.ApplicationNotificationProcessorFacade;
import com.sflpro.notifier.core.api.internal.model.common.result.ResultResponseModel;
import com.sflpro.notifier.core.api.internal.model.email.response.CreateEmailNotificationResponse;
import com.sflpro.notifier.core.api.internal.model.email.template.request.ForgotPasswordRequest;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.SwaggerDefinition;
import io.swagger.annotations.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Nonnull;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * User: Davit Yeghiazaryan
 * Company: SFL LLC
 * Date 6/15/16
 * Time 4:51 PM
 */
@SwaggerDefinition(tags = {@Tag(name = "email-template", description = "Email templates & related")})
@Api(tags = {"email-template"})
@Component
@Path("notification/event")
@Produces(MediaType.APPLICATION_JSON)
public class ApplicationNotificationProcessorResource {
    private static final Logger LOGGER = LoggerFactory.getLogger(ApplicationNotificationProcessorResource.class);

    /* Dependencies */
    @Autowired
    private ApplicationNotificationProcessorFacade applicationNotificationProcessorFacade;

    /* Constructors */
    public ApplicationNotificationProcessorResource() {
        LOGGER.debug("Initializing templated email notification resource");
    }

    @ApiOperation("Process forgot password requests")
    @POST
    @Path("forgotpassword")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response processForgotPassword(@Nonnull final ForgotPasswordRequest request) {
        LOGGER.debug("Processing forgot password email verification email notification request - {}", request);
        final ResultResponseModel<CreateEmailNotificationResponse> response = applicationNotificationProcessorFacade.processForgotPassword(request);
        LOGGER.debug("Processed forgot password email verification email. request - {}, response - {}", request, response);
        return Response.ok(response).build();
    }
}
