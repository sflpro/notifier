package com.sflpro.notifier.spi.email;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * Created by Hayk Mkrtchyan.
 * Date: 6/19/19
 * Time: 10:31 AM
 */
final class ImmutableSimpleEmailMessage extends AbstractEmailMessage implements SimpleEmailMessage {
    private final String body;

    ImmutableSimpleEmailMessage(final String from, final String to, final String subject, final String body) {
        super(from, to, subject);
        this.body = body;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SimpleEmailMessage)) {
            return false;
        }
        final SimpleEmailMessage that = (SimpleEmailMessage) o;
        return new EqualsBuilder()
                .appendSuper(super.equals(o))
                .append(body, that.body())
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .appendSuper(super.hashCode())
                .append(body)
                .toHashCode();
    }

    @Override
    public String body() {
        return body;
    }

    @Override
    public String subject() {
        return super.getSubject();
    }
}
