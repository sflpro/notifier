package com.sflpro.notifier.services.user;

import com.sflpro.notifier.db.entities.user.User;
import com.sflpro.notifier.services.user.dto.UserDto;

import javax.annotation.Nonnull;

/**
 * User: Ruben Dilanyan
 * Company: SFL LLC
 * Date: 10/01/14
 * Time: 10:18 PM
 */
public interface UserService {

    /**
     * Returns user for provided id
     *
     * @return user
     */
    @Nonnull
    User getUserById(@Nonnull final Long id);


    /**
     * Creates user for DTO
     *
     * @param userDto
     * @return user
     */
    @Nonnull
    User createUser(@Nonnull final UserDto userDto);

    /**
     * Checks if user exists for UUID
     *
     * @param uuId
     * @return exists
     */
    @Nonnull
    boolean checkIfUserExistsForUuId(@Nonnull final String uuId);

    /**
     * Gets user for provided UUID
     *
     * @param uuId
     * @return user
     */
    @Nonnull
    User getUserByUuId(@Nonnull final String uuId);

    /**
     * Gets or creates user for provided uuid
     *
     * @param uuId
     * @return user
     */
    @Nonnull
    User getOrCreateUserForUuId(final String uuId);
}
