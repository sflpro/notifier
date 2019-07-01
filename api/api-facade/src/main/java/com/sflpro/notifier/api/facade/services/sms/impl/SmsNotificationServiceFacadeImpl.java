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
import com.sflpro.notifier.services.notification.event.sms.StartSendingNotificationEvent;
import com.sflpro.notifier.services.notification.sms.SmsNotificationService;
import com.sflpro.notifier.services.system.event.ApplicationEventDistributionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import javax.annotation.Nonnull;
import java.util.List;

/**
 * User: Ruben Dilanyan
 * Company: SFL LLC
 * Date: 1/13/16
 * Time: 5:13 PM
 */
@Component
class SmsNotificationServiceFacadeImpl extends AbstractNotificationServiceFacadeImpl implements SmsNotificationServiceFacade {

    private static final Logger LOGGER = LoggerFactory.getLogger(SmsNotificationServiceFacadeImpl.class);

    /* Dependencies */
    @Autowired
    private SmsNotificationService smsNotificationService;

    @Autowired
    private ApplicationEventDistributionService applicationEventDistributionService;

    @Value("${sms.provider:MSG_AM}")
    private NotificationProviderType providerType = NotificationProviderType.MSG_AM;

    /* Constructors */
    SmsNotificationServiceFacadeImpl() {
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
        final SmsNotificationDto smsNotificationDto = new SmsNotificationDto(request.getRecipientNumber(), request.getBody(), request.getClientIpAddress(), providerType);
        smsNotificationDto.setTemplateName(request.getTemplateName());
        smsNotificationDto.setProperties(request.getProperties());
        final SmsNotification smsNotification = smsNotificationService.createSmsNotification(smsNotificationDto);
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
}
