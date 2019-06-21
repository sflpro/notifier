package com.sflpro.notifier.externalclients.email.mandrill.model;

import com.sflpro.notifier.externalclients.email.common.model.AbstractEmailNotificationApiCommunicatorModel;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * Company: SFL LLC
 * Created on 04/12/2017
 *
 * @author Davit Harutyunyan
 */
public abstract class AbstractMandrillEmailApiCommunicatorModel extends AbstractEmailNotificationApiCommunicatorModel {

    private static final long serialVersionUID = 1713115763225923897L;

    /* Constructors */
    public AbstractMandrillEmailApiCommunicatorModel() {
    }


    /* Equals, HashCode and ToString */
    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AbstractMandrillEmailApiCommunicatorModel)) {
            return false;
        }
        final EqualsBuilder builder = new EqualsBuilder();
        return builder.build();
    }

    @Override
    public int hashCode() {
        final HashCodeBuilder builder = new HashCodeBuilder();
        return builder.build();
    }

    @Override
    public String toString() {
        final ToStringBuilder builder = new ToStringBuilder(this);
        return builder.build();
    }
}
