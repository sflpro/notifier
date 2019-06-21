package com.sflpro.notifier.services.device.impl;

import com.sflpro.notifier.db.entities.device.UserDevice;
import com.sflpro.notifier.db.entities.user.User;
import com.sflpro.notifier.db.repositories.repositories.device.UserDeviceRepository;
import com.sflpro.notifier.services.device.dto.UserDeviceDto;
import com.sflpro.notifier.services.device.exception.UserDeviceAlreadyExistsException;
import com.sflpro.notifier.services.device.exception.UserDeviceNotFoundForIdException;
import com.sflpro.notifier.services.device.exception.UserDeviceNotFoundForUuIdException;
import com.sflpro.notifier.services.test.AbstractServicesUnitTest;
import com.sflpro.notifier.services.user.UserService;
import org.easymock.Mock;
import org.easymock.TestSubject;
import org.junit.Assert;
import org.junit.Test;

import java.util.Optional;

import static org.easymock.EasyMock.*;
import static org.junit.Assert.*;

/**
 * User: Ruben Dilanyan
 * Company: SFL LLC
 * Date: 1/10/16
 * Time: 3:11 PM
 */
public class UserDeviceServiceImplTest extends AbstractServicesUnitTest {

    /* Test subject and mocks */
    @TestSubject
    private UserDeviceServiceImpl userDeviceService = new UserDeviceServiceImpl();

    @Mock
    private UserDeviceRepository userDeviceRepository;

    @Mock
    private UserService userService;

    /* Constructors */
    public UserDeviceServiceImplTest() {
    }

    /* Test methods */
    @Test
    public void testGetOrCreateUserDeviceWithInvalidArguments() {
        // Test data
        final Long userId = 1L;
        final UserDeviceDto deviceDto = getServicesImplTestHelper().createUserDeviceDto();
        // Reset
        resetAll();
        // Replay
        replayAll();
        // Run test scenario
        try {
            userDeviceService.getOrCreateUserDevice(null, deviceDto);
            fail("Exception should be thrown");
        } catch (final IllegalArgumentException ex) {
            // Expected
        }
        try {
            userDeviceService.getOrCreateUserDevice(userId, new UserDeviceDto(null, deviceDto.getOsType()));
            fail("Exception should be thrown");
        } catch (final IllegalArgumentException ex) {
            // Expected
        }
        try {
            userDeviceService.getOrCreateUserDevice(userId, new UserDeviceDto(deviceDto.getUuId(), null));
            fail("Exception should be thrown");
        } catch (final IllegalArgumentException ex) {
            // Expected
        }
        // Verify
        verifyAll();
    }

    @Test
    public void testGetOrCreateUserDeviceWhenItAlreadyExists() {
        // Test data
        final Long userId = 1L;
        final User user = getServicesImplTestHelper().createUser();
        user.setId(userId);
        final UserDeviceDto deviceDto = getServicesImplTestHelper().createUserDeviceDto();
        final Long userDeviceId = 2L;
        final UserDevice userDevice = getServicesImplTestHelper().createUserDevice();
        userDevice.setId(userDeviceId);
        // Reset
        resetAll();
        // Expectations
        expect(userService.getUserById(eq(userId))).andReturn(user).once();
        expect(userDeviceRepository.findByUserAndUuId(eq(user), eq(deviceDto.getUuId()))).andReturn(userDevice).once();
        // Replay
        replayAll();
        // Run test scenario
        final UserDevice result = userDeviceService.getOrCreateUserDevice(userId, deviceDto);
        assertEquals(userDevice, result);
        // Verify
        verifyAll();
    }

