package com.sflpro.notifier.api.facade.endpoints.notification.email;

import com.sflpro.notifier.api.model.common.result.ErrorResponseModel;
import com.sflpro.notifier.api.model.common.result.ResultResponseModel;
import com.sflpro.notifier.api.model.email.EmailNotificationModel;
import com.sflpro.notifier.api.model.email.request.CreateEmailNotificationRequest;
import com.sflpro.notifier.api.model.email.response.CreateEmailNotificationResponse;
import com.sflpro.notifier.api.model.notification.NotificationClientType;
import com.sflpro.notifier.api.model.notification.NotificationStateClientType;
import com.sflpro.notifier.db.entities.notification.email.EmailNotification;
import com.sflpro.notifier.services.notification.dto.NotificationPropertyDto;
import com.sflpro.notifier.services.notification.dto.email.EmailNotificationDto;
import com.sflpro.notifier.services.notification.email.EmailNotificationService;
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
import java.util.Map;
import java.util.stream.Collectors;

/**
 * User: Ruben Dilanyan
 * Company: SFL LLC
 * Date: 1/14/16
 * Time: 12:40 PM
 */
@Singleton
@Path("notification/email")
@Produces(MediaType.APPLICATION_JSON)
public class EmailNotificationResource {

    private static final Logger LOGGER = LoggerFactory.getLogger(EmailNotificationResource.class);

    /* Dependencies */
    @Inject
    private EmailNotificationService emailNotificationService;

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
        final EmailNotificationDto emailNotificationDto = buildDto(request);
        final EmailNotification emailNotification = emailNotificationService.createAndSendEmailNotification(emailNotificationDto, buildPropertiesDto(request.getProperties()));
        final EmailNotificationModel emailNotificationModel = buildModel(emailNotification);
        final ResultResponseModel<CreateEmailNotificationResponse> response = new ResultResponseModel<>(new CreateEmailNotificationResponse(emailNotificationModel));
        LOGGER.debug("Processed create email notification request - {}, response - {}", request, response);
        return Response.ok(response).build();
    }

    /* utility methods */
    private EmailNotificationDto buildDto(final CreateEmailNotificationRequest request) {
        final EmailNotificationDto emailNotificationDto = new EmailNotificationDto();
        emailNotificationDto.setRecipientEmail(request.getRecipientEmail());
        emailNotificationDto.setSenderEmail(request.getSenderEmail());
        emailNotificationDto.setContent(request.getBody());
        emailNotificationDto.setSubject(request.getSubject());
        emailNotificationDto.setClientIpAddress(request.getClientIpAddress());
        emailNotificationDto.setTemplateName(request.getTemplateName());
        emailNotificationDto.setSecureProperties(request.getSecureProperties());
        emailNotificationDto.setUserUuid(request.getUserUuId());
        return emailNotificationDto;
    }

    private EmailNotificationModel buildModel(final EmailNotification emailNotification) {
        final EmailNotificationModel notificationModel = new EmailNotificationModel();
        notificationModel.setUuId(emailNotification.getUuId());
        notificationModel.setBody(emailNotification.getContent());
        notificationModel.setSubject(emailNotification.getSubject());
        notificationModel.setType(NotificationClientType.valueOf(emailNotification.getType().name()));
        notificationModel.setState(NotificationStateClientType.valueOf(emailNotification.getState().name()));
        notificationModel.setSenderEmail(emailNotification.getSenderEmail());
        notificationModel.setRecipientEmail(emailNotification.getRecipientEmail());
        return notificationModel;
    }

    private List<NotificationPropertyDto> buildPropertiesDto(final Map<String, String> properties) {
        return properties.entrySet()
                .stream()
                .map(entry -> new NotificationPropertyDto(entry.getKey(), entry.getValue()))
                .collect(Collectors.toList());
    }
}
