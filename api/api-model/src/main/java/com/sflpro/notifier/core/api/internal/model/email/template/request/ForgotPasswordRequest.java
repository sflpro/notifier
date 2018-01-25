package com.sflpro.notifier.core.api.internal.model.email.template.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sflpro.notifier.core.api.internal.model.email.template.EmailTemplateClientType;
import com.sflpro.notifier.core.api.internal.model.email.template.forgotpassword.ResetPasswordEmailClientModel;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * User: Davit Yeghiazaryan
 * Company: SFL LLC
 * Date 6/26/16
 * Time 3:36 PM
 */
public class ForgotPasswordRequest extends AbstractTemplatedEmailRequest {
    private static final long serialVersionUID = -1186877070585189621L;

    @JsonProperty("templateModel")
    private ResetPasswordEmailClientModel templateModel;

    /* Constructors */
    public ForgotPasswordRequest() {
        super(EmailTemplateClientType.FORGOT_PASSWORD);
    }

    /* Properties getters and setters */
    public ResetPasswordEmailClientModel getTemplateModel() {
        return templateModel;
    }

    public void setTemplateModel(final ResetPasswordEmailClientModel templateModel) {
        this.templateModel = templateModel;
    }

    /* Equals, HashCode and ToString */
    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ForgotPasswordRequest)) {
            return false;
        }
        final ForgotPasswordRequest that = (ForgotPasswordRequest) o;
        final EqualsBuilder builder = new EqualsBuilder();
        builder.appendSuper(super.equals(o));
        builder.append(this.getTemplateModel(), that.getTemplateModel());
        return builder.isEquals();
    }

    @Override
    public int hashCode() {
        final HashCodeBuilder builder = new HashCodeBuilder();
        builder.appendSuper(super.hashCode());
        builder.append(this.getTemplateModel());
        return builder.build();
    }


    @Override
    public String toString() {
        final ToStringBuilder builder = new ToStringBuilder(this);
        builder.appendSuper(super.toString());
        builder.append("templateModel", this.getTemplateModel());
        return builder.build();
    }
}
