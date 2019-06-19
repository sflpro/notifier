package com.sflpro.notifier.services.notification.impl.component;

import com.sflpro.notifier.db.entities.notification.Notification;
import com.sflpro.notifier.db.entities.notification.UserNotification;
import com.sflpro.notifier.db.entities.user.User;
import com.sflpro.notifier.services.notification.UserNotificationService;
import com.sflpro.notifier.services.notification.component.UserNotificationComponent;
import com.sflpro.notifier.services.notification.dto.UserNotificationDto;
import com.sflpro.notifier.services.user.UserService;
import io.jsonwebtoken.lang.Assert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * User: Syuzanna Eprikyan
 * Company: SFL LLC
 * Date: 6/19/19
 * Time: 11:12 AM
 */

@Component
public class UserNotificationComponentImpl implements UserNotificationComponent {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserNotificationComponentImpl.class);

    /* Dependencies */
    @Autowired
    private UserService userService;

    @Autowired
    private UserNotificationService userNotificationService;

    /* Constructors */
    public UserNotificationComponentImpl() {
        LOGGER.debug("Initializing user notification component");
    }

    /* Public methods */
    @Override
    public void associateUserWithNotification(final String userUuid, final Notification notification) {
        Assert.notNull(notification, "Notification should not be null.");
        if (userUuid != null) {
            final User user = userService.getOrCreateUserForUuId(userUuid);
            final UserNotification userNotification = userNotificationService.createUserNotification(user.getId(), notification.getId(), new UserNotificationDto());
            LOGGER.debug("Created user notification - {} for user - {} and notification - {}", userNotification, user, notification);
        }
    }
}
