package com.sfl.nms.services.user;

import com.sfl.nms.services.test.AbstractServiceIntegrationTest;
import com.sfl.nms.services.user.exception.UserAlreadyExistsForUuIdException;
import com.sfl.nms.services.user.model.User;
import com.sfl.nms.services.user.dto.UserDto;
import org.junit.Assert;
import static org.junit.Assert.*;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.UUID;

/**
 * User: Ruben Dilanyan
 * Company: SFL LLC
 * Date: 10/01/14
 * Time: 10:18 PM
 */
public class UserServiceIntegrationTest extends AbstractServiceIntegrationTest {


    /* Properties */
    @Autowired
    private UserService userService;

    /* Constructors */
    public UserServiceIntegrationTest() {

    }

    /* Test methods */
    @Test
    public void testGetOrCreateUserForUuId() {
        // Prepare data
        final String userUuId = UUID.randomUUID().toString();
        // Check if user exists
        boolean userExists = userService.checkIfUserExistsForUuId(userUuId);
        assertFalse(userExists);
        // Get or create user
        final User firstUser = userService.getOrCreateUserForUuId(userUuId);
        assertNotNull(firstUser);
        userExists = userService.checkIfUserExistsForUuId(userUuId);
        assertTrue(userExists);
        // Flush, clear and try again
        flushAndClear();
        userExists = userService.checkIfUserExistsForUuId(userUuId);
        assertTrue(userExists);
        // Call again and check that same user is returned
        final User secondUser = userService.getOrCreateUserForUuId(userUuId);
        assertNotNull(secondUser);
        Assert.assertEquals(firstUser.getId(), secondUser.getId());
    }

    @Test
    public void testCheckIfUserExistsForUuId() {
        // Prepare data
        final UserDto userDto = getServicesTestHelper().createUserDto();
        // Check no user exists
        boolean userExists = userService.checkIfUserExistsForUuId(userDto.getUuId());
        assertFalse(userExists);
        // Create user
        final User user = userService.createUser(userDto);
        assertNotNull(user);
        // Check if user exists
        userExists = userService.checkIfUserExistsForUuId(userDto.getUuId());
        assertTrue(userExists);
        // Flush, clear, reload and assert again
        flushAndClear();
        userExists = userService.checkIfUserExistsForUuId(userDto.getUuId());
        assertTrue(userExists);
    }

    @Test
    public void testGetUserById() {
        // Prepare data
        final User user = getServicesTestHelper().createUser();
        // Load user by id
        User result = userService.getUserById(user.getId());
        assertEquals(user, result);
        // Flush, clear, reload and assert again
        flushAndClear();
        result = userService.getUserById(user.getId());
        assertEquals(user, result);
    }

    @Test
    public void testGetUserByUuId() {
        // Prepare data
        final User user = getServicesTestHelper().createUser();
        // Load user by id
        User result = userService.getUserByUuId(user.getUuId());
        assertEquals(user, result);
        // Flush, clear, reload and assert again
        flushAndClear();
        result = userService.getUserByUuId(user.getUuId());
        assertEquals(user, result);
    }

    @Test
    public void testCreateUser() {
        // Prepare data
        final UserDto userDto = getServicesTestHelper().createUserDto();
        // Create user
        User user = userService.createUser(userDto);
        getServicesTestHelper().assertUser(user, userDto);
        // Flush, clear, reload and assert again
        flushAndClear();
        user = userService.getUserById(user.getId());
        getServicesTestHelper().assertUser(user, userDto);
    }

    @Test
    public void testCreateUserWithExistingUuId() {
        // Prepare data
        final UserDto userDto = getServicesTestHelper().createUserDto();
        // Create user
        User user = userService.createUser(userDto);
        assertNotNull(user);
        // Try to recreate user
        try {
            userService.createUser(userDto);
            fail("Exception should be thrown");
        } catch (final UserAlreadyExistsForUuIdException ex) {
            // Expected
        }
        // Flush, clear, reload and assert again
        flushAndClear();
        // Try to recreate user
        try {
            userService.createUser(userDto);
            fail("Exception should be thrown");
        } catch (final UserAlreadyExistsForUuIdException ex) {
            // Expected
        }
    }
}
