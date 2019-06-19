package com.sflpro.notifier.api.facade.endpoints.notification.email;

import com.sflpro.notifier.api.model.email.EmailNotificationModel;
import com.sflpro.notifier.api.model.email.request.CreateEmailNotificationRequest;
import com.sflpro.notifier.db.entities.notification.email.EmailNotification;
import com.sflpro.notifier.services.notification.dto.email.EmailNotificationDto;
import com.sflpro.notifier.services.notification.dto.email.EmailNotificationPropertyDto;
import com.sflpro.notifier.services.notification.event.sms.StartSendingNotificationEvent;

import java.util.List;
import java.util.Map;

/**
 * User: Syuzanna Eprikyan
 * Company: SFL LLC
 * Date: 6/19/19
 * Time: 10:29 AM
 */
public interface EmailNotificationModelConverter {

    EmailNotificationDto toDto(final CreateEmailNotificationRequest request);

    EmailNotificationModel toModel(final EmailNotification emailNotification);

    List<EmailNotificationPropertyDto> toPropertiesDtoList(final Map<String, String> properties);

    StartSendingNotificationEvent toSendEmailNotificationModel(final Long notificationId, final CreateEmailNotificationRequest request);
}
