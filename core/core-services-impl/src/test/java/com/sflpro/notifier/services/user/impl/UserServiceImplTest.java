package com.sflpro.notifier.services.user.impl;

import com.sflpro.notifier.db.entities.user.User;
import com.sflpro.notifier.persistence.repositories.user.UserRepository;
import com.sflpro.notifier.services.test.AbstractServicesUnitTest;
import com.sflpro.notifier.services.user.dto.UserDto;
import com.sflpro.notifier.services.user.exception.UserAlreadyExistsForUuIdException;
import com.sflpro.notifier.services.user.exception.UserNotFoundForIdException;
import com.sflpro.notifier.services.user.exception.UserNotFoundForUuidException;
import org.easymock.Mock;
import org.easymock.TestSubject;
import org.junit.Test;

import static org.easymock.EasyMock.*;
import static org.junit.Assert.*;

/**
 * User: Ruben Dilanyan
 * Company: SFL LLC
 * Date: 10/01/14
 * Time: 10:18 PM
 */
public class UserServiceImplTest extends AbstractServicesUnitTest {

    /* Test target and dependencies */
    @TestSubject
    private UserServiceImpl userService = new UserServiceImpl();

    @Mock
    private UserRepository userRepository;


    /* Constructors */
    public UserServiceImplTest() {

    }

    /* Test methods */
    @Test
    public void testGetOrCreateUserForUuIdWithInvalidArguments() {
        // Reset
        resetAll();
        // Replay
        replayAll();
        // Run test scenario
        try {
            userService.getOrCreateUserForUuId(null);
            fail("Exception should be thrown");
        } catch (final IllegalArgumentException ex) {
            // Expected
        }
        // Verify
        verifyAll();
    }

    @Test
    public void testGetOrCreateUserForUuIdWhenUserExists() {
        // Test data
        final Long userId = 1L;
        final User user = getServicesImplTestHelper().createUser();
        user.setId(userId);
        final String userUuId = user.getUuId();
        // Reset
        resetAll();
        // Expectations
        expect(userRepository.findByUuId(eq(userUuId))).andReturn(user).once();
        // Replay
        replayAll();
        // Run test scenario
        final User result = userService.getOrCreateUserForUuId(userUuId);
        assertEquals(user, result);
        // Verify
        verifyAll();
    }

    @Test
    public void testGetOrCreateUserForUuIdWhenUserDoesNotExist() {
        // Test data
        final String userUuId = "JKIBOBPUHUPGGIO";
        // Reset
        resetAll();
        // Expectations
        expect(userRepository.findByUuId(eq(userUuId))).andReturn(null).times(2);
        expect(userRepository.save(isA(User.class))).andAnswer(() -> (User) getCurrentArguments()[0]).once();
        // Replay
        replayAll();
        // Run test scenario
        final User result = userService.getOrCreateUserForUuId(userUuId);
        getServicesImplTestHelper().assertUser(result, new UserDto(userUuId));
        // Verify
        verifyAll();
    }

    @Test
    public void testGetUserByUuIdWithInvalidArguments() {
        // Reset
        resetAll();
        // Replay
        replayAll();
        // Run test scenario
        try {
            userService.getUserByUuId(null);
            fail("Exception should be thrown");
        } catch (final IllegalArgumentException ex) {
            // Expected
        }
        // Verify
        verifyAll();
    }

    @Test
    public void testGetUserByUuIdWithNotExistingUuId() {
        // Test data
        final String uuId = "KBLHOYUFOYTF^&FOYCCKTHLJ:";
        // Reset
        resetAll();
        // Expectations
        expect(userRepository.findByUuId(eq(uuId))).andReturn(null).once();
        // Replay
        replayAll();
        // Run test scenario
        try {
            userService.getUserByUuId(uuId);
            fail("Exception should be thrown");
        } catch (final UserNotFoundForUuidException ex) {
            // Expected
            assertUserNotFoundForUuidException(ex, uuId);
        }
        // Verify
        verifyAll();
    }

    @Test
    public void testGetUserByUuId() {
        // Test data
        final String uuId = "KBLHOYUFOYTF^&FOYCCKTHLJ:";
        final User user = getServicesImplTestHelper().createUser();
        // Reset
        resetAll();
        // Expectations
        expect(userRepository.findByUuId(eq(uuId))).andReturn(user).once();
        // Replay
        replayAll();
        // Run test scenario
        final User result = userService.getUserByUuId(uuId);
        assertEquals(user, result);
        // Verify
        verifyAll();
    }

    @Test
    public void testCheckIfUserExistsForUuIdWithInvalidArguments() {
        // Reset
        resetAll();
        // Replay
        replayAll();
        // Run test scenario
        try {
            userService.checkIfUserExistsForUuId(null);
            fail("Exception should be thrown");
        } catch (final IllegalArgumentException ex) {
            // Expected
        }
        // Verify
        verifyAll();
    }

