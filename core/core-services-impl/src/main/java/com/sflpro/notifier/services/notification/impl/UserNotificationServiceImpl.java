package com.sflpro.notifier.services.notification.impl;

import com.sflpro.notifier.db.entities.notification.Notification;
import com.sflpro.notifier.db.entities.notification.UserNotification;
import com.sflpro.notifier.db.entities.user.User;
import com.sflpro.notifier.db.repositories.repositories.notification.UserNotificationRepository;
import com.sflpro.notifier.services.notification.NotificationService;
import com.sflpro.notifier.services.notification.UserNotificationService;
import com.sflpro.notifier.services.notification.dto.UserNotificationDto;
import com.sflpro.notifier.services.notification.exception.UserNotificationAlreadyExistsException;
import com.sflpro.notifier.services.notification.exception.UserNotificationNotFoundForIdException;
import com.sflpro.notifier.services.user.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import javax.annotation.Nonnull;
import java.util.List;

/**
 * User: Ruben Dilanyan
 * Company: SFL LLC
 * Date: 4/2/15
 * Time: 6:59 PM
 */
@Service
public class UserNotificationServiceImpl implements UserNotificationService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserNotificationServiceImpl.class);

    /* Dependencies */
    @Autowired
    private UserNotificationRepository userNotificationRepository;

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private UserService userService;

    /* Constructors */
    public UserNotificationServiceImpl() {
        LOGGER.debug("Initializing user notification service");
    }

    @Transactional
    @Nonnull
    @Override
    public UserNotification createUserNotification(@Nonnull final Long userId, @Nonnull final Long notificationId, @Nonnull final UserNotificationDto userNotificationDto) {
        Assert.notNull(userId, "User id should not be null");
        Assert.notNull(notificationId, "Notification id should not be null");
        Assert.notNull(userNotificationDto, "Notification DTO should not be null");
        LOGGER.debug("Creating user notification, user id - {}, notification id - {}, user notification DTO - {}", userId, notificationId, userNotificationDto);
        // Load notification
        final Notification notification = notificationService.getNotificationById(notificationId);
        assertNoUserNotificationExistsForNotification(notification);
        // Load user
        final User user = userService.getUserById(userId);
        // Create user notification
        UserNotification userNotification = new UserNotification();
        userNotificationDto.updateDomainEntityProperties(userNotification);
        userNotification.setUser(user);
        userNotification.setNotification(notification);
        // Persist notification
        userNotification = userNotificationRepository.save(userNotification);
        LOGGER.debug("Successfully created new user notification with id - {}, user notification - {}", userNotification.getId(), userNotification);
        return userNotification;
    }

    @Nonnull
    @Override
    public UserNotification getUserNotificationById(@Nonnull final Long userNotificationId) {
        assertUserNotificationIdNotNull(userNotificationId);
        LOGGER.debug("Getting user notification for id - {}", userNotificationId);
        final UserNotification userNotification = userNotificationRepository.findOne(userNotificationId);
        assertUserNotificationNotNull(userNotification, userNotificationId);
        LOGGER.debug("Successfully retrieved user notification for id - {}, user notification - {}", userNotification.getId(), userNotification);
        return userNotification;
    }

    @Nonnull
    @Override
    public List<UserNotification> getUserNotificationsByUserId(@Nonnull final Long userId) {
        Assert.notNull(userId, "User id should not be null");
        LOGGER.debug("Getting user notifications for user id - {}", userId);
        final User user = userService.getUserById(userId);
        final List<UserNotification> userNotifications = userNotificationRepository.findByUser(user);
        LOGGER.debug("Successfully retrieved user notifications for user - {}, notifications - {}", user, userNotifications);
        return userNotifications;
    }

    /* Utility methods */
    private void assertUserNotificationNotNull(final UserNotification userNotification, final Long id) {
        if (userNotification == null) {
            LOGGER.error("No user notification was found for id - {}", id);
            throw new UserNotificationNotFoundForIdException(id);
        }
    }

    private void assertUserNotificationIdNotNull(final Long userNotificationId) {
        Assert.notNull(userNotificationId, "User notification id should not be null");
    }

    private void assertNoUserNotificationExistsForNotification(final Notification notification) {
        final UserNotification userNotification = userNotificationRepository.findByNotification(notification);
        if (userNotification != null) {
            LOGGER.error("User notification already exists for notification with id - {}. User notification id - {}", notification.getId(), userNotification.getId());
            throw new UserNotificationAlreadyExistsException(userNotification.getId(), notification.getId());
        }
    }

    /* Properties getters and setters */
    public UserNotificationRepository getUserNotificationRepository() {
        return userNotificationRepository;
    }

    public void setUserNotificationRepository(final UserNotificationRepository userNotificationRepository) {
        this.userNotificationRepository = userNotificationRepository;
    }

    public NotificationService getNotificationService() {
        return notificationService;
    }

    public void setNotificationService(final NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    public UserService getUserService() {
        return userService;
    }

    public void setUserService(final UserService userService) {
        this.userService = userService;
    }
}
