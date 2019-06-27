package com.sflpro.notifier.api.facade.security;

import com.sflpro.notifier.services.notification.dto.NotificationDto;

/**
 * Created by Hayk Mkrtchyan.
 * Date: 6/26/19
 * Time: 3:05 PM
 */
interface NotificationCreationPermissionChecker {

    <R extends NotificationDto<?>> boolean isNotificationCreationAllowed(final R creationRequest);
}
