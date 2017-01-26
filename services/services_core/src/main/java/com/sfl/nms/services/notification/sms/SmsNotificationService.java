package com.sfl.nms.services.notification.sms;

import com.sfl.nms.services.notification.dto.sms.SmsNotificationDto;
import com.sfl.nms.services.notification.AbstractNotificationService;
import com.sfl.nms.services.notification.model.sms.SmsNotification;

import javax.annotation.Nonnull;

/**
 * User: Ruben Dilanyan
 * Company: SFL LLC
 * Date: 3/21/15
 * Time: 7:52 PM
 */
public interface SmsNotificationService extends AbstractNotificationService<SmsNotification> {

    /**
     * Creates new SMS notification
     *
     * @param smsNotificationDto
     * @return smsNotification
     */
    @Nonnull
    SmsNotification createSmsNotification(@Nonnull final SmsNotificationDto smsNotificationDto);
}
