package com.sflpro.notifier.spi.template;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * Created by Hayk Mkrtchyan.
 * Date: 6/19/19
 * Time: 6:10 PM
 */
final class ImmutableTemplateContent implements TemplateContent {

    private final String subject;

    private final String body;

    ImmutableTemplateContent(final String subject, final String body) {
        this.subject = subject;
        this.body = body;
    }

    @Override
    public String subject() {
        return subject;
    }

    @Override
    public String body() {
        return body;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj == this) {
            return true;
        }
        if (obj.getClass() != getClass()) {
            return false;
        }
        ImmutableTemplateContent rhs = (ImmutableTemplateContent) obj;
        return new EqualsBuilder()
                .append(this.subject, rhs.subject)
                .append(this.body, rhs.body)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .append(subject)
                .append(body)
                .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("subject", subject)
                .append("body", body)
                .toString();
    }
}
