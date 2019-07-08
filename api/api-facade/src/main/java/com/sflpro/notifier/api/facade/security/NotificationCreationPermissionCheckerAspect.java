package com.sflpro.notifier.api.facade.security;

import com.sflpro.notifier.services.notification.dto.NotificationDto;
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
            "&& args(request,..)")
    public <R extends NotificationDto<?>> Object around(final ProceedingJoinPoint point, final R request) throws Throwable {
        if (!permissionChecker.isNotificationCreationAllowed(request)) {
            final String msg = "Creating notification is not permitted!";
            logger.warn(msg);
            throw new PermissionDeniedException(msg);
        }
        return point.proceed();
    }

}
