package com.sflpro.notifier.email;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * Created by Hayk Mkrtchyan.
 * Date: 6/19/19
 * Time: 10:43 AM
 */
abstract class AbstractEmailMessage implements EmailMessage {

    private final String from;
    private final String to;

    AbstractEmailMessage(final String from, final String to) {
        this.from = from;
        this.to = to;
    }


    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AbstractEmailMessage)) {
            return false;
        }
        final AbstractEmailMessage that = (AbstractEmailMessage) o;
        return new EqualsBuilder()
                .append(from, that.from)
                .append(to, that.to)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(from)
                .append(to)
                .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("from", from)
                .append("to", to)
                .toString();
    }

    @Override
    public String from() {
        return from;
    }

    @Override
    public String to() {
        return to;
    }
}
