package com.sflpro.notifier.sms;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * Created by Hayk Mkrtchyan.
 * Date: 6/18/19
 * Time: 3:53 PM
 */

final class ImmutableSmsMessage implements SmsMessage {

    private final String senderNumber;
    private final String recipientNumber;
    private final String messageBody;

    ImmutableSmsMessage(final String senderNumber, final String recipientNumber, final String messageBody) {
        this.senderNumber = senderNumber;
        this.recipientNumber = recipientNumber;
        this.messageBody = messageBody;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SmsMessage)) {
            return false;
        }
        final SmsMessage that = (SmsMessage) o;
        return new EqualsBuilder()
                .append(senderNumber, that.senderNumber())
                .append(recipientNumber, that.recipientNumber())
                .append(messageBody, that.messageBody())
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(senderNumber)
                .append(recipientNumber)
                .append(messageBody)
                .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("senderNumber", senderNumber)
                .append("recipientNumber", recipientNumber)
                .append("messageBody", messageBody)
                .toString();
    }

    @Override
    public String senderNumber() {
        return senderNumber;
    }

    @Override
    public String recipientNumber() {
        return recipientNumber;
    }

    @Override
    public String messageBody() {
        return messageBody;
    }
}