    @Test
    public void testGetOrCreateUserDeviceWhenItDoesNotExist() {
        // Test data
        final Long userId = 1L;
        final User user = getServicesImplTestHelper().createUser();
        user.setId(userId);
        final UserDeviceDto deviceDto = getServicesImplTestHelper().createUserDeviceDto();
        // Reset
        resetAll();
        // Expectations
        expect(userService.getUserById(eq(userId))).andReturn(user).times(2);
        expect(userDeviceRepository.findByUserAndUuId(eq(user), eq(deviceDto.getUuId()))).andReturn(null).times(2);
        expect(userDeviceRepository.save(isA(UserDevice.class))).andAnswer(() -> (UserDevice) getCurrentArguments()[0]).once();
        // Replay
        replayAll();
        // Run test scenario
        final UserDevice result = userDeviceService.getOrCreateUserDevice(userId, deviceDto);
        getServicesImplTestHelper().assertUserDevice(result, deviceDto);
        // Verify
        verifyAll();
    }

    @Test
    public void testCheckIfUserDeviceExistsForUuId() {
        // Test data
        final Long userId = 1L;
        final String deviceUuId = "JBIYITCTUFFVIFIFOYFTII";
        // Reset
        resetAll();
        // Replay
        replayAll();
        // Run test scenario
        try {
            userDeviceService.checkIfUserDeviceExistsForUuId(null, deviceUuId);
            fail("Exception should be thrown");
        } catch (final IllegalArgumentException ex) {
            // Expected
        }
        try {
            userDeviceService.checkIfUserDeviceExistsForUuId(userId, null);
            fail("Exception should be thrown");
        } catch (final IllegalArgumentException ex) {
            // Expected
        }
        // Verify
        verifyAll();
    }

    @Test
    public void testCheckIfUserDeviceExistsForUuIdWhenItExists() {
        // Test data
        final Long userId = 1L;
        final User user = getServicesImplTestHelper().createUser();
        user.setId(userId);
        final Long deviceId = 2L;
        final UserDevice userDevice = getServicesImplTestHelper().createUserDevice();
        userDevice.setId(deviceId);
        final String deviceUuId = userDevice.getUuId();
        // Reset
        resetAll();
        // Expectations
        expect(userService.getUserById(eq(userId))).andReturn(user).once();
        expect(userDeviceRepository.findByUserAndUuId(eq(user), eq(deviceUuId))).andReturn(userDevice).once();
        // Replay
        replayAll();
        // Run test scenario
        final boolean result = userDeviceService.checkIfUserDeviceExistsForUuId(userId, deviceUuId);
        assertTrue(result);
        // Verify
        verifyAll();
    }

    @Test
    public void testCheckIfUserDeviceExistsForUuIdWhenItDoesNotExist() {
        // Test data
        final Long userId = 1L;
        final User user = getServicesImplTestHelper().createUser();
        user.setId(userId);
        final Long deviceId = 2L;
        final UserDevice userDevice = getServicesImplTestHelper().createUserDevice();
        userDevice.setId(deviceId);
        final String deviceUuId = userDevice.getUuId();
        // Reset
        resetAll();
        // Expectations
        expect(userService.getUserById(eq(userId))).andReturn(user).once();
        expect(userDeviceRepository.findByUserAndUuId(eq(user), eq(deviceUuId))).andReturn(null).once();
        // Replay
        replayAll();
        // Run test scenario
        final boolean result = userDeviceService.checkIfUserDeviceExistsForUuId(userId, deviceUuId);
        assertFalse(result);
        // Verify
        verifyAll();
    }

    @Test
    public void testGetUserDeviceByUuIdWithInvalidArguments() {
        // Test data
        final Long userId = 1L;
        final String deviceUuId = "JBIYITCTUFFVIFIFOYFTII";
        // Reset
        resetAll();
        // Replay
        replayAll();
        // Run test scenario
        try {
            userDeviceService.getUserDeviceByUuId(null, deviceUuId);
            fail("Exception should be thrown");
        } catch (final IllegalArgumentException ex) {
            // Expected
        }
        try {
            userDeviceService.getUserDeviceByUuId(userId, null);
            fail("Exception should be thrown");
        } catch (final IllegalArgumentException ex) {
            // Expected
        }
        // Verify
        verifyAll();
    }

