package com.sfl.nms.queue.consumer.notification.push;

import javax.annotation.Nonnull;

/**
 * Created by alfkaghyan
 * Date: 9/17/15
 * Time: 2:05 PM
 */
public interface PushNotificationQueueConsumerService {

    /**
     * Process station review push notification
     *
     * @param carWashAppointmentId
     */
    void processStationReviewPushNotification(@Nonnull final Long carWashAppointmentId);

    /**
     * Process order payment notification
     *
     * @param carWashAppointmentUuid
     */
    void processOrderPaymentNotification(@Nonnull final String carWashAppointmentUuid);

    /**
     * Process reminder push notification to pay order
     *
     * @param carWashAppointmentUuid
     */
    void processOrderPaymentReminderNotification(@Nonnull final String carWashAppointmentUuid);

    /**
     * Process car wash retention reminder push notification
     *
     * @param carWashRetentionReminderId
     */
    void processCarWashRetentionReminderNotification(@Nonnull final Long carWashRetentionReminderId);
}
