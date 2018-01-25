package com.sflpro.notifier.services.user.impl;

import com.sflpro.notifier.db.entities.user.User;
import com.sflpro.notifier.db.repositories.repositories.user.UserRepository;
import com.sflpro.notifier.services.user.UserService;
import com.sflpro.notifier.services.user.dto.UserDto;
import com.sflpro.notifier.services.user.exception.UserAlreadyExistsForUuIdException;
import com.sflpro.notifier.services.user.exception.UserNotFoundForIdException;
import com.sflpro.notifier.services.user.exception.UserNotFoundForUuidException;
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
 * Date: 10/01/14
 * Time: 10:18 PM
 */
@Service
public class UserServiceImpl implements UserService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class);

    /* Properties */
    @Autowired
    private UserRepository userRepository;

    /* Constructors */
    public UserServiceImpl() {
        super();
    }

    /* Public methods */
    @Nonnull
    @Override
    public User getUserById(@Nonnull final Long id) {
        Assert.notNull(id, "User id should not be null");
        LOGGER.debug("Getting user for id - {}", id);
        final User user = userRepository.findOne(id);
        assertUserNotNullForId(user, id);
        LOGGER.debug("Successfully retrieved user for id - {}, user - {}", user.getId(), user);
        return user;
    }

    @Transactional
    @Nonnull
    @Override
    public User createUser(@Nonnull final UserDto userDto) {
        assertUserDto(userDto);
        LOGGER.debug("Creating user for DTO - {}", userDto);
        assertNoUserExistsForUuId(userDto.getUuId());
        // Create new user
        User user = new User();
        userDto.updateDomainEntityProperties(user);
        // Persist user
        user = userRepository.save(user);
        LOGGER.debug("Successfully created user with id - {}, user - {}", user.getId(), user);
        return user;
    }

    @Nonnull
    @Override
    public boolean checkIfUserExistsForUuId(@Nonnull final String uuId) {
        assertUserUuIdNotNull(uuId);
        LOGGER.debug("Checking if user exists for uuid - {}", uuId);
        final User user = userRepository.findByUuId(uuId);
        final boolean exists = (user != null);
        LOGGER.debug("Check if user exists for uuid - {} is - {}", uuId, exists);
        return exists;
    }

    @Nonnull
    @Override
    public User getUserByUuId(@Nonnull final String uuId) {
        assertUserUuIdNotNull(uuId);
        LOGGER.debug("Getting user for uuid - {}", uuId);
        final User user = userRepository.findByUuId(uuId);
        assertUserNotNullForUuId(user, uuId);
        LOGGER.debug("Successfully retrieved user for uuid - {}, user - {}", uuId, user);
        return user;
    }

    @Transactional
    @Nonnull
    @Override
    public User getOrCreateUserForUuId(final String uuId) {
        assertUserUuIdNotNull(uuId);
        LOGGER.debug("Getting or creating user for uuid - {}", uuId);
        User user = userRepository.findByUuId(uuId);
        if (user == null) {
            LOGGER.debug("No user was found for uuid - {}, creating new one", uuId);
            user = createUser(new UserDto(uuId));
        } else {
            LOGGER.debug("User for uuid already exists, using it, uuid - {}, user - {}", uuId, user);
        }
        return user;
    }

    /* Utility methods */
    private void assertUserUuIdNotNull(final String uuId) {
        Assert.notNull(uuId, "User uuid should not be null");
    }

    private void assertNoUserExistsForUuId(final String uuId) {
        final User user = userRepository.findByUuId(uuId);
        if (user != null) {
            LOGGER.error("User with id -{} already exists for UUID - {}", user.getId(), user.getUuId());
            throw new UserAlreadyExistsForUuIdException(uuId, user.getId());
        }
    }

    private void assertUserDto(@Nonnull final UserDto userDto) {
        Assert.notNull(userDto, "User DTO should not be null");
        Assert.notNull(userDto.getUuId(), "UUID in User DTO should not be null");
    }

    private void assertUserNotNullForId(final User user, final Long id) {
        if (user == null) {
            LOGGER.error("No user was found for id - {}", id);
            throw new UserNotFoundForIdException(id);
        }
    }

    private void assertUserNotNullForUuId(final User user, final String uuId) {
        if (user == null) {
            LOGGER.error("No user was found for uuid - {}", uuId);
            throw new UserNotFoundForUuidException(uuId);
        }
    }

    /* Properties getters and setters */
    public UserRepository getUserRepository() {
        return userRepository;
    }

    public void setUserRepository(final UserRepository userRepository) {
        this.userRepository = userRepository;
    }
}
