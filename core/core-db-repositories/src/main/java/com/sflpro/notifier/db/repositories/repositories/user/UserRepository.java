package com.sflpro.notifier.db.repositories.repositories.user;

import com.sflpro.notifier.db.entities.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.annotation.Nonnull;
import java.util.Optional;

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
    Optional<User> findByUuId(@Nonnull final String uuId);
}
