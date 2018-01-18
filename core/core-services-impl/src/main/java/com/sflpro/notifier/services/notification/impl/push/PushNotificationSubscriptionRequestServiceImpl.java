package com.sflpro.notifier.services.notification.impl.push;

import com.sflpro.notifier.persistence.repositories.notification.push.PushNotificationSubscriptionRequestRepository;
import com.sflpro.notifier.services.device.UserDeviceService;
import com.sflpro.notifier.services.device.model.UserDevice;
import com.sflpro.notifier.services.notification.dto.push.PushNotificationSubscriptionRequestDto;
import com.sflpro.notifier.services.notification.exception.push.PushNotificationSubscriptionInvalidDeviceUserException;
import com.sflpro.notifier.services.notification.exception.push.PushNotificationSubscriptionRequestInvalidRecipientUserException;
import com.sflpro.notifier.services.notification.exception.push.PushNotificationSubscriptionRequestNotFoundForIdException;
import com.sflpro.notifier.services.notification.exception.push.PushNotificationSubscriptionRequestNotFoundForUuIdException;
import com.sflpro.notifier.services.notification.model.push.PushNotificationRecipient;
import com.sflpro.notifier.services.notification.model.push.PushNotificationSubscriptionRequest;
import com.sflpro.notifier.services.notification.model.push.PushNotificationSubscriptionRequestState;
import com.sflpro.notifier.services.notification.push.PushNotificationRecipientService;
import com.sflpro.notifier.services.notification.push.PushNotificationSubscriptionRequestService;
import com.sflpro.notifier.services.user.UserService;
import com.sflpro.notifier.services.user.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import javax.annotation.Nonnull;
import java.util.Date;

/**
 * User: Ruben Dilanyan
 * Company: SFL LLC
 * Date: 8/21/15
 * Time: 9:18 AM
 */
@Service
public class PushNotificationSubscriptionRequestServiceImpl implements PushNotificationSubscriptionRequestService {

    private static final Logger LOGGER = LoggerFactory.getLogger(PushNotificationSubscriptionRequestServiceImpl.class);

    /* Dependencies */
    @Autowired
    private PushNotificationSubscriptionRequestRepository pushNotificationSubscriptionRequestRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private UserDeviceService userMobileDeviceService;

    @Autowired
    private PushNotificationRecipientService pushNotificationRecipientService;

    /* Constructors */
    public PushNotificationSubscriptionRequestServiceImpl() {
        LOGGER.debug("Initializing push notification subscription request service");
    }

    @Transactional
    @Nonnull
    @Override
    public PushNotificationSubscriptionRequest createPushNotificationSubscriptionRequest(@Nonnull final Long userId, @Nonnull final Long userMobileDeviceId, @Nonnull final PushNotificationSubscriptionRequestDto requestDto) {
        Assert.notNull(userId, "User id should not be null");
        Assert.notNull(userMobileDeviceId, "User device id should not be null");
        assertPushNotificationSubscriptionRequestDto(requestDto);
        LOGGER.debug("Creating push notification subscription request for user with id - {}, user device id - {}, DTO - {}", userId, userMobileDeviceId, requestDto);
        final User user = userService.getUserById(userId);
        final UserDevice userMobileDevice = userMobileDeviceService.getUserDeviceById(userMobileDeviceId);
        assertSubscriptionAndMobileDeviceUsers(user, userMobileDevice);
        // Create push notification subscription request
        PushNotificationSubscriptionRequest request = new PushNotificationSubscriptionRequest(true);
        requestDto.updateDomainEntityProperties(request);
        request.setUser(user);
        request.setUserMobileDevice(userMobileDevice);
        // Persist request
        request = pushNotificationSubscriptionRequestRepository.save(request);
        LOGGER.debug("Successfully created push notification subscription request with id - {}, request - {}", request.getId(), request);
        return request;
    }

