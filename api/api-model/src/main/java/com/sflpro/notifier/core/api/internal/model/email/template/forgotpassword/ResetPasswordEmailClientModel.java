package com.sflpro.notifier.core.api.internal.model.email.template.forgotpassword;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sflpro.notifier.core.api.internal.model.email.template.NextEventAwareBaseEmailClientModel;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * User: Davit Yeghiazaryan
 * Company: SFL LLC
 * Date 6/16/16
 * Time 6:01 PM
 */
public class ResetPasswordEmailClientModel extends NextEventAwareBaseEmailClientModel {
    private static final long serialVersionUID = 6026486056502484505L;

    /* Properties */
    @JsonProperty("name")
    private String name;

    @JsonProperty("token")
    private String token;

    @JsonProperty("resetUri")
    private String resetUri;

    @JsonProperty("registeredCustomer")
    private boolean registeredCustomer = true;

    @JsonProperty("corporateCustomer")
    private boolean corporateCustomer;

    @JsonProperty("reset_email")
    private String email;

    @JsonProperty("reset_token")
    private String verificationToken;

    /* Constructors */
    public ResetPasswordEmailClientModel() {
        //default constructor
    }

    /* Properties getters and setters */
    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public String getToken() {
        return token;
    }

    public void setToken(final String token) {
        this.token = token;
    }

    public String getResetUri() {
        return resetUri;
    }

    public void setResetUri(String resetUri) {
        this.resetUri = resetUri;
    }

    public boolean isRegisteredCustomer() {
        return registeredCustomer;
    }

    public void setRegisteredCustomer(final boolean registeredCustomer) {
        this.registeredCustomer = registeredCustomer;
    }

    public boolean isCorporateCustomer() {
        return corporateCustomer;
    }

    public void setCorporateCustomer(final boolean corporateCustomer) {
        this.corporateCustomer = corporateCustomer;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getVerificationToken() {
        return verificationToken;
    }

    public void setVerificationToken(String verificationToken) {
        this.verificationToken = verificationToken;
    }

    /* Equals, HashCode and ToString */
    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ResetPasswordEmailClientModel)) {
            return false;
        }
        final ResetPasswordEmailClientModel that = (ResetPasswordEmailClientModel) o;
        final EqualsBuilder builder = new EqualsBuilder();
        builder.appendSuper(super.equals(o));
        builder.append(this.getName(), that.getName());
        builder.append(this.getEmail(), that.getEmail());
        builder.append(this.getVerificationToken(), that.getVerificationToken());
        builder.append(this.getToken(), that.getToken());
        builder.append(this.isCorporateCustomer(), that.isCorporateCustomer());
        builder.append(this.isRegisteredCustomer(), that.isRegisteredCustomer());
        return builder.isEquals();
    }

    @Override
    public int hashCode() {
        final HashCodeBuilder builder = new HashCodeBuilder();
        builder.appendSuper(super.hashCode());
        builder.append(this.getName());
        builder.append(this.getEmail());
        builder.append(this.getVerificationToken());
        builder.append(this.getToken());
        builder.append(this.isCorporateCustomer());
        builder.append(this.isRegisteredCustomer());
        return builder.build();
    }


    @Override
    public String toString() {
        final ToStringBuilder builder = new ToStringBuilder(this);
        builder.appendSuper(super.toString());
        builder.append("name", this.getName());
        builder.append("email", this.getEmail());
        builder.append("verificationToken", this.getVerificationToken());
        builder.append("token", this.getToken());
        builder.append("corporateCustomer", this.isCorporateCustomer());
        builder.append("registeredCustomer", this.isRegisteredCustomer());
        return builder.build();
    }
}
