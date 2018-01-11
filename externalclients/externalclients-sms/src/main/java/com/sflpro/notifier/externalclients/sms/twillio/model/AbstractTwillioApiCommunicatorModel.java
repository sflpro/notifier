package com.sflpro.notifier.externalclients.sms.twillio.model;

import com.sflpro.notifier.externalclients.sms.common.model.AbstractSmsApiCommunicatorModel;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * User: Mher Sargsyan
 * Company: SFL LLC
 * Date: 4/9/15
 * Time: 3:32 PM
 */
public abstract class AbstractTwillioApiCommunicatorModel extends AbstractSmsApiCommunicatorModel {

    private static final long serialVersionUID = 1713115763225923897L;

    /* Constructors */
    public AbstractTwillioApiCommunicatorModel() {
    }


    /* Equals, HashCode and ToString */
    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AbstractTwillioApiCommunicatorModel)) {
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