    @Nonnull
    @Override
    public PushNotificationSubscriptionRequest getPushNotificationSubscriptionRequestById(@Nonnull final Long requestId) {
        assertPushNotificationSubscriptionRequestId(requestId);
        LOGGER.debug("Getting push notification subscription request for id - {}", requestId);
        final PushNotificationSubscriptionRequest request = pushNotificationSubscriptionRequestRepository.findOne(requestId);
        assertPushNotificationSubscriptionRequestNotNullForId(request, requestId);
        LOGGER.debug("Successfully retrieved push notification subscription request for id - {}, request - {}", request.getId(), request);
        return request;
    }

    @Transactional
    @Nonnull
    @Override
    public PushNotificationSubscriptionRequest updatePushNotificationSubscriptionRequestState(@Nonnull final Long requestId, @Nonnull final PushNotificationSubscriptionRequestState state) {
        assertPushNotificationSubscriptionRequestId(requestId);
        Assert.notNull(state, "Push notification request state should not be null");
        LOGGER.debug("Updating push notification request state for request with id - {}, state - {}", requestId, state);
        PushNotificationSubscriptionRequest request = pushNotificationSubscriptionRequestRepository.findOne(requestId);
        assertPushNotificationSubscriptionRequestNotNullForId(request, requestId);
        // Update state and persist
        request.setState(state);
        request.setUpdated(new Date());
        request = pushNotificationSubscriptionRequestRepository.save(request);
        LOGGER.debug("Successfully updated push notification request state for request with id - {}, request - {}", request.getId(), request);
        return request;
    }

    @Nonnull
    @Override
    public PushNotificationSubscriptionRequest getPushNotificationSubscriptionRequestByUuId(@Nonnull final String uuId) {
        assertPushNotificationSubscriptionRequestUuId(uuId);
        LOGGER.debug("Getting push notification subscription request for uuid - {}", uuId);
        final PushNotificationSubscriptionRequest request = pushNotificationSubscriptionRequestRepository.findByUuId(uuId);
        assertPushNotificationSubscriptionRequestNotNullForUuId(request, uuId);
        LOGGER.debug("Successfully retrieved push notification subscription request with uuId - {}, request - {}", request.getUuId(), request);
        return request;
    }

    @Nonnull
    @Override
    public boolean checkIfPushNotificationSubscriptionRecipientExistsForUuId(@Nonnull final String uuId) {
        assertPushNotificationSubscriptionRequestUuId(uuId);
        LOGGER.debug("Checking if push notification subscription request exists for uuid - {}", uuId);
        final PushNotificationSubscriptionRequest request = pushNotificationSubscriptionRequestRepository.findByUuId(uuId);
        final boolean exists = (request != null);
        LOGGER.debug("Push notification subscription request lookup result for uuid - {} is - {}", uuId, exists);
        return exists;
    }

    @Transactional
    @Nonnull
    @Override
    public PushNotificationSubscriptionRequest updatePushNotificationSubscriptionRequestRecipient(@Nonnull final Long requestId, @Nonnull final Long recipientId) {
        assertPushNotificationSubscriptionRequestId(requestId);
        Assert.notNull(recipientId, "Push notification subscription recipient id should not be null");
        LOGGER.debug("Updating push notification recipient for subscription request with id - {}, recipient id - {}", requestId, recipientId);
        PushNotificationSubscriptionRequest request = pushNotificationSubscriptionRequestRepository.findOne(requestId);
        assertPushNotificationSubscriptionRequestNotNullForId(request, requestId);
        final PushNotificationRecipient recipient = pushNotificationRecipientService.getPushNotificationRecipientById(recipientId);
        assertSubscriptionRequestAndRecipientUsers(request, recipient);
        // Update recipient on request and persist it
        request.setRecipient(recipient);
        request.setUpdated(new Date());
        request = pushNotificationSubscriptionRequestRepository.save(request);
        LOGGER.debug("Successfully updated recipient for push notification subscription request with id - {}, recipient id - {}, request - {}", request.getId(), recipientId, request);
        return request;
    }

