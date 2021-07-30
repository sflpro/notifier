package com.sflpro.notifier.api.facade.services.push.impl;

import com.sflpro.notifier.api.facade.services.AbstractNotificationServiceFacadeImpl;
import com.sflpro.notifier.api.facade.services.push.PushNotificationServiceFacade;
import com.sflpro.notifier.api.model.common.result.ErrorResponseModel;
import com.sflpro.notifier.api.model.common.result.ResultResponseModel;
import com.sflpro.notifier.api.model.push.PushNotificationModel;
import com.sflpro.notifier.api.model.push.PushNotificationRecipientModel;
import com.sflpro.notifier.api.model.push.request.CreatePushNotificationRequest;
import com.sflpro.notifier.api.model.push.request.UpdatePushNotificationSubscriptionRequest;
import com.sflpro.notifier.api.model.push.response.CreatePushNotificationResponse;
import com.sflpro.notifier.api.model.push.response.UpdatePushNotificationSubscriptionResponse;
import com.sflpro.notifier.db.entities.device.UserDevice;
import com.sflpro.notifier.db.entities.device.mobile.DeviceOperatingSystemType;
import com.sflpro.notifier.db.entities.notification.NotificationProviderType;
import com.sflpro.notifier.db.entities.notification.NotificationSendingPriority;
import com.sflpro.notifier.db.entities.notification.email.NotificationProperty;
import com.sflpro.notifier.db.entities.notification.push.PushNotification;
import com.sflpro.notifier.db.entities.notification.push.PushNotificationRecipient;
import com.sflpro.notifier.db.entities.notification.push.PushNotificationSubscriptionRequest;
import com.sflpro.notifier.db.entities.user.User;
import com.sflpro.notifier.services.device.UserDeviceService;
import com.sflpro.notifier.services.device.dto.UserDeviceDto;
import com.sflpro.notifier.services.notification.dto.push.PushNotificationDto;
import com.sflpro.notifier.services.notification.dto.push.PushNotificationSubscriptionRequestDto;
import com.sflpro.notifier.services.notification.event.push.StartPushNotificationSubscriptionRequestProcessingEvent;
import com.sflpro.notifier.services.notification.event.sms.StartSendingNotificationEvent;
import com.sflpro.notifier.services.notification.push.PushNotificationService;
import com.sflpro.notifier.services.notification.push.PushNotificationSubscriptionRequestService;
import com.sflpro.notifier.services.system.event.ApplicationEventDistributionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * User: Ruben Dilanyan
 * Company: SFL LLC
 * Date: 1/13/16
 * Time: 6:13 PM
 */
@Component
public class PushNotificationServiceFacadeImpl extends AbstractNotificationServiceFacadeImpl implements PushNotificationServiceFacade {

    //region Logger

    private static final Logger logger = LoggerFactory.getLogger(PushNotificationServiceFacadeImpl.class);

    //endregion

    //region Injections

    @Autowired
    private PushNotificationService pushNotificationService;

    @Autowired
    private ApplicationEventDistributionService applicationEventDistributionService;

    @Autowired
    private UserDeviceService userDeviceService;

    @Autowired
    private PushNotificationSubscriptionRequestService pushNotificationSubscriptionRequestService;

    @Value("${push.notification.provider:AMAZON_SNS}")
    private NotificationProviderType providerType;

    public PushNotificationServiceFacadeImpl() {
        //default constructor
    }

    //endregion

