package com.sflpro.notifier.spi.sms;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.Map;

/**
 * Created by Hayk Mkrtchyan.
 * Date: 6/20/19
 * Time: 3:11 PM
 */
final class ImmutableTemplatedSmsMessage extends AbstractSmsMessage implements TemplatedSmsMessage {


    private final String templateId;
    private final Map<String, ?> variables;

    ImmutableTemplatedSmsMessage(final String senderNumber,
                                 final String recipientNumber,
                                 final String templateId,
                                 final Map<String, ?> variables) {
        super(senderNumber, recipientNumber);
        this.templateId = templateId;
        this.variables = variables;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TemplatedSmsMessage)) {
            return false;
        }
        final TemplatedSmsMessage that = (TemplatedSmsMessage) o;
        return new EqualsBuilder()
                .appendSuper(super.equals(o))
                .append(templateId, that.templateId())
                .append(variables, that.variables())
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .appendSuper(super.hashCode())
                .append(templateId)
                .append(variables)
                .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("templateId", templateId)
                .append("variables", variables)
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
}
