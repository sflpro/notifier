package com.sflpro.notifier.api.internal.facade.notification.sms.impl;

import com.sflpro.notifier.api.internal.facade.notification.common.impl.AbstractNotificationServiceFacadeImpl;
import com.sflpro.notifier.api.internal.facade.notification.sms.SmsNotificationServiceFacade;
import com.sflpro.notifier.core.api.internal.model.common.result.ErrorResponseModel;
import com.sflpro.notifier.core.api.internal.model.common.result.ResultResponseModel;
import com.sflpro.notifier.core.api.internal.model.sms.SmsNotificationModel;
import com.sflpro.notifier.core.api.internal.model.sms.request.CreateSmsNotificationRequest;
import com.sflpro.notifier.core.api.internal.model.sms.response.CreateSmsNotificationResponse;
import com.sflpro.notifier.services.notification.dto.sms.SmsNotificationDto;
import com.sflpro.notifier.services.notification.event.sms.StartSendingNotificationEvent;
import com.sflpro.notifier.services.notification.model.NotificationProviderType;
import com.sflpro.notifier.services.notification.model.sms.SmsNotification;
import com.sflpro.notifier.services.notification.sms.SmsNotificationService;
import com.sflpro.notifier.services.system.event.ApplicationEventDistributionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
public class SmsNotificationServiceFacadeImpl extends AbstractNotificationServiceFacadeImpl implements SmsNotificationServiceFacade {

    private static final Logger LOGGER = LoggerFactory.getLogger(SmsNotificationServiceFacadeImpl.class);

    /* Dependencies */
    @Autowired
    private SmsNotificationService smsNotificationService;

    @Autowired
    private ApplicationEventDistributionService applicationEventDistributionService;

    /* Constructors */
    public SmsNotificationServiceFacadeImpl() {
    }

    @Nonnull
    @Override
    public ResultResponseModel<CreateSmsNotificationResponse> createSmsNotification(@Nonnull final CreateSmsNotificationRequest request) {
        Assert.notNull(request, "Request model should not be null");
        LOGGER.debug("Creating SMS notification, request - {}", request);
        // Validate request
        final List<ErrorResponseModel> errors = request.validateRequiredFields();
        if (errors.size() != 0) {
            return new ResultResponseModel<>(errors);
        }
        // Create notification DTO
        final SmsNotificationDto smsNotificationDto = new SmsNotificationDto(request.getRecipientNumber(), NotificationProviderType.AMAZON_SNS, request.getBody(), request.getClientIpAddress());
        final SmsNotification smsNotification = smsNotificationService.createSmsNotification(smsNotificationDto);
        associateUserWithNotificationIfRequired(request.getUserUuId(), smsNotification);
        // Publish event
        applicationEventDistributionService.publishAsynchronousEvent(new StartSendingNotificationEvent(smsNotification.getId()));
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

    /* Properties getters and setters */
    public SmsNotificationService getSmsNotificationService() {
        return smsNotificationService;
    }

    public void setSmsNotificationService(final SmsNotificationService smsNotificationService) {
        this.smsNotificationService = smsNotificationService;
    }

    public ApplicationEventDistributionService getApplicationEventDistributionService() {
        return applicationEventDistributionService;
    }

    public void setApplicationEventDistributionService(final ApplicationEventDistributionService applicationEventDistributionService) {
        this.applicationEventDistributionService = applicationEventDistributionService;
    }
}