    @Nonnull
    @Override
    public ResultResponseModel<CreatePushNotificationResponse> createPushNotifications(@Nonnull final CreatePushNotificationRequest request) {
        Assert.notNull(request, "Request should not be null");
        logger.debug("Creating push notifications for request - {}", request);
        // Validate request
        final List<ErrorResponseModel> errors = request.validateRequiredFields();
        if (!errors.isEmpty()) {
            return new ResultResponseModel<>(errors);
        }
        // Grab user
        final User user = getUserService().getOrCreateUserForUuId(request.getUserUuId());
        // Create push notification DTO
        final PushNotificationDto pushNotificationDto = new PushNotificationDto(request.getBody(), request.getSubject(), request.getClientIpAddress(), providerType);
        pushNotificationDto.setTemplateName(request.getTemplateName());
        pushNotificationDto.setLocale(request.getLocale());
        pushNotificationDto.setProperties(request.getProperties());
        pushNotificationDto.setSendingPriority(NotificationSendingPriority.valueOf(request.getSendingPriority().name()));
        // Create push notifications
        final List<PushNotification> pushNotifications = pushNotificationService.createNotificationsForUserActiveRecipients(user.getId(), pushNotificationDto);
        // Publish events
        pushNotifications.forEach(pushNotification -> applicationEventDistributionService.publishAsynchronousEvent(new StartSendingNotificationEvent(pushNotification.getId(), Collections.emptyMap(), pushNotification.getSendingPriority())));
        // Convert to notification models
        final List<PushNotificationModel> pushNotificationModels = pushNotifications.stream().map(PushNotificationServiceFacadeImpl::createPushNotificationModel).collect(Collectors.toCollection(ArrayList::new));
        // Create response model
        final CreatePushNotificationResponse response = new CreatePushNotificationResponse(pushNotificationModels);
        logger.debug("Successfully created push notification response - {} for request - {}", response, request);
        return new ResultResponseModel<>(response);
    }

    @Nonnull
    @Override
    public ResultResponseModel<UpdatePushNotificationSubscriptionResponse> updatePushNotificationSubscription(@Nonnull final UpdatePushNotificationSubscriptionRequest request) {
        Assert.notNull(request, "Request should not be null");
        logger.debug("Processing push notification subscription request - {}", request);
        // Validate request
        final List<ErrorResponseModel> errors = request.validateRequiredFields();
        if (!errors.isEmpty()) {
            return new ResultResponseModel<>(errors);
        }
        // Grab user
        final User user = getUserService().getOrCreateUserForUuId(request.getUserUuId());
        // Grab user device
        final UserDevice userDevice = userDeviceService.getOrCreateUserDevice(user.getId(), new UserDeviceDto(request.getDeviceUuId(), DeviceOperatingSystemType.valueOf(request.getDeviceOperatingSystemType().name())));
        // Create push notification subscription DTO
        final PushNotificationSubscriptionRequestDto requestDto = new PushNotificationSubscriptionRequestDto(request.getUserDeviceToken(), request.getApplication(), request.getSubscribe(), request.getLastUsedSubscriptionRequestUuId());
        // Create push notification request
        final PushNotificationSubscriptionRequest subscriptionRequest = pushNotificationSubscriptionRequestService.createPushNotificationSubscriptionRequest(user.getId(), userDevice.getId(), requestDto);
        applicationEventDistributionService.publishAsynchronousEvent(new StartPushNotificationSubscriptionRequestProcessingEvent(subscriptionRequest.getId()));
        // Create result
        final UpdatePushNotificationSubscriptionResponse response = new UpdatePushNotificationSubscriptionResponse(subscriptionRequest.getUuId());
        logger.debug("Successfully processed subscription request - {} , response - {}", request, response);
        return new ResultResponseModel<>(response);
    }

    //region Utility methods

    private static PushNotificationModel createPushNotificationModel(final PushNotification pushNotification) {
        final PushNotificationModel pushNotificationModel = new PushNotificationModel();
        setNotificationCommonProperties(pushNotificationModel, pushNotification);
        // Create recipient model
        pushNotificationModel.setRecipient(createPushNotificationRecipientModel(pushNotification.getRecipient()));
        // Set properties

        final Map<String, String> propertyModels = pushNotification.getProperties().stream().collect(Collectors.toMap(NotificationProperty::getPropertyKey, NotificationProperty::getPropertyValue));
        pushNotificationModel.setProperties(propertyModels);
        return pushNotificationModel;
    }

    private static PushNotificationRecipientModel createPushNotificationRecipientModel(final PushNotificationRecipient recipient) {
        final PushNotificationRecipientModel recipientModel = new PushNotificationRecipientModel();
        recipientModel.setApplicationType(recipient.getApplicationType());
        recipientModel.setDeviceOperatingSystemType(recipient.getDeviceOperatingSystemType().name());
        return recipientModel;
    }

    //endregion
}
