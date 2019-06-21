package com.sflpro.notifier.queue.consumer.common;

import javax.annotation.Nonnull;
import java.util.Map;

/**
 * User: Mher Sargsyan
 * Company: SFL LLC
 * Date: 4/10/15
 * Time: 6:49 PM
 */
public interface NotificationQueueConsumerService {

    /**
     * Processes notification
     *
     * @param notificationId
     */
    void processNotification(@Nonnull final Long notificationId, @Nonnull final Map<String, String> secureProperties);
}
