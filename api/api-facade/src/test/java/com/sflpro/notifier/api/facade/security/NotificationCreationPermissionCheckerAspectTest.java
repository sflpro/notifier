package com.sflpro.notifier.api.facade.security;

import com.sflpro.notifier.api.internal.facade.test.AbstractFacadeUnitTest;
import com.sflpro.notifier.db.entities.notification.email.EmailNotification;
import com.sflpro.notifier.services.notification.dto.NotificationDto;
import org.aspectj.lang.ProceedingJoinPoint;
import org.easymock.Mock;
import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.easymock.EasyMock.expect;

/**
 * Created by Hayk Mkrtchyan.
 * Date: 7/11/19
 * Time: 12:47 PM
 */
public class NotificationCreationPermissionCheckerAspectTest extends AbstractFacadeUnitTest {

    private NotificationCreationPermissionCheckerAspect notificationCreationPermissionCheckerAspect;

    @Mock
    private NotificationCreationPermissionChecker permissionChecker;

    @Mock
    private ProceedingJoinPoint proceedingJoinPoint;

    @Before
    public void prepare() {
        notificationCreationPermissionCheckerAspect = new NotificationCreationPermissionCheckerAspect(permissionChecker);
    }

    @Test
    public void testAroundNotificationCreationNotAllowed() throws Throwable {
        final NotificationDto notification = getServiceFacadeImplTestHelper().createEmailNotificationDto();
        expect(permissionChecker.isNotificationCreationAllowed(notification)).andReturn(false);
        replayAll();
        assertThatThrownBy(() -> notificationCreationPermissionCheckerAspect.aroundNotificationCreation(proceedingJoinPoint, notification))
                .isInstanceOf(PermissionDeniedException.class);
        verifyAll();
    }

    @Test
    public void testAroundNotificationCreationAllowed() throws Throwable {
        final NotificationDto notification = getServiceFacadeImplTestHelper().createEmailNotificationDto();
        final EmailNotification result = new EmailNotification();
        expect(permissionChecker.isNotificationCreationAllowed(notification)).andReturn(true);
        expect(proceedingJoinPoint.proceed()).andReturn(result);
        replayAll();
        assertThat(notificationCreationPermissionCheckerAspect.aroundNotificationCreation(proceedingJoinPoint, notification))
                .isEqualTo(result);
        verifyAll();
    }
}
