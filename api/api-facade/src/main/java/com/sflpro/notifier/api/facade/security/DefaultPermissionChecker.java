package com.sflpro.notifier.api.facade.security;


import com.sflpro.notifier.api.model.notification.request.AbstractCreateNotificationRequest;
import io.vavr.control.Try;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.ResourceServerTokenServices;

/**
 * Created by Hayk Mkrtchyan.
 * Date: 6/26/19
 * Time: 2:31 PM
 */
class DefaultPermissionChecker implements PermissionChecker {

    private final ResourceServerTokenServices resourceServerTokenServices;

    DefaultPermissionChecker(final ResourceServerTokenServices resourceServerTokenServices) {
        this.resourceServerTokenServices = resourceServerTokenServices;
    }

    @Override
    public <R extends AbstractCreateNotificationRequest> boolean isPermitted(final String permission, final String accessToken) {
        return Try.ofSupplier(() ->
                resourceServerTokenServices.loadAuthentication(accessToken)
        )
                .filter(authentication -> isPermitted(authentication, permission))
                .isSuccess();

    }

    private static boolean isPermitted(final OAuth2Authentication authentication, final String permissionName) {
        return authentication.isAuthenticated() && authentication.getUserAuthentication().getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .anyMatch(permissionName::equals);
    }


}
