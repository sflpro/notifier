package com.sflpro.notifier.services.notification.impl.push;

import com.sflpro.notifier.persistence.repositories.notification.push.AbstractPushNotificationRecipientRepository;
import com.sflpro.notifier.persistence.repositories.notification.push.PushNotificationRecipientDeviceRepository;
import com.sflpro.notifier.persistence.repositories.notification.push.PushNotificationRecipientRepository;
import com.sflpro.notifier.services.device.UserDeviceService;
import com.sflpro.notifier.services.device.model.UserDevice;
import com.sflpro.notifier.services.notification.dto.push.PushNotificationRecipientDto;
import com.sflpro.notifier.services.notification.exception.push.PushNotificationRecipientAlreadyExistsException;
import com.sflpro.notifier.services.notification.exception.push.PushNotificationRecipientInvalidDeviceUserException;
import com.sflpro.notifier.services.notification.exception.push.PushNotificationRecipientNotFoundForIdException;
import com.sflpro.notifier.services.notification.exception.push.PushNotificationRecipientNotFoundForLookupParametersException;
import com.sflpro.notifier.db.entities.notification.push.*;
import com.sflpro.notifier.services.notification.push.AbstractPushNotificationRecipientService;
import com.sflpro.notifier.services.notification.push.PushNotificationSubscriptionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import javax.annotation.Nonnull;
import java.util.Date;

/**
 * User: Ruben Dilanyan
 * Company: SFL LLC
 * Date: 8/13/15
 * Time: 1:13 PM
 */
