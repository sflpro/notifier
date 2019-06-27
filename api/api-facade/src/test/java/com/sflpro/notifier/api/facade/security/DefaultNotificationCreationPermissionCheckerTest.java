package com.sflpro.notifier.api.facade.security;

import com.sflpro.notifier.api.internal.facade.test.AbstractFacadeUnitTest;
import com.sflpro.notifier.services.notification.dto.email.EmailNotificationDto;
import org.easymock.Mock;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;

import java.util.Collections;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.easymock.EasyMock.expect;

/**
 * Created by Hayk Mkrtchyan.
 * Date: 6/28/19
 * Time: 10:43 AM
 */
public class DefaultNotificationCreationPermissionCheckerTest extends AbstractFacadeUnitTest {

    private NotificationCreationPermissionChecker notificationCreationPermissionChecker;

    @Mock
    private PermissionChecker permissionChecker;

    @Mock
    private PermissionNameResolver permissionNameResolver;

    @Before
    public void prepare() {
        notificationCreationPermissionChecker = new DefaultNotificationCreationPermissionChecker(
                permissionChecker,
                permissionNameResolver
        );
    }

    @After
    public void resetSecurityContex() {
        SecurityContextHolder.clearContext();
    }

    @Test
    public void isNotificationCreationAllowedInvalidArguments() {
        assertThatThrownBy(() -> notificationCreationPermissionChecker.isNotificationCreationAllowed(null))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    public void isNotificationCreationAllowedMissingSecurityContext() {
        final EmailNotificationDto notificationDto = new EmailNotificationDto();
        notificationDto.setTemplateName(uuid());
        assertThat(notificationCreationPermissionChecker.isNotificationCreationAllowed(notificationDto))
                .isTrue();
    }

    @Test
    public void isNotificationCreationAllowedNonPreAuthenticatedAuthenticationTokenSecurityContext() {
        final EmailNotificationDto notificationDto = new EmailNotificationDto();
        notificationDto.setTemplateName(uuid());
        final SecurityContext context = new SecurityContextImpl();
        context.setAuthentication(new AnonymousAuthenticationToken(uuid(), uuid(), Collections.singleton(new SimpleGrantedAuthority("USER"))));
        SecurityContextHolder.setContext(context);
        replayAll();
        assertThat(notificationCreationPermissionChecker.isNotificationCreationAllowed(notificationDto))
                .isTrue();
        verifyAll();
    }

    @Test
    public void isNotificationCreationAllowedMissingPermissionMapping() {
        final EmailNotificationDto notificationDto = new EmailNotificationDto();
        notificationDto.setTemplateName(uuid());
        final SecurityContext context = new SecurityContextImpl();
        context.setAuthentication(new PreAuthenticatedAuthenticationToken(uuid(), uuid()));
        SecurityContextHolder.setContext(context);
        expect(permissionNameResolver.resolve(notificationDto)).andReturn(Optional.empty());
        replayAll();
        assertThat(notificationCreationPermissionChecker.isNotificationCreationAllowed(notificationDto))
                .isFalse();
        verifyAll();
    }

    @Test
    public void isNotificationCreationAllowedPermittedCase() {
        final EmailNotificationDto notificationDto = new EmailNotificationDto();
        final String permissionName = uuid();
        final String token = uuid();
        final SecurityContext context = new SecurityContextImpl();
        context.setAuthentication(new PreAuthenticatedAuthenticationToken(token, uuid()));
        SecurityContextHolder.setContext(context);
        expect(permissionNameResolver.resolve(notificationDto)).andReturn(Optional.of(permissionName));
        expect(permissionChecker.isPermitted(permissionName, token)).andReturn(true);
        replayAll();
        assertThat(notificationCreationPermissionChecker.isNotificationCreationAllowed(notificationDto))
                .isTrue();
        verifyAll();
    }

}
