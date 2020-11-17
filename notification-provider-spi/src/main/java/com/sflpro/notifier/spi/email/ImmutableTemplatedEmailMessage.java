package com.sflpro.notifier.spi.email;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

/**
 * Created by Hayk Mkrtchyan.
 * Date: 6/19/19
 * Time: 10:42 AM
 */
final class ImmutableTemplatedEmailMessage extends AbstractEmailMessage implements TemplatedEmailMessage {

    private final String templateId;
    private final Map<String, ?> variables;
    private final Locale locale;

    ImmutableTemplatedEmailMessage(
            final String from,
            final String to,
            final Set<String> replyTo,
            final String templateId,
            final String subject,
            final Map<String, ?> variables,
            final Locale locale,
            final Set<SpiEmailNotificationFileAttachment> fileAttachments
    ) {
        super(from, to, replyTo, subject, fileAttachments);
        this.templateId = templateId;
        this.variables = variables;
        this.locale = locale;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TemplatedEmailMessage)) {
            return false;
        }
        final TemplatedEmailMessage that = (TemplatedEmailMessage) o;
        return new EqualsBuilder()
                .appendSuper(super.equals(o))
                .append(templateId, that.templateId())
                .append(variables, that.variables())
                .append(locale, that.locale())
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .appendSuper(super.hashCode())
                .append(templateId)
                .append(variables)
                .append(locale)
                .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("templateId", templateId)
                .append("variables", variables)
                .append("locale", locale)
                .toString();
    }

    @Override
    public String templateId() {
        return templateId;
    }

    @Override
    public Map<String, ?> variables() {
        return variables;
    }

    @Override
    public Optional<String> subject() {
        return Optional.ofNullable(getSubject());
    }

    @Override
    public Optional<Locale> locale() {
        return Optional.ofNullable(locale);
    }

}
