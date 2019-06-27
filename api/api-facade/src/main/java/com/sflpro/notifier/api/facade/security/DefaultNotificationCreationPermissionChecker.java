package com.sflpro.notifier.api.facade.security;

import com.sflpro.notifier.services.notification.dto.NotificationDto;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;

import java.util.Properties;

/**
 * Created by Hayk Mkrtchyan.
 * Date: 6/26/19
 * Time: 3:07 PM
 */
class DefaultNotificationCreationPermissionChecker implements NotificationCreationPermissionChecker {

    private final static Logger logger = LoggerFactory.getLogger(DefaultNotificationCreationPermissionChecker.class);

    private final PermissionChecker permissionChecker;
    private final String nonTemplatedNotificationCreationPermission;
    private final Properties permissionMappings;

    DefaultNotificationCreationPermissionChecker(final PermissionChecker permissionChecker,
                                                 final String nonTemplatedNotificationCreationPermission, final Properties permissionMappings) {
        this.permissionChecker = permissionChecker;
        this.nonTemplatedNotificationCreationPermission = nonTemplatedNotificationCreationPermission;
        this.permissionMappings = permissionMappings;
    }

    @Override
    public <R extends NotificationDto<?>> boolean isNotificationCreationAllowed(final R creationRequest) {
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
            final String permissionName = permissionNameFor(creationRequest);
            return permissionChecker.isPermitted(permissionName, authentication.getPrincipal().toString());
        }
        return true;
    }

    private <R extends NotificationDto<?>> String permissionNameFor(final R creationRequest) {
        if (StringUtils.isBlank(creationRequest.getTemplateName())) {
            return nonTemplatedNotificationCreationPermission;
        }
        return permissionMappings.getProperty(creationRequest.getTemplateName());
    }
}
