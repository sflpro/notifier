package com.sflpro.notifier.api.facade.services.email.impl;

import com.sflpro.notifier.api.facade.services.email.EmailNotificationServiceFacade;
import com.sflpro.notifier.api.model.common.result.ResultResponseModel;
import com.sflpro.notifier.api.model.email.EmailNotificationFileAttachmentModel;
import com.sflpro.notifier.api.model.email.EmailNotificationModel;
import com.sflpro.notifier.api.model.email.request.CreateEmailNotificationRequest;
import com.sflpro.notifier.api.model.email.request.EmailNotificationFileAttachmentRequest;
import com.sflpro.notifier.api.model.email.response.CreateEmailNotificationResponse;
import com.sflpro.notifier.api.model.notification.NotificationClientType;
import com.sflpro.notifier.api.model.notification.NotificationStateClientType;
import com.sflpro.notifier.db.entities.notification.NotificationProviderType;
import com.sflpro.notifier.db.entities.notification.email.EmailNotification;
import com.sflpro.notifier.db.entities.notification.email.EmailNotificationFileAttachment;
import com.sflpro.notifier.services.notification.dto.email.EmailNotificationDto;
import com.sflpro.notifier.services.notification.email.EmailNotificationService;
import com.sflpro.notifier.services.notification.event.sms.StartSendingNotificationEvent;
import com.sflpro.notifier.services.system.event.ApplicationEventDistributionService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by Hayk Mkrtchyan.
 * Date: 7/1/19
 * Time: 2:45 PM
 */
@Component
class EmailNotificationServiceFacadeImpl implements EmailNotificationServiceFacade {

    private final EmailNotificationService emailNotificationService;

    private final ApplicationEventDistributionService applicationEventDistributionService;

    private final NotificationProviderType providerType;

    EmailNotificationServiceFacadeImpl(final EmailNotificationService emailNotificationService,
                                       final ApplicationEventDistributionService applicationEventDistributionService,
                                       @Value("${email.provider:SMTP_SERVER}") final NotificationProviderType providerType) {
        this.emailNotificationService = emailNotificationService;
        this.applicationEventDistributionService = applicationEventDistributionService;
        this.providerType = providerType;
    }

    @Override
    public ResultResponseModel<CreateEmailNotificationResponse> createEmailNotification(final CreateEmailNotificationRequest request) {
        Assert.notNull(request, "Request model should not be null");
        final EmailNotificationDto emailNotificationDto = buildDto(request);
        final EmailNotification emailNotification = emailNotificationService.createEmailNotification(emailNotificationDto);
        applicationEventDistributionService.publishAsynchronousEvent(new StartSendingNotificationEvent(emailNotification.getId(), request.getSecureProperties()));
        final EmailNotificationModel emailNotificationModel = buildModel(emailNotification);
        return new ResultResponseModel<>(new CreateEmailNotificationResponse(emailNotificationModel));
    }

    /* utility methods */
    private EmailNotificationDto buildDto(final CreateEmailNotificationRequest request) {
        final EmailNotificationDto emailNotificationDto = new EmailNotificationDto();
        emailNotificationDto.setLocale(request.getLocale());
        emailNotificationDto.setRecipientEmail(request.getRecipientEmail());
        emailNotificationDto.setSenderEmail(request.getSenderEmail());
        emailNotificationDto.setContent(request.getBody());
        emailNotificationDto.setSubject(request.getSubject());
        emailNotificationDto.setProviderType(providerType);
        emailNotificationDto.setClientIpAddress(request.getClientIpAddress());
        emailNotificationDto.setTemplateName(request.getTemplateName());
        emailNotificationDto.setProperties(request.getProperties());
        emailNotificationDto.setUserUuid(request.getUserUuId());
        emailNotificationDto.setHasSecureProperties(!request.getSecureProperties().isEmpty());
        emailNotificationDto.setFileAttachments(mapFileAttachments(request.getFileAttachments()));
        return emailNotificationDto;
    }

    private EmailNotificationModel buildModel(final EmailNotification emailNotification) {
        final EmailNotificationModel notificationModel = new EmailNotificationModel();
        notificationModel.setUuId(emailNotification.getUuId());
        notificationModel.setBody(emailNotification.getContent());
        notificationModel.setSubject(emailNotification.getSubject());
        notificationModel.setType(NotificationClientType.valueOf(emailNotification.getType().name()));
        notificationModel.setState(NotificationStateClientType.valueOf(emailNotification.getState().name()));
        notificationModel.setSenderEmail(emailNotification.getSenderEmail());
        notificationModel.setRecipientEmail(emailNotification.getRecipientEmail());
        notificationModel.setFileAttachments(mapFileAttachmentsModel(emailNotification.getFileAttachments()));
        return notificationModel;
    }

    private Set<EmailNotificationFileAttachment> mapFileAttachments(final Set<EmailNotificationFileAttachmentRequest> fileAttachmentResource) {
        Set<EmailNotificationFileAttachment> destinationAttachments = new HashSet<>();

        for (EmailNotificationFileAttachmentRequest attachment : fileAttachmentResource) {
            EmailNotificationFileAttachment item = new EmailNotificationFileAttachment();
            item.setFileName(attachment.getFileName());
            item.setFileUrl(attachment.getFileUrl());
            item.setMimeType(attachment.getMimeType());
            destinationAttachments.add(item);
        }
        return destinationAttachments;
    }

    private Set<EmailNotificationFileAttachmentModel> mapFileAttachmentsModel(final Set<EmailNotificationFileAttachment> fileAttachmentResource){
        Set<EmailNotificationFileAttachmentModel> destinationAttachments = new HashSet<>();

        for (EmailNotificationFileAttachment attachment : fileAttachmentResource) {
            EmailNotificationFileAttachmentModel item = new EmailNotificationFileAttachmentModel();
            item.setFileName(attachment.getFileName());
            item.setFileUrl(attachment.getFileUrl());
            item.setMimeType(attachment.getMimeType());
            destinationAttachments.add(item);
        }
        return destinationAttachments;
    }
}
