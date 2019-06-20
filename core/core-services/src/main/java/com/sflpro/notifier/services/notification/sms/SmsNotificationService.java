package com.sflpro.notifier.services.notification.sms;

import com.sflpro.notifier.db.entities.notification.sms.SmsNotification;
import com.sflpro.notifier.services.notification.AbstractNotificationService;
import com.sflpro.notifier.services.notification.dto.NotificationPropertyDto;
import com.sflpro.notifier.services.notification.dto.sms.SmsNotificationDto;

import javax.annotation.Nonnull;
import java.util.List;

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
     * @param smsNotificationPropertyDtos
     * @return smsNotification
     */
    @Nonnull
    SmsNotification createSmsNotification(@Nonnull final SmsNotificationDto smsNotificationDto, final List<NotificationPropertyDto> smsNotificationPropertyDtos);
}