    @Test
    public void testGetUserDeviceByUuIdWithNotExistingUuId() {
        // Test data
        final Long userId = 1L;
        final User user = getServicesImplTestHelper().createUser();
        final String deviceUuId = "JBIYITCTUFFVIFIFOYFTII";
        // Reset
        resetAll();
        // Expectations
        expect(userService.getUserById(eq(userId))).andReturn(user).once();
        expect(userDeviceRepository.findByUserAndUuId(eq(user), eq(deviceUuId))).andReturn(null).once();
        // Replay
        replayAll();
        // Run test scenario
        try {
            userDeviceService.getUserDeviceByUuId(userId, deviceUuId);
            fail("Exception should be thrown");
        } catch (final UserDeviceNotFoundForUuIdException ex) {
            // Expected
            assertUserDeviceNotFoundForUuIdException(ex, userId, deviceUuId);
        }
        // Verify
        verifyAll();
    }

    @Test
    public void testGetUserDeviceByUuId() {
        // Test data
        final Long userId = 1L;
        final User user = getServicesImplTestHelper().createUser();
        final Long deviceId = 2L;
        final UserDevice userDevice = getServicesImplTestHelper().createUserDevice();
        userDevice.setId(deviceId);
        final String deviceUuId = userDevice.getUuId();
        // Reset
        resetAll();
        // Expectations
        expect(userService.getUserById(eq(userId))).andReturn(user).once();
        expect(userDeviceRepository.findByUserAndUuId(eq(user), eq(deviceUuId))).andReturn(userDevice).once();
        // Replay
        replayAll();
        // Run test scenario
        final UserDevice result = userDeviceService.getUserDeviceByUuId(userId, deviceUuId);
        assertEquals(userDevice, result);
        // Verify
        verifyAll();
    }

    @Test
    public void testCreateUserDeviceWithInvalidArguments() {
        // Test data
        final Long userId = 1L;
        final UserDeviceDto deviceDto = getServicesImplTestHelper().createUserDeviceDto();
        // Reset
        resetAll();
        // Replay
        replayAll();
        // Run test scenario
        try {
            userDeviceService.createUserDevice(null, deviceDto);
            fail("Exception should be thrown");
        } catch (final IllegalArgumentException ex) {
            // Expected
        }
        try {
            userDeviceService.createUserDevice(userId, null);
            fail("Exception should be thrown");
        } catch (final IllegalArgumentException ex) {
            // Expected
        }
        try {
            userDeviceService.createUserDevice(userId, new UserDeviceDto(null, deviceDto.getOsType()));
            fail("Exception should be thrown");
        } catch (final IllegalArgumentException ex) {
            // Expected
        }
        try {
            userDeviceService.createUserDevice(userId, new UserDeviceDto(deviceDto.getUuId(), null));
            fail("Exception should be thrown");
        } catch (final IllegalArgumentException ex) {
            // Expected
        }
        // Verify
        verifyAll();
    }

    @Test
    public void testCreateUserDeviceWithExistingUuId() {
        // Test data
        final Long userId = 1L;
        final User user = getServicesImplTestHelper().createUser();
        user.setId(userId);
        final UserDeviceDto deviceDto = getServicesImplTestHelper().createUserDeviceDto();
        final Long existingDeviceId = 2L;
        final UserDevice existingDevice = getServicesImplTestHelper().createUserDevice();
        existingDevice.setId(existingDeviceId);
        // Reset
        resetAll();
        // Expectations
        expect(userService.getUserById(eq(userId))).andReturn(user).once();
        expect(userDeviceRepository.findByUserAndUuId(eq(user), eq(deviceDto.getUuId()))).andReturn(existingDevice).once();
        // Replay
        replayAll();
        // Run test scenario
        try {
            userDeviceService.createUserDevice(userId, deviceDto);
            fail("Exception should be thrown");
        } catch (final UserDeviceAlreadyExistsException ex) {
            // Expected
            assertUserDeviceAlreadyExistsException(ex, userId, deviceDto.getUuId(), existingDeviceId);
        }
        // Verify
        verifyAll();
    }

