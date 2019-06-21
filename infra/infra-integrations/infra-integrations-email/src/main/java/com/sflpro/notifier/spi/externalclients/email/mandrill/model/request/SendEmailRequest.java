package com.sflpro.notifier.externalclients.email.mandrill.model.request;

import java.util.Map;

/**
 * Company: SFL LLC
 * Created on 04/12/2017
 *
 * @author Davit Harutyunyan
 */
public class SendEmailRequest {

    private String recipientMail;

    private String templateName;

    private Map<String, Object> templateContent;

    public SendEmailRequest(String recipientMail, String templateName, Map<String, Object> templateContent) {
        this.recipientMail = recipientMail;
        this.templateName = templateName;
        this.templateContent = templateContent;
    }

    public String getRecipientMail() {
        return recipientMail;
    }

    public void setRecipientMail(String recipientMail) {
        this.recipientMail = recipientMail;
    }

    public String getTemplateName() {
        return templateName;
    }

    public void setTemplateName(String templateName) {
        this.templateName = templateName;
    }

    public Map<String, Object> getTemplateContent() {
        return templateContent;
    }

    public void setTemplateContent(Map<String, Object> templateContent) {
        this.templateContent = templateContent;
    }
}
