package com.sflpro.notifier.db.repositories.repositories.device;

import com.sflpro.notifier.db.entities.device.UserDevice;
import com.sflpro.notifier.db.entities.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.annotation.Nonnull;

/**
 * User: Ruben Dilanyan
 * Company: SFL LLC
 * Date: 1/10/16
 * Time: 1:52 PM
 */
@Repository
public interface UserDeviceRepository extends JpaRepository<UserDevice, Long> {

    /**
     * Find user device by user and uuid
     *
     * @param user
     * @param uuId
     * @return userDevice
     */
    UserDevice findByUserAndUuId(@Nonnull final User user, final String uuId);
}
