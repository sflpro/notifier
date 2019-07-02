package com.sflpro.notifier.api.facade.security;


import io.vavr.control.Try;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.ResourceServerTokenServices;
import org.springframework.util.Assert;

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
    public boolean isPermitted(final String permission, final String accessToken) {
        Assert.hasText(permission, "Null or empty text was passed as an argument for parameter 'permission'.");
        Assert.hasText(accessToken, "Null or empty text was passed as an argument for parameter 'accessToken'.");
        return Try.ofSupplier(() ->
                resourceServerTokenServices.loadAuthentication(accessToken)
        )
                .filter(authentication -> isPermitted(authentication, permission))
                .isSuccess();

    }

    private static boolean isPermitted(final OAuth2Authentication authentication, final String permissionName) {
        return authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .anyMatch(permissionName::equals);
    }

}
