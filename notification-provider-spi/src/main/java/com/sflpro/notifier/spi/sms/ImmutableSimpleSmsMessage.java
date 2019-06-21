package com.sflpro.notifier.sms;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * Created by Hayk Mkrtchyan.
 * Date: 6/18/19
 * Time: 3:53 PM
 */

final class ImmutableSimpleSmsMessage extends AbstractSmsMessage implements SimpleSmsMessage {

    private final String messageBody;

    ImmutableSimpleSmsMessage(final String senderNumber, final String recipientNumber, final String messageBody) {
        super(senderNumber, recipientNumber);
        this.messageBody = messageBody;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SimpleSmsMessage)) {
            return false;
        }
        final SimpleSmsMessage that = (SimpleSmsMessage) o;
        return new EqualsBuilder()
                .appendSuper(super.equals(o))
                .append(messageBody, that.messageBody())
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .appendSuper(super.hashCode())
                .append(messageBody)
                .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("messageBody", messageBody)
                .toString();
    }

    @Override
    public String messageBody() {
        return messageBody;
    }
}
