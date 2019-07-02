package com.sflpro.notifier.api.facade.security;

import com.sflpro.notifier.services.notification.dto.NotificationDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.util.Assert;

import java.util.Optional;

/**
 * Created by Hayk Mkrtchyan.
 * Date: 6/26/19
 * Time: 3:07 PM
 */
class DefaultNotificationCreationPermissionChecker implements NotificationCreationPermissionChecker {

    private static final Logger logger = LoggerFactory.getLogger(DefaultNotificationCreationPermissionChecker.class);

    private final PermissionChecker permissionChecker;
    private final PermissionNameResolver permissionNameResolver;

    DefaultNotificationCreationPermissionChecker(final PermissionChecker permissionChecker,
                                                 final PermissionNameResolver permissionNameResolver) {
        this.permissionChecker = permissionChecker;
        this.permissionNameResolver = permissionNameResolver;
    }

    @Override
    public <R extends NotificationDto<?>> boolean isNotificationCreationAllowed(final R creationRequest) {
        Assert.notNull(creationRequest, "Null was passed as an argument for parameter 'creationRequest'.");
        final SecurityContext context = SecurityContextHolder.getContext();
        if (context == null) {
            return true;
        }
        final Authentication authentication = context.getAuthentication();
        if (authentication == null) {
            logger.warn("No access token was found associated with notification creation request.");
            return true;
        }
        if (authentication instanceof PreAuthenticatedAuthenticationToken) {
            return isPermitted(creationRequest, authentication.getPrincipal().toString());
        }
        return true;
    }

    private <R extends NotificationDto<?>> boolean isPermitted(final R creationRequest, final String token) {
        final Optional<String> permissionName = permissionNameResolver.resolve(creationRequest);
        if (!permissionName.isPresent()) {
            logger.debug("No permission name was resolved, request not permitted.");
            return false;
        }
        return permissionName.filter(permission -> permissionChecker.isPermitted(permission, token))
                .isPresent();
    }

}
