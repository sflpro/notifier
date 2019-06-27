package com.sflpro.notifier.api.facade.security;

import com.sflpro.notifier.services.notification.dto.NotificationDto;

import java.util.Optional;

/**
 * Created by Hayk Mkrtchyan.
 * Date: 6/28/19
 * Time: 11:14 AM
 */
interface PermissionNameResolver {

    <R extends NotificationDto<?>> Optional<String> resolve(final R creationRequest);
}
