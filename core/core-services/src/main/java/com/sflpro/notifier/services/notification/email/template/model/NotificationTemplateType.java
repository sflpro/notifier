package com.sflpro.notifier.services.notification.email.template.model;

/**
 * User: Davit Yeghiazaryan
 * Company: SFL LLC
 * Date 6/13/16
 * Time 7:35 PM
 */
public enum NotificationTemplateType {
    // --- password related email templates ---//
    FORGOT_PASSWORD("forgot-password");

    /* Properties */
    private final String templateName;

    /* Constructors */
    private NotificationTemplateType(String templateName)
    {
        this.templateName = templateName;
    }

    /* Properties getters */
    public String getTemplateName()
    {
        return templateName;
    }
}
