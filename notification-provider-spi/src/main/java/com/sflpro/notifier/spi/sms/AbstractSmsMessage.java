package com.sflpro.notifier.spi.sms;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * Created by Hayk Mkrtchyan.
 * Date: 6/20/19
 * Time: 3:04 PM
 */
abstract class AbstractSmsMessage implements SmsMessage {

    private final long internalId;
    private final String recipientNumber;
    private final String senderNumber;

    AbstractSmsMessage(final long internalId, final String senderNumber, final String recipientNumber) {
        this.internalId = internalId;
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
                .append(internalId, that.internalId)
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
                .append("internalId", internalId)
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

    @Override
    public long internalId() {
        return internalId;
    }
}
