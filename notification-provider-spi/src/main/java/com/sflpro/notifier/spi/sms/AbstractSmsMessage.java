package com.sflpro.notifier.sms;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * Created by Hayk Mkrtchyan.
 * Date: 6/20/19
 * Time: 3:04 PM
 */
abstract class AbstractSmsMessage implements SmsMessage {

    private final String recipientNumber;
    private final String senderNumber;

    AbstractSmsMessage(final String senderNumber,final String recipientNumber) {
        this.senderNumber = senderNumber;
        this.recipientNumber = recipientNumber;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AbstractSmsMessage)) {
            return false;
        }
        final AbstractSmsMessage that = (AbstractSmsMessage) o;
        return new EqualsBuilder()
                .append(recipientNumber, that.recipientNumber)
                .append(senderNumber, that.senderNumber)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(recipientNumber)
                .append(senderNumber)
                .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("recipientNumber", recipientNumber)
                .append("sender", senderNumber)
                .toString();
    }

    @Override
    public String sender() {
        return senderNumber;
    }

    @Override
    public String recipientNumber() {
        return recipientNumber;
    }
}
