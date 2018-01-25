package com.sflpro.notifier.api.internal.facade.notification.email.template.impl;

import com.sflpro.notifier.api.internal.facade.notification.email.template.ApplicationNotificationProcessorFacade;
import com.sflpro.notifier.api.internal.facade.notification.email.template.converter.EmailTemplateModelConverter;
import com.sflpro.notifier.core.api.internal.model.common.result.ResultResponseModel;
import com.sflpro.notifier.core.api.internal.model.email.EmailNotificationModel;
import com.sflpro.notifier.core.api.internal.model.email.response.CreateEmailNotificationResponse;
import com.sflpro.notifier.core.api.internal.model.email.template.BaseEmailClientModel;
import com.sflpro.notifier.core.api.internal.model.email.template.EmailTemplateClientType;
import com.sflpro.notifier.core.api.internal.model.email.template.request.AbstractTemplatedEmailRequest;
import com.sflpro.notifier.core.api.internal.model.email.template.request.ForgotPasswordRequest;
import com.sflpro.notifier.core.api.internal.model.notification.NotificationClientType;
import com.sflpro.notifier.core.api.internal.model.notification.NotificationModel;
import com.sflpro.notifier.core.api.internal.model.notification.NotificationStateClientType;
import com.sflpro.notifier.db.entities.notification.Notification;
import com.sflpro.notifier.services.common.exception.ServicesRuntimeException;
import com.sflpro.notifier.services.notification.email.ApplicationTransactionalEmailService;
import com.sflpro.notifier.services.notification.email.EmailPreparationService;
import com.sflpro.notifier.services.notification.email.template.model.BaseEmailTemplateModel;
import com.sflpro.notifier.services.notification.email.template.model.forgotpassword.ResetPasswordEmailTemplateModel;
import com.sflpro.notifier.db.entities.notification.email.EmailNotification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import javax.annotation.Nonnull;
import java.util.List;

/**
 * User: Davit Yeghiazaryan
 * Company: SFL LLC
 * Date 6/15/16
 * Time 4:58 PM
 */
@Component
public class ApplicationNotificationProcessorFacadeImpl implements ApplicationNotificationProcessorFacade {
    private static final Logger LOGGER = LoggerFactory.getLogger(ApplicationNotificationProcessorFacadeImpl.class);

    /* Dependencies */
    @Autowired
    private List<EmailTemplateModelConverter> converters;

    @Autowired
    private ApplicationTransactionalEmailService applicationTransactionalEmailService;

    @Autowired
    private EmailPreparationService emailPreparationService;

    /* Constructors */
    public ApplicationNotificationProcessorFacadeImpl() {
        LOGGER.debug("Initializing templated email notificatoin service facade");
    }

    /* Public methods */
    @Nonnull
    @Override
    public ResultResponseModel<CreateEmailNotificationResponse> processForgotPassword(@Nonnull final ForgotPasswordRequest request) {
        assertRequest(request);
        Assert.notNull(request.getTemplateModel(), "Template model should not be null");
        LOGGER.debug("Processing forgot password request - {}", request);
        final BaseEmailTemplateModel baseEmailTemplateModel = convertTemplateModel(request.getEmailTemplateType(), request.getTemplateModel());
        final EmailNotification notification = emailPreparationService.prepareAndSendResetPasswordEmail(request.getRecipientEmail(),
                (ResetPasswordEmailTemplateModel) baseEmailTemplateModel);
        final ResultResponseModel<CreateEmailNotificationResponse> resultResponseModel = createResultModel(notification);
        LOGGER.debug("Successfully processed forgot password request - {}. result - {}", request, resultResponseModel);
        return resultResponseModel;
    }

    /* Properties getters and setters */
    public List<EmailTemplateModelConverter> getConverters() {
        return converters;
    }

    public void setConverters(final List<EmailTemplateModelConverter> converters) {
        this.converters = converters;
    }

    public ApplicationTransactionalEmailService getApplicationTransactionalEmailService() {
        return applicationTransactionalEmailService;
    }

    public void setApplicationTransactionalEmailService(final ApplicationTransactionalEmailService applicationTransactionalEmailService) {
        this.applicationTransactionalEmailService = applicationTransactionalEmailService;
    }

    /* Utility methods */
    private static final void assertRequest(final AbstractTemplatedEmailRequest request) {
        Assert.notNull(request, "Request should not be null");
        Assert.notNull(request.getEmailTemplateType(), "Email type in email model should not be null");
        Assert.notNull(request.getRecipientEmail(), "Recipient email should not be null");
        Assert.notNull(request.getSenderEmail(), "Sender email should not be null");
    }

    private BaseEmailTemplateModel convertTemplateModel(final EmailTemplateClientType emailTemplateClientType, final BaseEmailClientModel emailModel) {
        final EmailTemplateModelConverter emailTemplateModelConverter = getEmailTemplateConverter(emailTemplateClientType);
        final BaseEmailTemplateModel baseEmailTemplateModel = emailTemplateModelConverter.createEmailTemplateModel();
        emailTemplateModelConverter.convertClientModelToTemplateModel(emailModel, baseEmailTemplateModel);
        return baseEmailTemplateModel;
    }

    private static void setNotificationCommonProperties(final NotificationModel notificationModel, final Notification notification) {
        notificationModel.setBody(notification.getContent());
        notificationModel.setUuId(notification.getUuId());
        notificationModel.setSubject(notification.getSubject());
        notificationModel.setType(NotificationClientType.valueOf(notification.getType().name()));
        notificationModel.setState(NotificationStateClientType.valueOf(notification.getState().name()));
    }

    private static EmailNotificationModel createEmailNotificationModel(final EmailNotification emailNotification) {
        final EmailNotificationModel notificationModel = new EmailNotificationModel();
        setNotificationCommonProperties(notificationModel, emailNotification);
        notificationModel.setRecipientEmail(emailNotification.getRecipientEmail());
        notificationModel.setSenderEmail(emailNotification.getSenderEmail());
        return notificationModel;
    }

    private static ResultResponseModel<CreateEmailNotificationResponse> createResultModel(final EmailNotification notification) {
        final EmailNotificationModel emailNotificationModel = createEmailNotificationModel(notification);
        final CreateEmailNotificationResponse response = new CreateEmailNotificationResponse(emailNotificationModel);
        final ResultResponseModel<CreateEmailNotificationResponse> result = new ResultResponseModel<>();
        result.setResponse(response);
        return result;
    }

    private EmailTemplateModelConverter getEmailTemplateConverter(final EmailTemplateClientType templateType) {
        return converters.stream().filter(emailTemplateConverter -> templateType.equals(emailTemplateConverter.getEmailTemplateType())).
                findFirst().orElseThrow(() -> new ServicesRuntimeException("No converter found for template type " + templateType));
    }
}
