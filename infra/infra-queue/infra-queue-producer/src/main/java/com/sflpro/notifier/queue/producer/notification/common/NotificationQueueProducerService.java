package com.sflpro.notifier.queue.producer.notification.common;

import com.sflpro.notifier.db.entities.notification.NotificationSendingPriority;

import javax.annotation.Nonnull;
import java.util.Map;

/**
 * User: Mher Sargsyan
 * Company: SFL LLC
 * Date: 4/10/15
 * Time: 7:49 PM
 */
public interface NotificationQueueProducerService {

    /**
     * Processes event to start sending notification
     *
     * @param notificationId
     */
    void processStartSendingNotificationEvent(@Nonnull final Long notificationId, @Nonnull final NotificationSendingPriority sendingPriority, @Nonnull final Map<String, String> secureProperties);
}
