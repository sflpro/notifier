package com.sflpro.notifier.services.notification.email.template.model.forgotpassword;

import com.sflpro.notifier.services.notification.email.template.model.NextEventAwareBaseEmailTemplateModel;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.io.Serializable;

/**
 * @author Ruben Dilanyan
 *         <p>
 *         Jul 11, 2014
 */
public class ResetPasswordEmailTemplateModel extends NextEventAwareBaseEmailTemplateModel implements Serializable {
    private static final long serialVersionUID = 1L;

    /* Properties */
    private String name;

    private String token;

    private String email;

    private String verificationToken;

    private boolean isRegisteredCustomer = true;

    private boolean isCorporateCustomer;

    public ResetPasswordEmailTemplateModel() {
        super();
    }

    public ResetPasswordEmailTemplateModel(final String name, final String token, final String email, final String verificationToken) {
        this.name = name;
        this.token = token;
        this.email = email;
        this.verificationToken = verificationToken;
    }

    public boolean getIsRegisteredCustomer() {
        return isRegisteredCustomer;
    }

    public void setRegisteredCustomer(boolean isRegisteredCustomer) {
        this.isRegisteredCustomer = isRegisteredCustomer;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getVerificationToken() {
        return verificationToken;
    }

    public void setVerificationToken(String verificationToken) {
        this.verificationToken = verificationToken;
    }

    public boolean isCorporateCustomer() {
        return isCorporateCustomer;
    }

    public void setCorporateCustomer(final boolean corporateCustomer) {
        isCorporateCustomer = corporateCustomer;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        ToStringBuilder builder = new ToStringBuilder(this);
        builder.appendSuper(super.toString());
        builder.append("name", getName());
        builder.append("token", getToken());
        builder.append("email", getEmail());
        builder.append("verificationToken", getVerificationToken());
        return builder.build();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj == this) {
            return true;
        }
        if (obj.getClass() != getClass()) {
            return false;
        }
        ResetPasswordEmailTemplateModel rhs = (ResetPasswordEmailTemplateModel) obj;
        return new EqualsBuilder()
                .appendSuper(super.equals(obj))
                .append(this.name, rhs.name)
                .append(this.token, rhs.token)
                .append(this.verificationToken, rhs.verificationToken)
                .append(this.email, rhs.email)
                .append(this.isRegisteredCustomer, rhs.isRegisteredCustomer)
                .append(this.isCorporateCustomer, rhs.isCorporateCustomer)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .appendSuper(super.hashCode())
                .append(name)
                .append(token)
                .append(verificationToken)
                .append(email)
                .append(isRegisteredCustomer)
                .append(isCorporateCustomer)
                .toHashCode();
    }
}
