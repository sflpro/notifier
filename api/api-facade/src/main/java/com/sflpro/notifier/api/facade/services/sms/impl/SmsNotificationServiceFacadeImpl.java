package com.sflpro.notifier.api.facade.services.sms.impl;

import com.sflpro.notifier.api.facade.services.AbstractNotificationServiceFacadeImpl;
import com.sflpro.notifier.api.facade.services.sms.SmsNotificationServiceFacade;
import com.sflpro.notifier.api.model.common.result.ErrorResponseModel;
import com.sflpro.notifier.api.model.common.result.ResultResponseModel;
import com.sflpro.notifier.api.model.sms.SmsNotificationModel;
import com.sflpro.notifier.api.model.sms.request.CreateSmsNotificationRequest;
import com.sflpro.notifier.api.model.sms.response.CreateSmsNotificationResponse;
import com.sflpro.notifier.db.entities.notification.NotificationProviderType;
import com.sflpro.notifier.db.entities.notification.sms.SmsNotification;
import com.sflpro.notifier.services.notification.dto.sms.SmsNotificationDto;
import com.sflpro.notifier.services.notification.dto.sms.SmsNotificationPropertyDto;
import com.sflpro.notifier.services.notification.event.sms.StartSendingNotificationEvent;
import com.sflpro.notifier.services.notification.sms.SmsNotificationService;
import com.sflpro.notifier.services.system.event.ApplicationEventDistributionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.stream.Collectors;

/**
 * User: Ruben Dilanyan
 * Company: SFL LLC
 * Date: 1/13/16
 * Time: 5:13 PM
 */
@Component
public class SmsNotificationServiceFacadeImpl extends AbstractNotificationServiceFacadeImpl implements SmsNotificationServiceFacade {

    private static final Logger LOGGER = LoggerFactory.getLogger(SmsNotificationServiceFacadeImpl.class);

    /* Dependencies */
    @Autowired
    private SmsNotificationService smsNotificationService;

    @Autowired
    private ApplicationEventDistributionService applicationEventDistributionService;

    /* Constructors */
    public SmsNotificationServiceFacadeImpl() {
        super();
    }

    @Nonnull
    @Override
    public ResultResponseModel<CreateSmsNotificationResponse> createSmsNotification(@Nonnull final CreateSmsNotificationRequest request) {
        Assert.notNull(request, "Request model should not be null");
        LOGGER.debug("Creating SMS notification, request - {}", request);
        // Validate request
        final List<ErrorResponseModel> errors = request.validateRequiredFields();
        if (!errors.isEmpty()) {
            return new ResultResponseModel<>(errors);
        }
        // Create notification DTO
        final SmsNotificationDto smsNotificationDto = buildSmsNotificationDto(request);
        final List<SmsNotificationPropertyDto> smsNotificationPropertyDtos = request.getProperties().entrySet()
                .stream()
                .map(entry -> new SmsNotificationPropertyDto(entry.getKey(), entry.getValue()))
                .collect(Collectors.toList());

        final SmsNotification smsNotification = smsNotificationService.createSmsNotification(smsNotificationDto, smsNotificationPropertyDtos);
        associateUserWithNotificationIfRequired(request.getUserUuId(), smsNotification);
        // Publish event
        applicationEventDistributionService.publishAsynchronousEvent(new StartSendingNotificationEvent(smsNotification.getId(), request.getSecureProperties()));
        // Create response model
        final SmsNotificationModel smsNotificationModel = createSmsNotificationModel(smsNotification);
        final CreateSmsNotificationResponse response = new CreateSmsNotificationResponse(smsNotificationModel);
        LOGGER.debug("Successfully created response model for SMS request - {}, response - {}", request, response);
        return new ResultResponseModel<>(response);
    }

    /* Utility methods */
    private SmsNotificationModel createSmsNotificationModel(final SmsNotification smsNotification) {
        final SmsNotificationModel notificationModel = new SmsNotificationModel();
        setNotificationCommonProperties(notificationModel, smsNotification);
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
        smsNotificationDto.setHasSecureProperties(!request.getSecureProperties().isEmpty());
        return smsNotificationDto;
    }
}
