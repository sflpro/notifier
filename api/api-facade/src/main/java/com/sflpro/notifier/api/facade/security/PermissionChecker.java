package com.sflpro.notifier.api.facade.security;

import com.sflpro.notifier.api.model.notification.request.AbstractCreateNotificationRequest;

/**
 * Created by Hayk Mkrtchyan.
 * Date: 6/26/19
 * Time: 2:25 PM
 */
interface PermissionChecker {

    <R extends AbstractCreateNotificationRequest> boolean isPermitted(final String permission, final String accessToken);
}
