package com.sfl.nms.services.util.mutable;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.io.Serializable;

/**
 * User: Ruben Dilanyan
 * Company: SFL LLC
 * Date: 12/27/14
 * Time: 4:10 PM
 */
public class MutableHolder<T> implements Serializable {
    private static final long serialVersionUID = 1039650287502723928L;

    /* Properties */
    private T value;

    /* Constructors */
    public MutableHolder(final T value) {
        this.value = value;
    }

    /* Properties getters and setters */
    public T getValue() {
        return value;
    }

    public void setValue(final T value) {
        this.value = value;
    }

    /* Equals, HashCode and ToString */
    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof MutableHolder)) {
            return false;
        }
        final MutableHolder model = (MutableHolder) o;
        final EqualsBuilder builder = new EqualsBuilder();
        builder.append(getValue(), model.getValue());
        return builder.isEquals();
    }

    @Override
    public int hashCode() {
        final HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(getValue());
        return builder.build();
    }

    @Override
    public String toString() {
        final ToStringBuilder builder = new ToStringBuilder(this);
        builder.append("value", getValue());
        return builder.build();
    }
}
