package com.sflpro.notifier.spi.push;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * Created by Hayk Mkrtchyan.
 * Date: 7/3/19
 * Time: 10:50 AM
 */
final class ImmutablePushMessageSendingResult implements PushMessageSendingResult {

    private final String messageId;

    ImmutablePushMessageSendingResult(final String messageId) {
        this.messageId = messageId;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o){ return true;}
        if (!(o instanceof ImmutablePushMessageSendingResult)){ return false;}
        final ImmutablePushMessageSendingResult that = (ImmutablePushMessageSendingResult) o;
        return new EqualsBuilder()
                .append(messageId, that.messageId)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(messageId)
                .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("messageId", messageId)
                .toString();
    }

    @Override
    public String messageId() {
        return messageId;
    }
}
