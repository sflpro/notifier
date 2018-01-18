package com.sflpro.notifier.core.api.internal.model.common;

import com.sflpro.notifier.core.api.internal.model.common.request.ValidatableRequest;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * User: Ruben Dilanyan
 * Company: SFL LLC
 * Date: 7/17/15
 * Time: 6:34 PM
 */
public abstract class AbstractRequestModel extends AbstractApiModel implements ValidatableRequest {

    private static final long serialVersionUID = -2567218055455706524L;

    /* Constructors */
    public AbstractRequestModel() {
    }

    /* Equals, HashCode and ToString */
    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AbstractRequestModel)) {
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
