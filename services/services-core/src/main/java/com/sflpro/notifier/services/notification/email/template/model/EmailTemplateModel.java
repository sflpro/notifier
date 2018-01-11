package com.sflpro.notifier.services.notification.email.template.model;

import java.io.Serializable;

/**
 * User: Davit Yeghiazaryan
 * Company: SFL LLC
 * Date 6/13/16
 * Time 12:15 PM
 */
public interface EmailTemplateModel extends Serializable {
    String getEmailCode();

    void setEmailCode(final String emailCode);

    String getUrl();

    String getCustomerSupportUrl();
}
