package com.sflpro.notifier.api.facade.services;

import com.sflpro.notifier.api.model.notification.NotificationClientType;
import com.sflpro.notifier.api.model.notification.NotificationModel;
import com.sflpro.notifier.api.model.notification.NotificationStateClientType;
import com.sflpro.notifier.db.entities.notification.Notification;
import com.sflpro.notifier.db.entities.notification.UserNotification;
import com.sflpro.notifier.db.entities.user.User;
import com.sflpro.notifier.services.notification.UserNotificationService;
import com.sflpro.notifier.services.notification.dto.UserNotificationDto;
import com.sflpro.notifier.services.user.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * User: Ruben Dilanyan
 * Company: SFL LLC
 * Date: 1/12/16
 * Time: 8:13 PM
 */
public abstract class AbstractNotificationServiceFacadeImpl {

    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractNotificationServiceFacadeImpl.class);

    /* Dependencies */
    @Autowired
    private UserService userService;

    @Autowired
    private UserNotificationService userNotificationService;

    /* Constructors */
    public AbstractNotificationServiceFacadeImpl() {
    }

    /* Utility methods */
    private void associateUserWithNotification(final User user, final Notification notification) {
        final UserNotification userNotification = userNotificationService.createUserNotification(user.getId(), notification.getId(), new UserNotificationDto());
        LOGGER.debug("Created user notification - {} for user - {} and notification - {}", userNotification, user, notification);
    }

    protected void associateUserWithNotificationIfRequired(final String userUuId, final Notification notification) {
        if (userUuId != null) {
            final User user = userService.getOrCreateUserForUuId(userUuId);
            associateUserWithNotification(user, notification);
        }
    }

    protected static void setNotificationCommonProperties(final NotificationModel notificationModel, final Notification notification) {
        notificationModel.setUuId(notification.getUuId());
        notificationModel.setBody(notification.getContent());
        notificationModel.setSubject(notification.getSubject());
        notificationModel.setType(NotificationClientType.valueOf(notification.getType().name()));
        notificationModel.setState(NotificationStateClientType.valueOf(notification.getState().name()));
    }

    /* Properties getters and setters */
    public UserService getUserService() {
        return userService;
    }

    public void setUserService(final UserService userService) {
        this.userService = userService;
    }

    public UserNotificationService getUserNotificationService() {
        return userNotificationService;
    }

    public void setUserNotificationService(final UserNotificationService userNotificationService) {
        this.userNotificationService = userNotificationService;
    }
}
