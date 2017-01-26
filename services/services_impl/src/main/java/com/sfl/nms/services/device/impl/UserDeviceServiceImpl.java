package com.sfl.nms.services.device.impl;

import com.sfl.nms.services.device.dto.UserDeviceDto;
import com.sfl.nms.services.device.exception.UserDeviceAlreadyExistsException;
import com.sfl.nms.services.device.exception.UserDeviceNotFoundForIdException;
import com.sfl.nms.services.user.UserService;
import com.sfl.nms.services.user.model.User;
import com.sfl.nms.persistence.repositories.device.UserDeviceRepository;
import com.sfl.nms.services.device.UserDeviceService;
import com.sfl.nms.services.device.exception.UserDeviceNotFoundForUuIdException;
import com.sfl.nms.services.device.model.UserDevice;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import javax.annotation.Nonnull;

/**
 * User: Ruben Dilanyan
 * Company: SFL LLC
 * Date: 1/10/16
 * Time: 1:50 PM
 */
@Service
public class UserDeviceServiceImpl implements UserDeviceService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserDeviceServiceImpl.class);

    /* Dependencies */
    @Autowired
    private UserDeviceRepository userDeviceRepository;

    @Autowired
    private UserService userService;

    /* Constructors */
    public UserDeviceServiceImpl() {
    }

    @Nonnull
    @Override
    public UserDevice getUserDeviceById(@Nonnull final Long userDeviceId) {
        Assert.notNull(userDeviceId, "User device id should not be null");
        LOGGER.debug("Retrieving user device for id - {}", userDeviceId);
        final UserDevice userDevice = userDeviceRepository.findOne(userDeviceId);
        assertUserDeviceNotNullForId(userDevice, userDeviceId);
        LOGGER.debug("Successfully retrieved user device for id - {}, user device - {}", userDeviceId, userDevice);
        return userDevice;
    }

    @Transactional
    @Nonnull
    @Override
    public UserDevice createUserDevice(@Nonnull final Long userId, @Nonnull final UserDeviceDto userDeviceDto) {
        assertUserId(userId);
        assertUserDeviceDto(userDeviceDto);
        LOGGER.debug("Creating user device for user with id - {}, user device DTO - {}", userId, userDeviceDto);
        final User user = userService.getUserById(userId);
        assertNoUserDeviceExistsForUuId(user, userDeviceDto.getUuId());
        // Create user device
        UserDevice userDevice = new UserDevice();
        userDevice.setUser(user);
        userDeviceDto.updateDomainEntityProperties(userDevice);
        // Persist user device
        userDevice = userDeviceRepository.save(userDevice);
        LOGGER.debug("Successfully created user device with id - {}, user device - {}", userDevice.getId(), userDevice);
        return userDevice;
    }

    @Nonnull
    @Override
    public UserDevice getUserDeviceByUuId(@Nonnull final Long userId, @Nonnull final String uuId) {
        assertUserId(userId);
        assertUuIdNotNull(uuId);
        LOGGER.debug("Getting user device for user with id - {}, device uuid - {}", userId, uuId);
        final User user = userService.getUserById(userId);
        final UserDevice userDevice = userDeviceRepository.findByUserAndUuId(user, uuId);
        assertUserDeviceNotNullForUuId(userDevice, userId, uuId);
        LOGGER.debug("Successfully retrieved user device with id - {} for user with id - {}, device uuid - {}", userDevice.getId(), userId, uuId);
        return userDevice;
    }

    @Nonnull
    @Override
    public boolean checkIfUserDeviceExistsForUuId(@Nonnull final Long userId, @Nonnull final String uuId) {
        assertUserId(userId);
        assertUuIdNotNull(uuId);
        LOGGER.debug("Checking if user device exists for user with id - {}, device uuid - {}", userId, uuId);
        final User user = userService.getUserById(userId);
        final UserDevice userDevice = userDeviceRepository.findByUserAndUuId(user, uuId);
        final boolean exists = (userDevice != null);
        LOGGER.debug("User device lookup result for user with id - {} and device uuid - {} is - {}", userId, uuId, exists);
        return exists;
    }

    @Transactional
    @Nonnull
    @Override
    public UserDevice getOrCreateUserDevice(@Nonnull final Long userId, @Nonnull final UserDeviceDto userDeviceDto) {
        assertUserId(userId);
        assertUserDeviceDto(userDeviceDto);
        LOGGER.debug("Getting or creating user device for user id - {}, device DTO - {}", userId, userDeviceDto);
        final User user = userService.getUserById(userId);
        UserDevice userDevice = userDeviceRepository.findByUserAndUuId(user, userDeviceDto.getUuId());
        if (userDevice == null) {
            userDevice = createUserDevice(userId, userDeviceDto);
            LOGGER.debug("Created user device with id - {} for user with id - {}, device DTO - {}", userDevice.getId(), user.getId(), userDeviceDto);
        } else {
            LOGGER.debug("User device with id - {} already exists for user with id - {}, device DTO - {}", userDevice.getId(), user.getId(), userDeviceDto);
        }
        return userDevice;
    }

    /* Utility methods */
    private void assertUserDeviceNotNullForUuId(final UserDevice userDevice, final Long userId, final String uuId) {
        if (userDevice == null) {
            LOGGER.error("No user device was found for user with id - {}, device uuid - {}", userId, uuId);
            throw new UserDeviceNotFoundForUuIdException(userId, uuId);
        }
    }

    private void assertUserId(final Long userId) {
        Assert.notNull(userId, "User id should not be null");
    }

    private void assertUuIdNotNull(final String uuId) {
        Assert.notNull(uuId, "User uuid should not be null");
    }

    private void assertNoUserDeviceExistsForUuId(final User user, final String uuId) {
        final UserDevice userDevice = userDeviceRepository.findByUserAndUuId(user, uuId);
        if (userDevice != null) {
            LOGGER.error("User device with uuid - {} already exists for user with id - {}, existing device id - {}", uuId, user.getId(), userDevice.getId());
            throw new UserDeviceAlreadyExistsException(user.getId(), uuId, userDevice.getId());
        }
    }

    private void assertUserDeviceNotNullForId(final UserDevice userDevice, final Long id) {
        if (userDevice == null) {
            LOGGER.error("No user device was found for id - {}", id);
            throw new UserDeviceNotFoundForIdException(id);
        }
    }

    private void assertUserDeviceDto(final UserDeviceDto userDeviceDto) {
        Assert.notNull(userDeviceDto, "User device DTO should not be null");
        Assert.notNull(userDeviceDto.getUuId(), "User device UUID in user device DTO should not be null");
        Assert.notNull(userDeviceDto.getOsType(), "OS type is user device DTO should not be null");
    }

    /* Properties getters and setters */
    public UserDeviceRepository getUserDeviceRepository() {
        return userDeviceRepository;
    }

    public void setUserDeviceRepository(final UserDeviceRepository userDeviceRepository) {
        this.userDeviceRepository = userDeviceRepository;
    }
}
