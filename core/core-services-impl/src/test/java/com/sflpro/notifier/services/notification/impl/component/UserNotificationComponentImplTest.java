package com.sflpro.notifier.services.notification.impl.component;

import com.sflpro.notifier.db.entities.notification.Notification;
import com.sflpro.notifier.db.entities.notification.UserNotification;
import com.sflpro.notifier.db.entities.user.User;
import com.sflpro.notifier.services.notification.UserNotificationService;
import com.sflpro.notifier.services.notification.component.UserNotificationComponent;
import com.sflpro.notifier.services.notification.dto.UserNotificationDto;
import com.sflpro.notifier.services.test.AbstractServicesUnitTest;
import com.sflpro.notifier.services.user.UserService;
import org.easymock.Mock;
import org.easymock.TestSubject;
import org.junit.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.easymock.EasyMock.expect;

/**
 * User: Syuzanna Eprikyan
 * Company: SFL LLC
 * Date: 6/19/19
 * Time: 11:31 AM
 */
public class UserNotificationComponentImplTest extends AbstractServicesUnitTest {

    @TestSubject
    private UserNotificationComponent userNotificationComponent = new UserNotificationComponentImpl();

    @Mock
    private UserService userService;

    @Mock
    private UserNotificationService userNotificationService;

    @Test
    public void testAssociateUserWithNotification_WithInvalidArguments() {
        resetAll();
        // test data
        // expectations
        replayAll();
        assertThatThrownBy(() -> userNotificationComponent.associateUserWithNotification(UUID.randomUUID().toString(), null)).isInstanceOf(IllegalArgumentException.class);
        verifyAll();
    }

    @Test
    public void testAssociateUserWithNotification_WhenUserUuidIsNull() {
        resetAll();
        // test data
        final String userUuid = null;
        final Notification notification = getServicesImplTestHelper().createEmailNotification();
        // expectations
        replayAll();
        userNotificationComponent.associateUserWithNotification(userUuid, notification);
        verifyAll();
    }

    @Test
    public void testAssociateUserWithNotification() {
        resetAll();
        // test data
        final String userUuid = UUID.randomUUID().toString();
        final Notification notification = getServicesImplTestHelper().createEmailNotification();
        final User user = getServicesImplTestHelper().createUser();
        final UserNotification userNotification = getServicesImplTestHelper().createUserNotification();
        // expectations
        expect(userService.getOrCreateUserForUuId(userUuid)).andReturn(user);
        expect(userNotificationService.createUserNotification(user.getId(), notification.getId(), new UserNotificationDto())).andReturn(userNotification);
        replayAll();
        userNotificationComponent.associateUserWithNotification(userUuid, notification);
        verifyAll();
    }
}