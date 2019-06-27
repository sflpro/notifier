package com.sflpro.notifier.api.facade.security;

import com.sflpro.notifier.api.internal.facade.test.AbstractFacadeUnitTest;
import org.easymock.Mock;
import org.junit.Before;
import org.junit.Test;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.ResourceServerTokenServices;

import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.easymock.EasyMock.expect;

/**
 * Created by Hayk Mkrtchyan.
 * Date: 6/28/19
 * Time: 11:28 AM
 */
public class DefaultPermissionCheckerTest extends AbstractFacadeUnitTest {

    private PermissionChecker permissionChecker;

    @Mock
    private ResourceServerTokenServices resourceServerTokenServices;

    @Mock
    private OAuth2Authentication oAuth2Authentication;

    @Before
    public void prepare() {
        permissionChecker = new DefaultPermissionChecker(
                resourceServerTokenServices
        );
    }

    @Test
    public void isPermittedWithInvalidArguments() {
        assertThatThrownBy(() -> permissionChecker.isPermitted(null, uuid())).isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> permissionChecker.isPermitted("", uuid())).isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> permissionChecker.isPermitted(uuid(), null)).isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> permissionChecker.isPermitted(uuid(), "")).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    public void isPermittedTrue() {
        final String permission = uuid();
        final String token = uuid();
        expect(oAuth2Authentication.getAuthorities()).andReturn(Collections.singleton(new SimpleGrantedAuthority(permission)));
        expect(resourceServerTokenServices.loadAuthentication(token)).andReturn(oAuth2Authentication);
        replayAll();
        assertThat(permissionChecker.isPermitted(permission, token)).isTrue();
        verifyAll();
    }

    @Test
    public void isPermittedFalse() {
        final String permission = uuid();
        final String token = uuid();
        expect(oAuth2Authentication.getAuthorities()).andReturn(Collections.singleton(new SimpleGrantedAuthority(uuid())));
        expect(resourceServerTokenServices.loadAuthentication(token)).andReturn(oAuth2Authentication);
        replayAll();
        assertThat(permissionChecker.isPermitted(permission, token)).isFalse();
        verifyAll();
    }
}
