package com.sflpro.notifier.core.api.internal.model.email.template;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.io.Serializable;

/**
 * User: Davit Yeghiazaryan
 * Company: SFL LLC
 * Date 6/15/16
 * Time 3:56 PM
 */
public abstract class BaseEmailClientModel implements Serializable {

    private static final long serialVersionUID = 5473190595430394074L;

    /* Properties */
    @JsonProperty("url")
    private String url;

    @JsonProperty("customerSupportUrl")
    private String customerSupportUrl;

    @JsonProperty("emailCode")
    private String emailCode;

    @JsonProperty("recipientEmail")
    private String recipientEmail;

    /* Constructors */
    public BaseEmailClientModel() {
    }

    /* Properties getters and setters */
    public String getUrl() {
        return url;
    }

    public void setUrl(final String url) {
        this.url = url;
    }

    public String getCustomerSupportUrl() {
        return customerSupportUrl;
    }

    public void setCustomerSupportUrl(final String customerSupportUrl) {
        this.customerSupportUrl = customerSupportUrl;
    }

    public String getEmailCode() {
        return emailCode;
    }

    public void setEmailCode(final String emailCode) {
        this.emailCode = emailCode;
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
        if (!(o instanceof BaseEmailClientModel)) {
            return false;
        }
        final BaseEmailClientModel that = (BaseEmailClientModel) o;
        return new EqualsBuilder()
                .append(this.getEmailCode(), that.getEmailCode())
                .append(this.getUrl(), that.getUrl())
                .append(this.getCustomerSupportUrl(), that.getCustomerSupportUrl())
                .append(this.getRecipientEmail(), that.getRecipientEmail())
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .append(this.getEmailCode())
                .append(this.getUrl())
                .append(this.getCustomerSupportUrl())
                .append(this.getRecipientEmail())
                .build();
    }


    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("emailCode", this.getEmailCode())
                .append("url", this.getUrl())
                .append("customerSupportUrl", this.getCustomerSupportUrl())
                .append("recipientEmail", this.getRecipientEmail())
                .build();
    }
}
