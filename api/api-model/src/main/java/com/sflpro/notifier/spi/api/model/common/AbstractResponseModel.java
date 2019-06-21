package com.sflpro.notifier.api.model.common;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * User: Ruben Dilanyan
 * Company: SFL LLC
 * Date: 7/17/15
 * Time: 3:25 PM
 */
public abstract class AbstractResponseModel extends AbstractApiModel {

    private static final long serialVersionUID = 3348299189734274222L;

    /* Constructors */
    public AbstractResponseModel() {
    }

    /* Equals, HashCode and ToString */
    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AbstractResponseModel)) {
            return false;
        }
        final EqualsBuilder builder = new EqualsBuilder();
        return builder.isEquals();
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
