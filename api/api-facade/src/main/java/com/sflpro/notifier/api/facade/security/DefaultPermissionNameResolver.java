package com.sflpro.notifier.api.facade.security;

import com.sflpro.notifier.services.notification.dto.NotificationDto;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import java.util.Optional;
import java.util.Properties;

/**
 * Created by Hayk Mkrtchyan.
 * Date: 6/28/19
 * Time: 11:15 AM
 */
class DefaultPermissionNameResolver implements PermissionNameResolver {

    private static final Logger logger = LoggerFactory.getLogger(DefaultPermissionNameResolver.class);

    private static final String NON_TEMPLATED_NOTIFICATION_CREATION_PERMISSION_KEY = "nontemplated";

    private final Properties permissionMappings;

    DefaultPermissionNameResolver(final Properties permissionMappings) {
        this.permissionMappings = permissionMappings;
    }

    @Override
    public <R extends NotificationDto<?>> Optional<String> resolve(final R creationRequest) {
        Assert.notNull(creationRequest, "Null was passed as an argument for parameter 'creationRequest'.");
        final String permissionNameKey = permissionNameKey(creationRequest);
        final String permissionName = permissionMappings.getProperty(permissionNameKey(creationRequest));
        if (StringUtils.isBlank(permissionName)) {
            logger.warn("No permission was configured for {}", permissionNameKey);
            return Optional.empty();
        }
        return Optional.of(permissionName);
    }

    private static <R extends NotificationDto<?>> String permissionNameKey(final R creationRequest) {
        if (StringUtils.isBlank(creationRequest.getTemplateName())) {
            return NON_TEMPLATED_NOTIFICATION_CREATION_PERMISSION_KEY;
        }
        return creationRequest.getTemplateName();
    }
}