    /* Utility methods */
    private void assertSubscriptionRequestAndRecipientUsers(final PushNotificationSubscriptionRequest request, final PushNotificationRecipient recipient) {
        final Long recipientId = recipient.getId();
        final Long recipientUserId = recipient.getSubscription().getUser().getId();
        final Long requestId = request.getId();
        final Long requestUserId = request.getUser().getId();
        if (!requestUserId.equals(recipientUserId)) {
            LOGGER.error("Push notification subscription request with id - {} belongs to user with id - {} where as recipient with id - {} belongs to user with id - {}", requestId, requestUserId, recipientId, recipientUserId);
            throw new PushNotificationSubscriptionRequestInvalidRecipientUserException(requestId, requestUserId, recipientId, recipientUserId);
        }
    }

    private void assertPushNotificationSubscriptionRequestUuId(final String uuId) {
        Assert.notNull(uuId, "Push notification subscription uuid should not be null");
    }

    private void assertPushNotificationSubscriptionRequestId(final Long requestId) {
        Assert.notNull(requestId, "Push notification subscription request id should not be null");
    }

    private void assertPushNotificationSubscriptionRequestNotNullForId(final PushNotificationSubscriptionRequest request, final Long id) {
        if (request == null) {
            LOGGER.error("No push notification subscription request was found for id - {}", id);
            throw new PushNotificationSubscriptionRequestNotFoundForIdException(id);
        }
    }

    private void assertPushNotificationSubscriptionRequestNotNullForUuId(final PushNotificationSubscriptionRequest request, final String uuId) {
        if (request == null) {
            LOGGER.error("No push notification subscription request was found for uuid - {}", uuId);
            throw new PushNotificationSubscriptionRequestNotFoundForUuIdException(uuId);
        }
    }

    private void assertSubscriptionAndMobileDeviceUsers(final User user, final UserDevice userMobileDevice) {
        final Long userId = user.getId();
        final Long deviceUserId = userMobileDevice.getUser().getId();
        final Long deviceId = userMobileDevice.getId();
        if (!userId.equals(deviceUserId)) {
            LOGGER.error("User mobile device id - {} belongs to user with id - {} where as subscription is requested for user with id - {}", deviceId, deviceUserId, userId);
            throw new PushNotificationSubscriptionInvalidDeviceUserException(userId, deviceId, deviceUserId);
        }
    }

    private void assertPushNotificationSubscriptionRequestDto(final PushNotificationSubscriptionRequestDto requestDto) {
        Assert.notNull(requestDto, "Push notification subscription request should not be null");
        Assert.notNull(requestDto.getUserDeviceToken(), "User device token in push notification subscription request should not be null");
        Assert.notNull(requestDto.getApplicationType(), "Application type in push notification subscription request should not be null");
    }

    /* Properties getters and setters */
    public PushNotificationSubscriptionRequestRepository getPushNotificationSubscriptionRequestRepository() {
        return pushNotificationSubscriptionRequestRepository;
    }

    public void setPushNotificationSubscriptionRequestRepository(final PushNotificationSubscriptionRequestRepository pushNotificationSubscriptionRequestRepository) {
        this.pushNotificationSubscriptionRequestRepository = pushNotificationSubscriptionRequestRepository;
    }

    public UserService getUserService() {
        return userService;
    }

    public void setUserService(final UserService userService) {
        this.userService = userService;
    }

    public UserDeviceService getUserMobileDeviceService() {
        return userMobileDeviceService;
    }

    public void setUserMobileDeviceService(final UserDeviceService userMobileDeviceService) {
        this.userMobileDeviceService = userMobileDeviceService;
    }

    public PushNotificationRecipientService getPushNotificationRecipientService() {
        return pushNotificationRecipientService;
    }

    public void setPushNotificationRecipientService(final PushNotificationRecipientService pushNotificationRecipientService) {
        this.pushNotificationRecipientService = pushNotificationRecipientService;
    }
}
