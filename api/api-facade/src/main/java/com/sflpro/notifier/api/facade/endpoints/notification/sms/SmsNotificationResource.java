package com.sflpro.notifier.api.facade.endpoints.notification.sms;

import com.sflpro.notifier.api.model.common.result.ErrorResponseModel;
import com.sflpro.notifier.api.model.common.result.ResultResponseModel;
import com.sflpro.notifier.api.model.notification.NotificationClientType;
import com.sflpro.notifier.api.model.notification.NotificationStateClientType;
import com.sflpro.notifier.api.model.sms.SmsNotificationModel;
import com.sflpro.notifier.api.model.sms.request.CreateSmsNotificationRequest;
import com.sflpro.notifier.api.model.sms.response.CreateSmsNotificationResponse;
import com.sflpro.notifier.db.entities.notification.NotificationProviderType;
import com.sflpro.notifier.db.entities.notification.sms.SmsNotification;
import com.sflpro.notifier.services.notification.dto.sms.SmsNotificationDto;
import com.sflpro.notifier.services.notification.dto.sms.SmsNotificationPropertyDto;
import com.sflpro.notifier.services.notification.sms.SmsNotificationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.stream.Collectors;

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
    private SmsNotificationService smsNotificationService;

    /* Constructors */
    public SmsNotificationResource() {
        super();
    }

    @POST
    @Path("create")
    @Consumes("application/json")
    public Response createSmsNotification(final CreateSmsNotificationRequest request) {
        Assert.notNull(request, "Request model should not be null");
        final List<ErrorResponseModel> errors = request.validateRequiredFields();
        if (!errors.isEmpty()) {
            return Response.status(Response.Status.BAD_REQUEST).entity(new ResultResponseModel<CreateSmsNotificationResponse>(errors)).build();
        }
        LOGGER.debug("Processing create SMS notification request - {}", request);
        final SmsNotificationDto smsNotificationDto = buildSmsNotificationDto(request);
        final List<SmsNotificationPropertyDto> smsNotificationPropertyDtos = buildPropertiesDto(request);

        final SmsNotification smsNotification = smsNotificationService.createSmsNotification(smsNotificationDto, smsNotificationPropertyDtos);

        final SmsNotificationModel smsNotificationModel = buildSmsNotificationModel(smsNotification);
        final CreateSmsNotificationResponse response = new CreateSmsNotificationResponse(smsNotificationModel);
        LOGGER.debug("Processed create SMS notification request - {}, response - {}", request, response);
        return Response.ok(response).build();
    }

    /* Utility methods */
    private SmsNotificationModel buildSmsNotificationModel(final SmsNotification smsNotification) {
        final SmsNotificationModel notificationModel = new SmsNotificationModel();
        notificationModel.setUuId(smsNotification.getUuId());
        notificationModel.setBody(smsNotification.getContent());
        notificationModel.setSubject(smsNotification.getSubject());
        notificationModel.setType(NotificationClientType.valueOf(smsNotification.getType().name()));
        notificationModel.setState(NotificationStateClientType.valueOf(smsNotification.getState().name()));
        notificationModel.setSubject(smsNotification.getSubject());
        notificationModel.setRecipientNumber(smsNotification.getRecipientMobileNumber());
        return notificationModel;
    }

    private SmsNotificationDto buildSmsNotificationDto(final CreateSmsNotificationRequest request) {
        final SmsNotificationDto smsNotificationDto = new SmsNotificationDto();
        smsNotificationDto.setRecipientMobileNumber(request.getRecipientNumber());
        smsNotificationDto.setProviderType(NotificationProviderType.AMAZON_SNS);
        smsNotificationDto.setContent(request.getBody());
        smsNotificationDto.setClientIpAddress(request.getClientIpAddress());
        smsNotificationDto.setTemplateName(request.getTemplateName());
        smsNotificationDto.setSecureProperties(request.getSecureProperties());
        smsNotificationDto.setUserUuid(request.getUserUuId());
        return smsNotificationDto;
    }

    private List<SmsNotificationPropertyDto>  buildPropertiesDto(final CreateSmsNotificationRequest request) {
        return request.getProperties().entrySet()
                .stream()
                .map(entry -> new SmsNotificationPropertyDto(entry.getKey(), entry.getValue()))
                .collect(Collectors.toList());
    }
}