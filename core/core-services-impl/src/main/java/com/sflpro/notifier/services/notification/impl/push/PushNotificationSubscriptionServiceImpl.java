package com.sflpro.notifier.services.notification.impl.push;

import com.sflpro.notifier.db.entities.user.User;
import com.sflpro.notifier.persistence.repositories.notification.push.PushNotificationSubscriptionRepository;
import com.sflpro.notifier.services.notification.dto.push.PushNotificationSubscriptionDto;
import com.sflpro.notifier.services.notification.exception.push.PushNotificationSubscriptionAlreadyExistsForUserException;
import com.sflpro.notifier.services.notification.exception.push.PushNotificationSubscriptionNotFoundForIdException;
import com.sflpro.notifier.services.notification.exception.push.PushNotificationSubscriptionNotFoundForUserException;
import com.sflpro.notifier.db.entities.notification.push.PushNotificationSubscription;
import com.sflpro.notifier.services.notification.push.PushNotificationSubscriptionService;
import com.sflpro.notifier.services.user.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import javax.annotation.Nonnull;

/**
 * User: Ruben Dilanyan
 * Company: SFL LLC
 * Date: 8/13/15
 * Time: 10:18 AM
 */
@Service
public class PushNotificationSubscriptionServiceImpl implements PushNotificationSubscriptionService {

    private static final Logger LOGGER = LoggerFactory.getLogger(PushNotificationSubscriptionServiceImpl.class);

    /* Dependencies */
    @Autowired
    private PushNotificationSubscriptionRepository pushNotificationSubscriptionRepository;

    @Autowired
    private UserService userService;

    /* Constructors */
    public PushNotificationSubscriptionServiceImpl() {
        LOGGER.debug("Initializing push notification subscription service");
    }

    @Nonnull
    @Override
    public boolean checkIfPushNotificationSubscriptionExistsForUser(@Nonnull final Long userId) {
        assertUserIdNotNull(userId);
        LOGGER.debug("Checking if push notification subscription exists for user with id - {}", userId);
        final User user = userService.getUserById(userId);
        final PushNotificationSubscription subscription = pushNotificationSubscriptionRepository.findByUser(user);
        final boolean exists = (subscription != null);
        LOGGER.debug("Push notification subscription lookup result for user with id - {} is - {}", userId, exists);
        return exists;
    }

    @Nonnull
    @Override
    public PushNotificationSubscription getPushNotificationSubscriptionForUser(@Nonnull final Long userId) {
        assertUserIdNotNull(userId);
        LOGGER.debug("Getting push notification subscription for user with id - {}", userId);
        final User user = userService.getUserById(userId);
        final PushNotificationSubscription subscription = pushNotificationSubscriptionRepository.findByUser(user);
        assertPushNotificationSubscriptionNotNullForUser(subscription, user);
        LOGGER.debug("Successfully retrieved push notification subscription for user with id - {}, subscription - {}", user.getId(), subscription);
        return subscription;
    }

    @Transactional
    @Nonnull
    @Override
    public PushNotificationSubscription createPushNotificationSubscription(@Nonnull final Long userId, @Nonnull final PushNotificationSubscriptionDto subscriptionDto) {
        assertUserIdNotNull(userId);
        Assert.notNull(subscriptionDto, "Subscription DTO should not be null");
        // Load user
        final User user = userService.getUserById(userId);
        assertNoSubscriptionExistsForUser(user);
        // Create new subscription
        PushNotificationSubscription subscription = new PushNotificationSubscription();
        subscriptionDto.updateDomainEntityProperties(subscription);
        subscription.setUser(user);
        // Persist subscription
        subscription = pushNotificationSubscriptionRepository.save(subscription);
        LOGGER.debug("Successfully created push notification subscription with id - {}, subscription - {}", subscription.getId(), subscription);
        return subscription;
    }

    @Nonnull
    @Override
    public PushNotificationSubscription getPushNotificationSubscriptionById(@Nonnull final Long subscriptionId) {
        assertPushNotificationSubscriptionId(subscriptionId);
        LOGGER.debug("Getting push notification subscription for id - {}", subscriptionId);
        final PushNotificationSubscription subscription = pushNotificationSubscriptionRepository.findOne(subscriptionId);
        assertPushNotificationSubscriptionNotNullForId(subscription, subscriptionId);
        LOGGER.debug("Successfully retrieved push notification subscription for id - {}, subscription - {}", subscription.getId(), subscription);
        return subscription;
    }

    /* Utility methods */
    private void assertPushNotificationSubscriptionNotNullForUser(final PushNotificationSubscription subscription, final User user) {
        if (subscription == null) {
            LOGGER.error("No Push notification subscription found for user with id - {}", user);
            throw new PushNotificationSubscriptionNotFoundForUserException(user.getId());
        }
    }

    private void assertUserIdNotNull(final Long userId) {
        Assert.notNull(userId, "User id should not be null");
    }

    private void assertPushNotificationSubscriptionNotNullForId(final PushNotificationSubscription subscription, final Long id) {
        if (subscription == null) {
            LOGGER.error("No push notification subscription was found for id - {}", id);
            throw new PushNotificationSubscriptionNotFoundForIdException(id);
        }
    }

    private void assertPushNotificationSubscriptionId(@Nonnull final Long subscriptionId) {
        Assert.notNull(subscriptionId, "Subscription id should not be null");
    }

    private void assertNoSubscriptionExistsForUser(final User user) {
        final PushNotificationSubscription subscription = pushNotificationSubscriptionRepository.findByUser(user);
        if (subscription != null) {
            LOGGER.error("User with id - {} already has push notification subscription with id - {}", user.getId(), subscription.getId());
            throw new PushNotificationSubscriptionAlreadyExistsForUserException(user.getId(), subscription.getId());
        }
    }

    /* Properties getters and setters */
    public PushNotificationSubscriptionRepository getPushNotificationSubscriptionRepository() {
        return pushNotificationSubscriptionRepository;
    }

    public void setPushNotificationSubscriptionRepository(final PushNotificationSubscriptionRepository pushNotificationSubscriptionRepository) {
        this.pushNotificationSubscriptionRepository = pushNotificationSubscriptionRepository;
    }

    public UserService getUserService() {
        return userService;
    }

    public void setUserService(final UserService userService) {
        this.userService = userService;
    }
}
