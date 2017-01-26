package com.sfl.nms.services.device;

import com.sfl.nms.services.device.dto.UserDeviceDto;
import com.sfl.nms.services.device.model.UserDevice;

import javax.annotation.Nonnull;

/**
 * User: Ruben Dilanyan
 * Company: SFL LLC
 * Date: 12/24/15
 * Time: 6:44 PM
 */
public interface UserDeviceService {

    /**
     * Gets user device for id
     *
     * @param userDeviceId
     * @return userDevice
     */
    @Nonnull
    UserDevice getUserDeviceById(@Nonnull final Long userDeviceId);

    /**
     * Creates user device
     *
     * @param userId
     * @param userDeviceDto
     * @return userDevice
     */
    @Nonnull
    UserDevice createUserDevice(@Nonnull final Long userId, @Nonnull final UserDeviceDto userDeviceDto);

    /**
     * Get user device for user id and uuid
     *
     * @param userId
     * @param uuId
     * @return userDevice
     */
    @Nonnull
    UserDevice getUserDeviceByUuId(@Nonnull final Long userId, @Nonnull final String uuId);

    /**
     * Checks if user device exists for user id and uuid
     *
     * @param userId
     * @param uuId
     * @return
     */
    @Nonnull
    boolean checkIfUserDeviceExistsForUuId(@Nonnull final Long userId, @Nonnull final String uuId);


    /**
     * Gets or creates user device for provided DTO
     *
     * @param userId
     * @param userDeviceDto
     * @return userDevice
     */
    @Nonnull
    UserDevice getOrCreateUserDevice(@Nonnull final Long userId, @Nonnull final UserDeviceDto userDeviceDto);

}
