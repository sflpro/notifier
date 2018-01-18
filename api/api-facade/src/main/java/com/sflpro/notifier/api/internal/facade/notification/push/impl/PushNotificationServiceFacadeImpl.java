package com.sflpro.notifier.api.internal.facade.notification.push.impl;

import com.sflpro.notifier.api.internal.facade.notification.common.impl.AbstractNotificationServiceFacadeImpl;
import com.sflpro.notifier.api.internal.facade.notification.push.PushNotificationServiceFacade;
import com.sflpro.notifier.core.api.internal.model.common.result.ErrorResponseModel;
import com.sflpro.notifier.core.api.internal.model.common.result.ResultResponseModel;
import com.sflpro.notifier.core.api.internal.model.push.PushNotificationModel;
import com.sflpro.notifier.core.api.internal.model.push.PushNotificationPropertyModel;
import com.sflpro.notifier.core.api.internal.model.push.PushNotificationRecipientModel;
import com.sflpro.notifier.core.api.internal.model.push.request.CreatePushNotificationRequest;
import com.sflpro.notifier.core.api.internal.model.push.request.UpdatePushNotificationSubscriptionRequest;
import com.sflpro.notifier.core.api.internal.model.push.response.CreatePushNotificationResponse;
import com.sflpro.notifier.core.api.internal.model.push.response.UpdatePushNotificationSubscriptionResponse;
import com.sflpro.notifier.db.entities.device.UserDevice;
import com.sflpro.notifier.db.entities.user.User;
import com.sflpro.notifier.services.device.UserDeviceService;
import com.sflpro.notifier.services.device.dto.UserDeviceDto;
import com.sflpro.notifier.db.entities.device.mobile.DeviceOperatingSystemType;
import com.sflpro.notifier.services.notification.dto.push.PushNotificationDto;
import com.sflpro.notifier.services.notification.dto.push.PushNotificationPropertyDto;
import com.sflpro.notifier.services.notification.dto.push.PushNotificationSubscriptionRequestDto;
import com.sflpro.notifier.services.notification.event.push.StartPushNotificationSubscriptionRequestProcessingEvent;
import com.sflpro.notifier.services.notification.event.sms.StartSendingNotificationEvent;
import com.sflpro.notifier.db.entities.notification.push.PushNotification;
import com.sflpro.notifier.db.entities.notification.push.PushNotificationRecipient;
import com.sflpro.notifier.db.entities.notification.push.PushNotificationSubscriptionRequest;
import com.sflpro.notifier.services.notification.push.PushNotificationService;
import com.sflpro.notifier.services.notification.push.PushNotificationSubscriptionRequestService;
import com.sflpro.notifier.services.system.event.ApplicationEventDistributionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * User: Ruben Dilanyan
 * Company: SFL LLC
 * Date: 1/13/16
 * Time: 6:13 PM
 */
@Component
public class PushNotificationServiceFacadeImpl extends AbstractNotificationServiceFacadeImpl implements PushNotificationServiceFacade {

    private static final Logger LOGGER = LoggerFactory.getLogger(PushNotificationServiceFacadeImpl.class);

    /* Properties */
    @Autowired
    private PushNotificationService pushNotificationService;

    @Autowired
    private ApplicationEventDistributionService applicationEventDistributionService;

    @Autowired
    private UserDeviceService userDeviceService;

    @Autowired
    private PushNotificationSubscriptionRequestService pushNotificationSubscriptionRequestService;

    /* Constructors */
    public PushNotificationServiceFacadeImpl() {
        //default constructor
    }

