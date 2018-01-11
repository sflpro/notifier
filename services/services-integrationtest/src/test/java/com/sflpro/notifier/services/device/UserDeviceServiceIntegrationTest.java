package com.sflpro.notifier.services.device;

import com.sflpro.notifier.services.device.dto.UserDeviceDto;
import com.sflpro.notifier.services.device.exception.UserDeviceAlreadyExistsException;
import com.sflpro.notifier.services.device.model.UserDevice;
import com.sflpro.notifier.services.test.AbstractServiceIntegrationTest;
import com.sflpro.notifier.services.user.model.User;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.*;

/**
 * User: Ruben Dilanyan
 * Company: SFL LLC
 * Date: 1/10/16
 * Time: 4:13 PM
 */
public class UserDeviceServiceIntegrationTest extends AbstractServiceIntegrationTest {

    /* Dependencies */
    @Autowired
    private UserDeviceService userDeviceService;

    /* Constructors */
    public UserDeviceServiceIntegrationTest() {
    }

    /* Test methods */
    @Test
    public void testGetOrCreateUserDevice() {
        // Prepare data
        final User user = getServicesTestHelper().createUser();
        final UserDeviceDto deviceDto = getServicesTestHelper().createUserDeviceDto();
        flushAndClear();
        // Check if device exists
        boolean deviceExists = userDeviceService.checkIfUserDeviceExistsForUuId(user.getId(), deviceDto.getUuId());
        assertFalse(deviceExists);
        // Get or create it
        final UserDevice createdDevice = userDeviceService.getOrCreateUserDevice(user.getId(), deviceDto);
        assertNotNull(createdDevice);
        deviceExists = userDeviceService.checkIfUserDeviceExistsForUuId(user.getId(), deviceDto.getUuId());
        assertTrue(deviceExists);
        // Load device again
        final UserDevice reloadedDevice = userDeviceService.getOrCreateUserDevice(user.getId(), deviceDto);
        assertNotNull(reloadedDevice);
        Assert.assertEquals(createdDevice.getId(), reloadedDevice.getId());
        // Flush, clear and check existence again
        flushAndClear();
        deviceExists = userDeviceService.checkIfUserDeviceExistsForUuId(user.getId(), deviceDto.getUuId());
        assertTrue(deviceExists);

    }

    @Test
    public void testGetUserDeviceById() {
        // Prepare data
        final UserDevice userDevice = getServicesTestHelper().createUserDevice();
        // Load user device and assert
        UserDevice result = userDeviceService.getUserDeviceById(userDevice.getId());
        assertEquals(userDevice, result);
        // Flush, clear, reload and assert
        flushAndClear();
        result = userDeviceService.getUserDeviceById(userDevice.getId());
        assertEquals(userDevice, result);
    }

    @Test
    public void testCreateUserDevice() {
        // Prepare data
        final User user = getServicesTestHelper().createUser();
        final UserDeviceDto deviceDto = getServicesTestHelper().createUserDeviceDto();
        flushAndClear();
        // Create user device
        UserDevice result = userDeviceService.createUserDevice(user.getId(), deviceDto);
        assertUserDevice(result, deviceDto, user);
        // Flush, clear, reload and assert again
        flushAndClear();
        result = userDeviceService.getUserDeviceById(result.getId());
        assertUserDevice(result, deviceDto, user);
    }

    @Test
    public void testCreateUserDeviceWithExistingUuId() {
        // Prepare data
        final User user = getServicesTestHelper().createUser();
        final UserDeviceDto deviceDto = getServicesTestHelper().createUserDeviceDto();
        flushAndClear();
        // Create user device
        final UserDevice result = userDeviceService.createUserDevice(user.getId(), deviceDto);
        assertNotNull(result);
        try {
            userDeviceService.createUserDevice(user.getId(), deviceDto);
            fail("Exception should be thrown");
        } catch (final UserDeviceAlreadyExistsException ex) {
            // Expected
        }
        // Flush, clear, reload and assert again
        flushAndClear();
        try {
            userDeviceService.createUserDevice(user.getId(), deviceDto);
            fail("Exception should be thrown");
        } catch (final UserDeviceAlreadyExistsException ex) {
            // Expected
        }
    }

    @Test
    public void testGetUserDeviceByUuId() {
        // Prepare data
        final UserDevice userDevice = getServicesTestHelper().createUserDevice();
        final User user = userDevice.getUser();
        // Load user device and assert
        UserDevice result = userDeviceService.getUserDeviceByUuId(user.getId(), userDevice.getUuId());
        assertEquals(userDevice, result);
        // Flush, clear, reload and assert
        flushAndClear();
        result = userDeviceService.getUserDeviceByUuId(user.getId(), userDevice.getUuId());
        assertEquals(userDevice, result);
    }

    @Test
    public void testCheckIfUserDeviceExistsForUuId() {
        // Prepare data
        final User user = getServicesTestHelper().createUser();
        final UserDeviceDto deviceDto = getServicesTestHelper().createUserDeviceDto();
        flushAndClear();
        // Check if device exists
        boolean result = userDeviceService.checkIfUserDeviceExistsForUuId(user.getId(), deviceDto.getUuId());
        assertFalse(result);
        // Create device and assert again
        final UserDevice userDevice = userDeviceService.createUserDevice(user.getId(), deviceDto);
        assertNotNull(userDevice);
        result = userDeviceService.checkIfUserDeviceExistsForUuId(user.getId(), deviceDto.getUuId());
        assertTrue(result);
        // Flush, clear, reload and check again
        flushAndClear();
        result = userDeviceService.checkIfUserDeviceExistsForUuId(user.getId(), deviceDto.getUuId());
        assertTrue(result);
    }

    /* Utility method */
    private void assertUserDevice(final UserDevice userDevice, final UserDeviceDto userDeviceDto, final User user) {
        getServicesTestHelper().assertUserDevice(userDevice, userDeviceDto);
        assertNotNull(userDevice.getUser());
        Assert.assertEquals(user.getId(), userDevice.getUser().getId());
    }
}