public abstract class AbstractPushNotificationRecipientServiceImpl<T extends PushNotificationRecipient> implements AbstractPushNotificationRecipientService<T> {

    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractPushNotificationRecipientServiceImpl.class);

    /* Dependencies */
    @Autowired
    private PushNotificationSubscriptionService pushNotificationSubscriptionService;

    @Autowired
    private PushNotificationRecipientRepository pushNotificationRecipientRepository;

    @Autowired
    private PushNotificationRecipientDeviceRepository pushNotificationRecipientDeviceRepository;

    @Autowired
    private UserDeviceService userDeviceService;

    /* Constructors */
    public AbstractPushNotificationRecipientServiceImpl() {
        LOGGER.debug("Initializing abstract push notification recipient service");
    }

    @Nonnull
    @Override
    public T getPushNotificationRecipientById(@Nonnull final Long recipientId) {
        assertPushNotificationRecipientIdNotNull(recipientId);
        LOGGER.debug("Getting push notification recipient for id - {}, class - {}", recipientId, getInstanceClass());
        final T recipient = getRepository().findOne(recipientId);
        assertPushNotificationRecipientNotNullForId(recipient, recipientId);
        LOGGER.debug("Successfully retrieved push notification recipient for id - {}, class - {}, recipient - {}", recipient.getId(), getInstanceClass(), recipient);
        return recipient;
    }

    @Transactional
    @Nonnull
    @Override
    public T updatePushNotificationRecipientUserDevice(@Nonnull final Long recipientId, @Nonnull final Long userDeviceId) {
        assertPushNotificationRecipientIdNotNull(recipientId);
        Assert.notNull(userDeviceId, "User device id should not be null");
        LOGGER.debug("Updating user device for push notification recipient with id - {}, user device id - {}", recipientId, userDeviceId);
        T recipient = getRepository().findOne(recipientId);
        assertPushNotificationRecipientNotNullForId(recipient, recipientId);
        // Load user device
        final UserDevice userDevice = userDeviceService.getUserDeviceById(userDeviceId);
        assertPushNotificationRecipientAndDeviceBelongToSameUser(recipient, userDevice);
        // Check if device is already associated with recipient
        PushNotificationRecipientDevice recipientDevice = pushNotificationRecipientDeviceRepository.findByRecipientAndDevice(recipient, userDevice);
        if (recipientDevice == null) {
            LOGGER.debug("Push notification recipient with id - {} is not yet associated with user device by id - {}, associating them.", recipient.getId(), userDevice.getId());
            recipientDevice = new PushNotificationRecipientDevice();
            recipientDevice.setRecipient(recipient);
            recipientDevice.setDevice(userDevice);
            // Persist recipient device
            recipientDevice = pushNotificationRecipientDeviceRepository.save(recipientDevice);
            // Add to the collection of recipient devices
            recipient.getDevices().add(recipientDevice);
            LOGGER.debug("Successfully associated push notification recipient with id - {} to user device with id - {}, created recipient device id - {}", recipient.getId(), userDevice.getId(), recipientDevice.getId());
        }
        // Update last used device for recipient
        recipient.setLastDevice(userDevice);
        recipient.setUpdated(new Date());
        // Persist recipient
        recipient = getRepository().save(recipient);
        LOGGER.debug("Successfully updated user device for push notification recipient with id - {}. User device id - {}", recipient.getId(), userDevice.getId());
        return recipient;
    }

    @Transactional
    @Nonnull
    @Override
    public T updatePushNotificationRecipientStatus(@Nonnull final Long recipientId, @Nonnull final PushNotificationRecipientStatus status) {
        assertPushNotificationRecipientIdNotNull(recipientId);
        Assert.notNull(status, "Push notification recipient status should not be null");
        LOGGER.debug("Updating push notification recipient status for recipient with id - {} , status - {}", recipientId, status);
        T recipient = getRepository().findOne(recipientId);
        assertPushNotificationRecipientNotNullForId(recipient, recipientId);
        // Update recipient status
        recipient.setStatus(status);
        recipient.setUpdated(new Date());
        // Persist recipient
        recipient = getRepository().save(recipient);
        LOGGER.debug("Successfully update push notification recipient status with id - {}, recipient - {}", recipient.getId(), recipient);
        return recipient;
    }

    /* Abstract methods */
    protected abstract AbstractPushNotificationRecipientRepository<T> getRepository();

    protected abstract Class<T> getInstanceClass();

    /* Utility methods */
    protected void assertPushNotificationRecipientAndDeviceBelongToSameUser(final T recipient, final UserDevice userDevice) {
        final Long recipientUserId = recipient.getSubscription().getUser().getId();
        final Long deviceUserId = userDevice.getUser().getId();
        if (!recipientUserId.equals(deviceUserId)) {
            LOGGER.debug("Push notification recipient with id - {} belongs to user with id - {} where as user device with id - {} belongs to user with id - {}", recipient.getId(), recipientUserId, userDevice.getId(), deviceUserId);
            throw new PushNotificationRecipientInvalidDeviceUserException(recipient.getId(), recipientUserId, userDevice.getId(), deviceUserId);
        }
    }

    protected void assertPushNotificationNotNullForLookupParameters(final PushNotificationRecipient recipient, final Long subscriptionId, final PushNotificationProviderType type, final String destinationRouteToken) {
        if (recipient == null) {
            LOGGER.error("No push notification request is found for subscription with id - {}, type - {}, destination route token - {}", subscriptionId, type, destinationRouteToken);
            throw new PushNotificationRecipientNotFoundForLookupParametersException(subscriptionId, type, destinationRouteToken);
        }
    }

    protected void assertDestinationRouteTokenNotNull(final String destinationRouteToken) {
        Assert.notNull(destinationRouteToken, "Destination route token should not be null");
    }

    protected void assertPushNotificationRecipientProviderTypeNotNull(final PushNotificationProviderType providerType) {
        Assert.notNull(providerType, "Push notification provider type should not be null");
    }

    protected void assertPushNotificationRecipientNotNullForId(final T recipient, final Long id) {
        if (recipient == null) {
            LOGGER.error("Push notification recipient not found for id - {}, class - {}", id, getInstanceClass());
            throw new PushNotificationRecipientNotFoundForIdException(id, getInstanceClass());
        }
    }

    protected void assertPushNotificationRecipientIdNotNull(final Long recipientId) {
        Assert.notNull(recipientId, "Push notification recipient id should not be null");
    }

    protected void assertPushNotificationSubscriptionIdNotNull(final Long subscriptionId) {
        Assert.notNull(subscriptionId, "Push notification subscription ID should not be null");
    }

    protected void assertPushNotificationRecipientDto(final PushNotificationRecipientDto<? extends PushNotificationRecipient> recipientDto) {
        Assert.notNull(recipientDto, "Push notification recipient DTO should not be null");
        Assert.notNull(recipientDto.getDestinationRouteToken(), "Destination route token in push notification recipient DTO should not be null");
        Assert.notNull(recipientDto.getDeviceOperatingSystemType(), "Device operating system type in push notification recipient DTO should not be null");
        Assert.notNull(recipientDto.getApplicationType(), "Application type in push notification recipient DTO should not be null");
    }

    protected void assertNoRecipientExists(final PushNotificationProviderType type, final PushNotificationSubscription subscription, final String destinationRouteToken, final String applicationType) {
        final PushNotificationRecipient pushNotificationRecipient = pushNotificationRecipientRepository.findByTypeAndSubscriptionAndDestinationRouteTokenAndApplicationType(type, subscription, destinationRouteToken, applicationType);
        if (pushNotificationRecipient != null) {
            LOGGER.error("Push notification recipient already exist for provider type - {}, subscription - {}, destination route token - {}", type, subscription, destinationRouteToken);
            throw new PushNotificationRecipientAlreadyExistsException(pushNotificationRecipient.getId(), type, destinationRouteToken, subscription.getId(), applicationType);
        }
    }

    /* Properties getters and setters */
    public PushNotificationSubscriptionService getPushNotificationSubscriptionService() {
        return pushNotificationSubscriptionService;
    }

    public void setPushNotificationSubscriptionService(final PushNotificationSubscriptionService pushNotificationSubscriptionService) {
        this.pushNotificationSubscriptionService = pushNotificationSubscriptionService;
    }

    public PushNotificationRecipientRepository getPushNotificationRecipientRepository() {
        return pushNotificationRecipientRepository;
    }

    public void setPushNotificationRecipientRepository(final PushNotificationRecipientRepository pushNotificationRecipientRepository) {
        this.pushNotificationRecipientRepository = pushNotificationRecipientRepository;
    }

    public UserDeviceService getUserDeviceService() {
        return userDeviceService;
    }

    public void setUserDeviceService(final UserDeviceService userDeviceService) {
        this.userDeviceService = userDeviceService;
    }

    public PushNotificationRecipientDeviceRepository getPushNotificationRecipientDeviceRepository() {
        return pushNotificationRecipientDeviceRepository;
    }

    public void setPushNotificationRecipientDeviceRepository(final PushNotificationRecipientDeviceRepository pushNotificationRecipientDeviceRepository) {
        this.pushNotificationRecipientDeviceRepository = pushNotificationRecipientDeviceRepository;
    }
}