    @Test
    public void testCheckIfUserExistsForUuIdWhenUserExists() {
        // Test data
        final String uuId = "JBYIUYFITYDR^DITDTTYCTK";
        final User user = getServicesImplTestHelper().createUser();
        // Reset
        resetAll();
        // Expectations
        expect(userRepository.findByUuId(eq(uuId))).andReturn(user).once();
        // Replay
        replayAll();
        // Run test scenario
        final boolean result = userService.checkIfUserExistsForUuId(uuId);
        assertTrue(result);
        // Verify
        verifyAll();
    }

    @Test
    public void testCheckIfUserExistsForUuIdWhenUserDoesNotExist() {
        // Test data
        final String uuId = "JBYIUYFITYDR^DITDTTYCTK";
        // Reset
        resetAll();
        // Expectations
        expect(userRepository.findByUuId(eq(uuId))).andReturn(null).once();
        // Replay
        replayAll();
        // Run test scenario
        final boolean result = userService.checkIfUserExistsForUuId(uuId);
        assertFalse(result);
        // Verify
        verifyAll();
    }

    @Test
    public void testCreateUserWithInvalidArguments() {
        // Reset
        resetAll();
        // Replay
        replayAll();
        // Run test scenario
        try {
            userService.createUser(null);
            fail("Exception should be thrown");
        } catch (final IllegalArgumentException ex) {
            // Expected
        }
        try {
            userService.createUser(new UserDto(null));
            fail("Exception should be thrown");
        } catch (final IllegalArgumentException ex) {
            // Expected
        }
        // Verify
        verifyAll();
    }

    @Test
    public void testCreateUserWithExistingUuId() {
        // Test data
        final UserDto userDto = getServicesImplTestHelper().createUserDto();
        final Long existingUserId = 1L;
        final User existingUser = getServicesImplTestHelper().createUser();
        existingUser.setId(existingUserId);
        // Reset
        resetAll();
        // Expectations
        expect(userRepository.findByUuId(eq(userDto.getUuId()))).andReturn(existingUser).once();
        // Replay
        replayAll();
        // Run test scenario
        try {
            userService.createUser(userDto);
            fail("Exception should be thrown");
        } catch (final UserAlreadyExistsForUuIdException ex) {
            // Expected
            assertUserAlreadyExistsForUuIdException(ex, userDto.getUuId(), existingUserId);
        }
        // Verify
        verifyAll();
    }

    @Test
    public void testCreateUser() {
        // Test data
        final UserDto userDto = getServicesImplTestHelper().createUserDto();
        // Reset
        resetAll();
        // Expectations
        expect(userRepository.findByUuId(eq(userDto.getUuId()))).andReturn(null).once();
        expect(userRepository.save(isA(User.class))).andAnswer(() -> (User) getCurrentArguments()[0]).once();
        // Replay
        replayAll();
        // Run test scenario
        final User result = userService.createUser(userDto);
        getServicesImplTestHelper().assertUser(result, userDto);
        // Verify
        verifyAll();
    }

    @Test
    public void testGetUserByIdWithInvalidArguments() {
        // Reset
        resetAll();
        // Replay
        replayAll();
        // Run test scenarios
        try {
            userService.getUserById(null);
            fail("Exception should be thrown");
        } catch (final IllegalArgumentException ex) {
            // Expected
        }
        // Verify
        verifyAll();

    }

    @Test
    public void testGetUserByIdWithNotExistingUser() {
        // Test data
        final Long userId = 1L;
        // Reset
        resetAll();
        // Expectations
        expect(userRepository.findOne(eq(userId))).andReturn(null).once();
        // Replay
        replayAll();
        // Run test scenarios
        try {
            userService.getUserById(userId);
            fail("Exception should be thrown");
        } catch (final UserNotFoundForIdException ex) {
            // Expected
            assertUserNotFoundForIdException(ex, userId);
        }
        // Verify
        verifyAll();
    }

    @Test
    public void testGetUserById() {
        // Test data
        final Long userId = 1L;
        final User user = getServicesImplTestHelper().createUser();
        user.setId(userId);
        // Reset
        resetAll();
        // Expectations
        expect(userRepository.findOne(eq(userId))).andReturn(user).once();
        // Replay
        replayAll();
        // Run test scenarios
        final User result = userService.getUserById(userId);
        assertEquals(user, result);
        // Verify
        verifyAll();
    }


    /* Utility methods */
    private void assertUserAlreadyExistsForUuIdException(final UserAlreadyExistsForUuIdException ex, final String uuId, final Long existingUserId) {
        assertEquals(uuId, ex.getUuId());
        assertEquals(existingUserId, ex.getExistingUserId());
    }

    private void assertUserNotFoundForIdException(final UserNotFoundForIdException ex, final Long id) {
        assertEquals(id, ex.getId());
        assertEquals(User.class, ex.getEntityClass());
    }

    private void assertUserNotFoundForUuidException(final UserNotFoundForUuidException ex, final String uuId) {
        assertEquals(uuId, ex.getUuId());
    }
}
