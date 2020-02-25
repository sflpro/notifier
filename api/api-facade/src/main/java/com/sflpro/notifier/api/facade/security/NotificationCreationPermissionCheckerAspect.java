package com.sflpro.notifier.api.facade.security;

import com.sflpro.notifier.services.notification.dto.NotificationDto;
import com.sflpro.notifier.services.notification.dto.push.PushNotificationDto;
import com.sflpro.notifier.services.notification.dto.push.TemplatedPushNotificationDto;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Hayk Mkrtchyan.
 * Date: 6/26/19
 * Time: 3:25 PM
 */
@Aspect
class NotificationCreationPermissionCheckerAspect {

    private static final Logger logger = LoggerFactory.getLogger(NotificationCreationPermissionCheckerAspect.class);

    private final NotificationCreationPermissionChecker permissionChecker;


    NotificationCreationPermissionCheckerAspect(final NotificationCreationPermissionChecker permissionChecker) {
        this.permissionChecker = permissionChecker;
    }

    @Around("execution(* com.sflpro.notifier.services.notification..*.create* (..)) " +
            "&& args(notification,..)")
    public <N extends NotificationDto<?>> Object aroundNotificationCreation(final ProceedingJoinPoint point, final N notification) throws Throwable {
        logger.debug("Checking permission for sending {} notification with subject {}", notification.getType(), notification.getSubject());
        return executeAuthorized(point, notification);
    }

    @Around("execution(* com.sflpro.notifier.services.notification..*.createNotificationsForUserActiveRecipients* (..)) " +
            "&& args(userId,pushNotification,..)")
    public Object aroundPushNotificationCreation(final ProceedingJoinPoint point, final Long userId, final PushNotificationDto pushNotification) throws Throwable {
        logger.debug("Checking permission for sending push message with subject {} to user {}", pushNotification.getSubject(), userId);
        return executeAuthorized(point, pushNotification);
    }

    @Around("execution(* com.sflpro.notifier.services.notification..*.createNotificationsForUserActiveRecipients* (..)) " +
            "&& args(userId,pushNotification,..)")
    public Object aroundTemplatedPushNotificationCreation(final ProceedingJoinPoint point, final Long userId, final TemplatedPushNotificationDto pushNotification) throws Throwable {
        logger.debug("Checking permission for sending push message with subject {} to user {}", pushNotification.getSubject(), userId);
        return executeAuthorized(point, pushNotification);
    }

    private <R extends NotificationDto<?>> Object executeAuthorized(final ProceedingJoinPoint point, final R request) throws Throwable {
        if (!permissionChecker.isNotificationCreationAllowed(request)) {
            final String msg = "Creating notification is not permitted!";
            logger.warn(msg);
            throw new PermissionDeniedException(msg);
        }
        return point.proceed();
    }

}
