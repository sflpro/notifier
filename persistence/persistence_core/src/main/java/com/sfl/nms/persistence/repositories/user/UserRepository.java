package com.sfl.nms.persistence.repositories.user;

import com.sfl.nms.services.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.annotation.Nonnull;

/**
 * User: Ruben Dilanyan
 * Company: SFL LLC
 * Date: 10/01/14
 * Time: 10:18 PM
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Find user for uuid
     *
     * @param uuId
     * @return user
     */
    User findByUuId(@Nonnull final String uuId);
}