    @Test
    public void testCreateUserDevice() {
        // Test data
        final Long userId = 1L;
        final User user = getServicesImplTestHelper().createUser();
        user.setId(userId);
        final UserDeviceDto deviceDto = getServicesImplTestHelper().createUserDeviceDto();
        // Reset
        resetAll();
        // Expectations
        expect(userService.getUserById(eq(userId))).andReturn(user).once();
        expect(userDeviceRepository.findByUserAndUuId(eq(user), eq(deviceDto.getUuId()))).andReturn(null).once();
        expect(userDeviceRepository.save(isA(UserDevice.class))).andAnswer(() -> (UserDevice) getCurrentArguments()[0]).once();
        // Replay
        replayAll();
        // Run test scenario
        final UserDevice result = userDeviceService.createUserDevice(userId, deviceDto);
        getServicesImplTestHelper().assertUserDevice(result, deviceDto);
        Assert.assertEquals(user, result.getUser());
        // Verify
        verifyAll();
    }

    @Test
    public void testGetUserDeviceByIdWithInvalidArguments() {
        // Reset
        resetAll();
        // Replay
        replayAll();
        // Run test scenario
        try {
            userDeviceService.getUserDeviceById(null);
            fail("Exception should be thrown");
        } catch (final IllegalArgumentException ex) {
            // Expected
        }
        // Verify
        verifyAll();
    }

    @Test
    public void testGetUserDeviceByIdWithNotExistingId() {
        // Test data
        final Long deviceId = 1L;
        // Reset
        resetAll();
        // Expectations
        expect(userDeviceRepository.findById(eq(deviceId))).andReturn(Optional.empty()).once();
        // Replay
        replayAll();
        // Run test scenario
        try {
            userDeviceService.getUserDeviceById(deviceId);
            fail("Exception should be thrown");
        } catch (final UserDeviceNotFoundForIdException ex) {
            // Expected
            assertUserDeviceNotFoundForIdException(ex, deviceId);
        }
        // Verify
        verifyAll();
    }

    @Test
    public void testGetUserDeviceById() {
        // Test data
        final Long deviceId = 1L;
        final UserDevice userDevice = getServicesImplTestHelper().createUserDevice();
        userDevice.setId(deviceId);
        // Reset
        resetAll();
        // Expectations
        expect(userDeviceRepository.findById(eq(deviceId))).andReturn(Optional.of(userDevice)).once();
        // Replay
        replayAll();
        // Run test scenario
        final UserDevice result = userDeviceService.getUserDeviceById(deviceId);
        assertEquals(userDevice, result);
        // Verify
        verifyAll();
    }

    /* Utility methods */
    private void assertUserDeviceNotFoundForUuIdException(final UserDeviceNotFoundForUuIdException ex, final Long userId, final String deviceUuId) {
        assertEquals(userId, ex.getUserId());
        assertEquals(deviceUuId, ex.getUuId());
    }

    private void assertUserDeviceAlreadyExistsException(final UserDeviceAlreadyExistsException ex, final Long userId, final String deviceUuId, final Long existingDeviceId) {
        assertEquals(userId, ex.getUserId());
        assertEquals(deviceUuId, ex.getUserDeviceUuId());
        assertEquals(existingDeviceId, ex.getExistingDeviceId());
    }

    private void assertUserDeviceNotFoundForIdException(final UserDeviceNotFoundForIdException ex, final Long id) {
        Assert.assertEquals(id, ex.getId());
        Assert.assertEquals(UserDevice.class, ex.getEntityClass());
    }
}
