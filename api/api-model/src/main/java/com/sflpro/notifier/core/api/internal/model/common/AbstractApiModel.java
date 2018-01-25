package com.sflpro.notifier.core.api.internal.model.common;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.joda.time.MutableDateTime;

import java.io.Serializable;
import java.util.Date;

/**
 * User: Ruben Dilanyan
 * Company: SFL LLC
 * Date: 7/29/15
 * Time: 9:58 PM
 */
public abstract class AbstractApiModel implements Serializable {

    private static final long serialVersionUID = -897786952184615806L;

    /* Constructors */
    public AbstractApiModel() {
    }

    /* Static utility methods */
    public static Date cloneDateIfNotNull(final Date date) {
        if (date == null) {
            return null;
        }
        return (Date) date.clone();
    }

    public static Date cloneDateIfNotNullAndStripOffMillisOfSecond(final Date date) {
        if (date == null) {
            return null;
        }
        final MutableDateTime mutableDateTime = new MutableDateTime(date);
        mutableDateTime.setMillisOfSecond(0);
        return mutableDateTime.toDate();
    }

    /* Equals, HashCode and ToString */
    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AbstractApiModel)) {
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
