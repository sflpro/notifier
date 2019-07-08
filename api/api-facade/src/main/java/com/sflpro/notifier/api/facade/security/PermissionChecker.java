package com.sflpro.notifier.api.facade.security;

/**
 * Created by Hayk Mkrtchyan.
 * Date: 6/26/19
 * Time: 2:25 PM
 */
interface PermissionChecker {

    boolean isPermitted(final String permission, final String accessToken);
}
