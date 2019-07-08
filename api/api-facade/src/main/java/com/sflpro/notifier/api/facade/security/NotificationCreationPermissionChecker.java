package com.sflpro.notifier.api.facade.security;

import com.sflpro.notifier.services.notification.dto.NotificationDto;

import javax.annotation.Nonnull;

/**
 * Created by Hayk Mkrtchyan.
 * Date: 6/26/19
 * Time: 3:05 PM
 */
interface NotificationCreationPermissionChecker {

    <R extends NotificationDto<?>> boolean isNotificationCreationAllowed(@Nonnull final R creationRequest);
}
