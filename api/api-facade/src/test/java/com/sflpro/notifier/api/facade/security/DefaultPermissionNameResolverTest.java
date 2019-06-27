package com.sflpro.notifier.api.facade.security;

import com.sflpro.notifier.api.internal.facade.test.AbstractFacadeUnitTest;
import com.sflpro.notifier.services.notification.dto.email.EmailNotificationDto;
import org.easymock.Mock;
import org.junit.Before;
import org.junit.Test;

import java.util.Properties;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.easymock.EasyMock.expect;

/**
 * Created by Hayk Mkrtchyan.
 * Date: 6/28/19
 * Time: 11:47 AM
 */
public class DefaultPermissionNameResolverTest extends AbstractFacadeUnitTest {

    private PermissionNameResolver permissionNameResolver;

    @Mock
    private Properties permissionMappings;

    @Before
    public void prepare() {
        permissionNameResolver = new DefaultPermissionNameResolver(permissionMappings);
    }

    @Test
    public void resolveWithIllegalArguments() {
        assertThatThrownBy(() -> permissionNameResolver.resolve(null))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    public void resolveForTemplatedNotificationCreation() {
        final EmailNotificationDto notificationDto = new EmailNotificationDto();
        notificationDto.setTemplateName(uuid());
        final String permissionName = uuid();
        expect(permissionMappings.getProperty(notificationDto.getTemplateName())).andReturn(permissionName);
        replayAll();
        assertThat(permissionNameResolver.resolve(notificationDto).orElseThrow(() -> new AssertionError("PermissionName should be present!")))
                .isEqualTo(permissionName);
        verifyAll();
    }

    @Test
    public void resolveForNonTemplatedNotificationCreation() {
        final EmailNotificationDto notificationDto = new EmailNotificationDto();
        final String permissionName = uuid();
        expect(permissionMappings.getProperty("nontemplated")).andReturn(permissionName);
        replayAll();
        assertThat(permissionNameResolver.resolve(notificationDto).orElseThrow(() -> new AssertionError("PermissionName should be present!")))
                .isEqualTo(permissionName);
        verifyAll();
    }

    @Test
    public void resolveMissingPermissionMapping() {
        final EmailNotificationDto notificationDto = new EmailNotificationDto();
        notificationDto.setTemplateName(uuid());
        expect(permissionMappings.getProperty(notificationDto.getTemplateName())).andReturn(null);
        replayAll();
        assertThat(permissionNameResolver.resolve(notificationDto)).isEmpty();
        verifyAll();
    }
}
