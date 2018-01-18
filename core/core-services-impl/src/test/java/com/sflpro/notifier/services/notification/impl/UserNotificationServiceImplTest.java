package com.sflpro.notifier.services.notification.impl;

import com.sflpro.notifier.db.entities.notification.Notification;
import com.sflpro.notifier.db.entities.notification.UserNotification;
import com.sflpro.notifier.db.entities.user.User;
import com.sflpro.notifier.persistence.repositories.notification.UserNotificationRepository;
import com.sflpro.notifier.services.notification.NotificationService;
import com.sflpro.notifier.services.notification.dto.UserNotificationDto;
import com.sflpro.notifier.services.notification.exception.UserNotificationAlreadyExistsException;
import com.sflpro.notifier.services.notification.exception.UserNotificationNotFoundForIdException;
import com.sflpro.notifier.services.test.AbstractServicesUnitTest;
import com.sflpro.notifier.services.user.UserService;
import org.easymock.Mock;
import org.easymock.TestSubject;
import org.junit.Assert;
import org.junit.Test;

import static org.easymock.EasyMock.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * User: Ruben Dilanyan
 * Company: SFL LLC
 * Date: 4/2/15
 * Time: 7:05 PM
 */
public class UserNotificationServiceImplTest extends AbstractServicesUnitTest {

    /* Test subject and mocks */
    @TestSubject
    private UserNotificationServiceImpl userNotificationService = new UserNotificationServiceImpl();

    @Mock
    private UserNotificationRepository userNotificationRepository;

    @Mock
    private NotificationService notificationService;

    @Mock
    private UserService userService;

    /* Constructors */
    public UserNotificationServiceImplTest() {
    }

    /* Test methods */
    @Test
    public void testGetUserNotificationByIdWithInvalidArguments() {
        // Reset
        resetAll();
        // Replay
        replayAll();
        // Run test scenario
        try {
            userNotificationService.getUserNotificationById(null);
            fail("Exception should be thrown");
        } catch (final IllegalArgumentException ex) {
            // Expected
        }
        // Verify
        verifyAll();
    }

    @Test
    public void testGetUserNotificationByIdWithNotExistingNotificationId() {
        // Test data
        final Long userNotificationId = 1L;
        // Reset
        resetAll();
        // Expectations
        expect(userNotificationRepository.findOne(eq(userNotificationId))).andReturn(null).once();
        // Replay
        replayAll();
        // Run test scenario
        try {
            userNotificationService.getUserNotificationById(userNotificationId);
            fail("Exception should be thrown");
        } catch (final UserNotificationNotFoundForIdException ex) {
            // Expected
            assertUserNotificationNotFoundForIdException(ex, userNotificationId);
        }
        // Verify
        verifyAll();
    }

    @Test
    public void testGetUserNotificationById() {
        // Test data
        final Long userNotificationId = 1L;
        final UserNotification userNotification = getServicesImplTestHelper().createUserNotification();
        userNotification.setId(userNotificationId);
        // Reset
        resetAll();
        // Expectations
        expect(userNotificationRepository.findOne(eq(userNotificationId))).andReturn(userNotification).once();
        // Replay
        replayAll();
        // Run test scenario
        final UserNotification result = userNotificationService.getUserNotificationById(userNotificationId);
        assertEquals(userNotification, result);
        // Verify
        verifyAll();
    }

    @Test
    public void testCreateUserNotificationWithInvalidArguments() {
        // Test data
        final Long userId = 1L;
        final Long notificationId = 2L;
        final UserNotificationDto userNotificationDto = getServicesImplTestHelper().createUserNotificationDto();
        // Reset
        resetAll();
        // Replay
        replayAll();
        // Run test scenario
        try {
            userNotificationService.createUserNotification(null, notificationId, userNotificationDto);
            fail("Exception should be thrown");
        } catch (final IllegalArgumentException ex) {
            // Expected
        }
        try {
            userNotificationService.createUserNotification(userId, null, userNotificationDto);
            fail("Exception should be thrown");
        } catch (final IllegalArgumentException ex) {
            // Expected
        }
        try {
            userNotificationService.createUserNotification(userId, notificationId, null);
            fail("Exception should be thrown");
        } catch (final IllegalArgumentException ex) {
            // Expected
        }
        // Verify
        verifyAll();
    }

    @Test
    public void testCreateUserNotificationWhenItAlreadyExistsForNotification() {
        // Test data
        final Long userId = 1L;
        final Long notificationId = 2L;
        final Notification notification = getServicesImplTestHelper().createEmailNotification();
        notification.setId(notificationId);
        final Long existingUserNotificationId = 3L;
        final UserNotification existingUserNotification = getServicesImplTestHelper().createUserNotification();
        existingUserNotification.setId(existingUserNotificationId);
        final UserNotificationDto userNotificationDto = getServicesImplTestHelper().createUserNotificationDto();
        // Reset
        resetAll();
        // Expectations
        expect(notificationService.getNotificationById(eq(notificationId))).andReturn(notification).once();
        expect(userNotificationRepository.findByNotification(eq(notification))).andReturn(existingUserNotification).once();
        // Replay
        replayAll();
        // Run test scenario
        try {
            userNotificationService.createUserNotification(userId, notificationId, userNotificationDto);
            fail("Exception should be thrown");
        } catch (final UserNotificationAlreadyExistsException ex) {
            // Expected
            assertUserNotificationAlreadyExistsException(ex, existingUserNotificationId, notificationId);
        }
        // Verify
        verifyAll();
    }

    @Test
    public void testCreateUserNotification() {
        // Test data
        final Long userId = 1L;
        final User user = getServicesImplTestHelper().createUser();
        user.setId(userId);
        final Long notificationId = 2L;
        final Notification notification = getServicesImplTestHelper().createEmailNotification();
        notification.setId(notificationId);
        final UserNotificationDto userNotificationDto = getServicesImplTestHelper().createUserNotificationDto();
        // Reset
        resetAll();
        // Expectations
        expect(notificationService.getNotificationById(eq(notificationId))).andReturn(notification).once();
        expect(userNotificationRepository.findByNotification(eq(notification))).andReturn(null).once();
        expect(userService.getUserById(eq(userId))).andReturn(user).once();
        expect(userNotificationRepository.save(isA(UserNotification.class))).andAnswer(() -> (UserNotification) getCurrentArguments()[0]).once();
        // Replay
        replayAll();
        // Run test scenario
        final UserNotification userNotification = userNotificationService.createUserNotification(userId, notificationId, userNotificationDto);
        getServicesImplTestHelper().assertUserNotification(userNotification, userNotificationDto);
        Assert.assertEquals(user, userNotification.getUser());
        assertEquals(notification, userNotification.getNotification());
        // Verify
        verifyAll();
    }

    /* Utility methods */
    private void assertUserNotificationAlreadyExistsException(final UserNotificationAlreadyExistsException ex, final Long existingUserNotificationId, final Long notificationId) {
        assertEquals(existingUserNotificationId, ex.getUserNotificationId());
        assertEquals(notificationId, ex.getNotificationId());
    }

    private void assertUserNotificationNotFoundForIdException(final UserNotificationNotFoundForIdException ex, final Long id) {
        Assert.assertEquals(id, ex.getId());
        Assert.assertEquals(UserNotification.class, ex.getEntityClass());
    }

}
