package com.sflpro.notifier.spi.email;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * Created by Hayk Mkrtchyan.
 * Date: 6/19/19
 * Time: 6:10 PM
 */
final class ImmutableEmailTemplateContent implements EmailTemplateContent {

    private final String subject;
    private final String body;

    ImmutableEmailTemplateContent(final String subject, final String body) {
        this.subject = subject;
        this.body = body;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ImmutableEmailTemplateContent)) {
            return false;
        }
        final ImmutableEmailTemplateContent that = (ImmutableEmailTemplateContent) o;
        return new EqualsBuilder()
                .append(subject, that.subject)
                .append(body, that.body)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
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

    @Override
    public String subject() {
        return subject;
    }

    @Override
    public String body() {
        return body;
    }
}
