package com.sflpro.notifier.api.facade.endpoints.notification.email;

import com.sflpro.notifier.api.model.common.result.ErrorResponseModel;
import com.sflpro.notifier.api.model.common.result.ResultResponseModel;
import com.sflpro.notifier.api.model.email.request.CreateEmailNotificationRequest;
import com.sflpro.notifier.api.model.email.response.CreateEmailNotificationResponse;
import com.sflpro.notifier.db.entities.notification.email.EmailNotification;
import com.sflpro.notifier.services.notification.component.SendEmailNotificationComponent;
import com.sflpro.notifier.services.notification.email.EmailNotificationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

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
@Component
@Path("notification/email")
@Produces(MediaType.APPLICATION_JSON)
public class EmailNotificationResource {

    private static final Logger LOGGER = LoggerFactory.getLogger(EmailNotificationResource.class);

    /* Dependencies */
    @Autowired
    private EmailNotificationModelConverter emailNotificationModelConverter;

    @Autowired
    private EmailNotificationService emailNotificationService;

    @Autowired
    private SendEmailNotificationComponent sendEmailNotificationComponent;

    /* Constructors */
    public EmailNotificationResource() {
        //default constructor
    }

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
        final EmailNotification emailNotification = emailNotificationService.createEmailNotification(emailNotificationModelConverter.toDto(request), emailNotificationModelConverter.toPropertiesDtoList(request.getProperties()));
        sendEmailNotificationComponent.sendEmailNotification(emailNotificationModelConverter.toSendEmailNotificationModel(emailNotification.getId(), request));
        final ResultResponseModel<CreateEmailNotificationResponse> response = new ResultResponseModel<>(new CreateEmailNotificationResponse(emailNotificationModelConverter.toModel(emailNotification)));
        LOGGER.debug("Processed create email notification request - {}, response - {}", request, response);
        return Response.ok(response).build();
    }
}
