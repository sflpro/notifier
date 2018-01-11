package com.sflpro.notifier.services.notification.email.template.model;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * User: Davit Yeghiazaryan
 * Company: SFL LLC
 * Date 6/13/16
 * Time 12:11 PM
 */
public abstract class BaseEmailTemplateModel implements EmailTemplateModel {

    private static final long serialVersionUID = 5774047307368426330L;

    /* Properties */
    private String url;

    private String customerSupportUrl;

    private String emailCode;

    private String recipientEmail;

    /* Constructors */
    public BaseEmailTemplateModel() {
    }

    protected BaseEmailTemplateModel(final String url, final String emailCode) {
        this();
        this.url = url;
        this.emailCode = emailCode;
    }

    protected BaseEmailTemplateModel(final String url, final String emailCode, final String recipientEmail) {
        this();
        this.url = url;
        this.emailCode = emailCode;
        this.recipientEmail = recipientEmail;
    }

    /* Properties getters and setters */
    @Override
    public String getEmailCode() {
        return emailCode;
    }

    @Override
    public void setEmailCode(final String emailCode) {
        this.emailCode = emailCode;
    }

    @Override
    public String getUrl() {
        return url;
    }

    public void setUrl(final String url) {
        this.url = url;
    }

    @Override
    public String getCustomerSupportUrl() {
        return customerSupportUrl;
    }

    public void setCustomerSupportUrl(final String customerSupportUrl) {
        this.customerSupportUrl = customerSupportUrl;
    }

    public String getRecipientEmail()
    {
        return recipientEmail;
    }

    public void setRecipientEmail(String recipientEmail)
    {
        this.recipientEmail = recipientEmail;
    }

    /* Equals, HashCode and ToString */
    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof BaseEmailTemplateModel)) {
            return false;
        }
        final BaseEmailTemplateModel that = (BaseEmailTemplateModel) o;
        final EqualsBuilder builder = new EqualsBuilder();
        builder.append(this.getUrl(), that.getUrl());
        builder.append(this.getEmailCode(), that.getEmailCode());
        builder.append(this.getRecipientEmail(), that.getRecipientEmail());
        return builder.isEquals();
    }

    @Override
    public int hashCode() {
        final HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(this.getUrl());
        builder.append(this.getEmailCode());
        builder.append(this.getRecipientEmail());
        return builder.build();
    }


    @Override
    public String toString() {
        final ToStringBuilder builder = new ToStringBuilder(this);
        builder.append("url", this.getUrl());
        builder.append("emailCode", this.getEmailCode());
        builder.append("recipientEmail", this.getRecipientEmail());
        return builder.build();
    }
}
