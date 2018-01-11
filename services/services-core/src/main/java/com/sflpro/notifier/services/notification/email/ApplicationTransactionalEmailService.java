package com.sflpro.notifier.services.notification.email;

import com.sflpro.notifier.services.notification.email.template.model.NotificationTemplateType;
import com.sflpro.notifier.services.notification.email.template.model.forgotpassword.ResetPasswordEmailTemplateModel;
import com.sflpro.notifier.services.notification.model.email.EmailNotification;

import javax.annotation.Nonnull;

/**
 * User: Davit Yeghiazaryan
 * Company: SFL LLC
 * Date 7/1/16
 * Time 4:21 PM
 */
public interface ApplicationTransactionalEmailService {

    /**
     * Send forgot password email
     *
     * @param from
     * @param to
     * @param templateType
     * @param emailTemplateModel
     *
     * @return EmailNotification
     */
    @Nonnull
    EmailNotification sendForgotPasswordEmail(@Nonnull final String from, @Nonnull final String to, @Nonnull final NotificationTemplateType templateType,
                                              @Nonnull final ResetPasswordEmailTemplateModel emailTemplateModel);

}