    @Nonnull
    @Override
    public ResultResponseModel<CreatePushNotificationResponse> createPushNotifications(@Nonnull final CreatePushNotificationRequest request) {
        Assert.notNull(request, "Request should not be null");
        LOGGER.debug("Creating push notifications for request - {}", request);
        // Validate request
        final List<ErrorResponseModel> errors = request.validateRequiredFields();
        if (!errors.isEmpty()) {
            return new ResultResponseModel<>(errors);
        }
        // Grab user
        final User user = getUserService().getOrCreateUserForUuId(request.getUserUuId());
        // Create push notification DTO
        final PushNotificationDto pushNotificationDto = new PushNotificationDto(request.getBody(), request.getSubject(), request.getClientIpAddress());
        final List<PushNotificationPropertyDto> propertyDTOs = request.getProperties().stream().map(propertyModel -> new PushNotificationPropertyDto(propertyModel.getPropertyKey(), propertyModel.getPropertyValue())).collect(Collectors.toCollection(ArrayList::new));
        // Create push notifications
        final List<PushNotification> pushNotifications = pushNotificationService.createNotificationsForUserActiveRecipients(user.getId(), pushNotificationDto, propertyDTOs);
        // Publish events
        pushNotifications.forEach(pushNotification -> applicationEventDistributionService.publishAsynchronousEvent(new StartSendingNotificationEvent(pushNotification.getId())));
        // Convert to notification models
        final List<PushNotificationModel> pushNotificationModels = pushNotifications.stream().map(this::createPushNotificationModel).collect(Collectors.toCollection(ArrayList::new));
        // Create response model
        final CreatePushNotificationResponse response = new CreatePushNotificationResponse(pushNotificationModels);
        LOGGER.debug("Successfully created push notification response - {} for request - {}", response, request);
        return new ResultResponseModel<>(response);
    }

    @Nonnull
    @Override
    public ResultResponseModel<UpdatePushNotificationSubscriptionResponse> updatePushNotificationSubscription(@Nonnull final UpdatePushNotificationSubscriptionRequest request) {
        Assert.notNull(request, "Request should not be null");
        LOGGER.debug("Processing push notification subscription request - {}", request);
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
        LOGGER.debug("Successfully processed subscription request - {} , response - {}", request, response);
        return new ResultResponseModel<>(response);
    }

    /* Utility methods */
    private PushNotificationModel createPushNotificationModel(final PushNotification pushNotification) {
        final PushNotificationModel pushNotificationModel = new PushNotificationModel();
        setNotificationCommonProperties(pushNotificationModel, pushNotification);
        // Create recipient model
        pushNotificationModel.setRecipient(createPushNotificationRecipientModel(pushNotification.getRecipient()));
        // Set properties
        final List<PushNotificationPropertyModel> propertyModels = pushNotification.getProperties().stream().map(property -> new PushNotificationPropertyModel(property.getPropertyKey(), property.getPropertyValue())).collect(Collectors.toCollection(ArrayList::new));
        pushNotificationModel.setProperties(propertyModels);
        return pushNotificationModel;
    }

    private static PushNotificationRecipientModel createPushNotificationRecipientModel(final PushNotificationRecipient recipient) {
        final PushNotificationRecipientModel recipientModel = new PushNotificationRecipientModel();
        recipientModel.setApplicationType(recipient.getApplicationType());
        recipientModel.setDeviceOperatingSystemType(recipient.getDeviceOperatingSystemType().name());
        return recipientModel;
    }

    /* Properties getters and setters */
    public PushNotificationService getPushNotificationService() {
        return pushNotificationService;
    }

    public void setPushNotificationService(final PushNotificationService pushNotificationService) {
        this.pushNotificationService = pushNotificationService;
    }

    public ApplicationEventDistributionService getApplicationEventDistributionService() {
        return applicationEventDistributionService;
    }

    public void setApplicationEventDistributionService(final ApplicationEventDistributionService applicationEventDistributionService) {
        this.applicationEventDistributionService = applicationEventDistributionService;
    }

    public UserDeviceService getUserDeviceService() {
        return userDeviceService;
    }

    public void setUserDeviceService(final UserDeviceService userDeviceService) {
        this.userDeviceService = userDeviceService;
    }

    public PushNotificationSubscriptionRequestService getPushNotificationSubscriptionRequestService() {
        return pushNotificationSubscriptionRequestService;
    }

    public void setPushNotificationSubscriptionRequestService(final PushNotificationSubscriptionRequestService pushNotificationSubscriptionRequestService) {
        this.pushNotificationSubscriptionRequestService = pushNotificationSubscriptionRequestService;
    }
}
