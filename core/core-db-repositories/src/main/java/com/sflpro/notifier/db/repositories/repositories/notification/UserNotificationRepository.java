package com.sflpro.notifier.db.repositories.repositories.notification;

import com.sflpro.notifier.db.entities.notification.Notification;
import com.sflpro.notifier.db.entities.notification.UserNotification;
import com.sflpro.notifier.db.entities.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.annotation.Nonnull;
import java.util.List;

/**
 * User: Ruben Dilanyan
 * Company: SFL LLC
 * Date: 4/2/15
 * Time: 7:01 PM
 */
@Repository
public interface UserNotificationRepository extends JpaRepository<UserNotification, Long> {

    /**
     * Gets user notification by notification
     *
     * @param notification
     * @return userNotification
     */
    UserNotification findByNotification(@Nonnull final Notification notification);

    /**
     * Gets user notifications by user
     *
     * @param user
     * @return userNotifications
     */
    List<UserNotification> findByUser(@Nonnull final User user);
}
