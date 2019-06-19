package com.sflpro.notifier.api.facade.endpoints.notification.email.impl;

import com.sflpro.notifier.api.facade.endpoints.notification.email.EmailNotificationModelConverter;
import com.sflpro.notifier.api.model.email.EmailNotificationModel;
import com.sflpro.notifier.api.model.email.request.CreateEmailNotificationRequest;
import com.sflpro.notifier.api.model.notification.NotificationClientType;
import com.sflpro.notifier.api.model.notification.NotificationStateClientType;
import com.sflpro.notifier.db.entities.notification.NotificationProviderType;
import com.sflpro.notifier.db.entities.notification.email.EmailNotification;
import com.sflpro.notifier.services.notification.dto.email.EmailNotificationDto;
import com.sflpro.notifier.services.notification.dto.email.EmailNotificationPropertyDto;
import com.sflpro.notifier.services.notification.event.sms.StartSendingNotificationEvent;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * User: Syuzanna Eprikyan
 * Company: SFL LLC
 * Date: 6/19/19
 * Time: 10:30 AM
 */

@Component
public class EmailNotificationModelConverterImpl implements EmailNotificationModelConverter {

    @Override
    public EmailNotificationDto toDto(final CreateEmailNotificationRequest request) {
        final EmailNotificationDto emailNotificationDto = new EmailNotificationDto();
        emailNotificationDto.setRecipientEmail(request.getRecipientEmail());
        emailNotificationDto.setSenderEmail(request.getSenderEmail());
        emailNotificationDto.setProviderType(NotificationProviderType.SMTP_SERVER);
        emailNotificationDto.setContent(request.getBody());
        emailNotificationDto.setSubject(request.getSubject());
        emailNotificationDto.setClientIpAddress(request.getClientIpAddress());
        emailNotificationDto.setTemplateName(request.getTemplateName());
        emailNotificationDto.setSecureProperties(request.getSecureProperties());
        emailNotificationDto.setUserUuid(request.getUserUuId());
        return emailNotificationDto;
    }

    @Override
    public EmailNotificationModel toModel(final EmailNotification emailNotification) {
        final EmailNotificationModel notificationModel = new EmailNotificationModel();
        notificationModel.setUuId(emailNotification.getUuId());
        notificationModel.setBody(emailNotification.getContent());
        notificationModel.setSubject(emailNotification.getSubject());
        notificationModel.setType(NotificationClientType.valueOf(emailNotification.getType().name()));
        notificationModel.setState(NotificationStateClientType.valueOf(emailNotification.getState().name()));
        notificationModel.setSenderEmail(emailNotification.getSenderEmail());
        notificationModel.setRecipientEmail(emailNotification.getRecipientEmail());
        return notificationModel;
    }

    @Override
    public List<EmailNotificationPropertyDto> toPropertiesDtoList(final Map<String, String> properties) {
        return properties.entrySet()
                .stream()
                .map(entry -> new EmailNotificationPropertyDto(entry.getKey(), entry.getValue()))
                .collect(Collectors.toList());
    }

    @Override
    public StartSendingNotificationEvent toSendEmailNotificationModel(final Long notificationId, final CreateEmailNotificationRequest request) {
        return new StartSendingNotificationEvent(notificationId, request.getSecureProperties());
    }
}
