package com.sflpro.notifier.sms;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * Created by Hayk Mkrtchyan.
 * Date: 6/18/19
 * Time: 4:02 PM
 */
final class ImmutableSmsMessageSendingResult implements SmsMessageSendingResult {

    private final String sid;

    ImmutableSmsMessageSendingResult(final String sid) {
        this.sid = sid;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }

        if (!(o instanceof SmsMessageSendingResult)) {
            return false;
        }
        final SmsMessageSendingResult that = (SmsMessageSendingResult) o;
        return new EqualsBuilder()
                .append(sid, that.sid())
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(sid)
                .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("sid", sid)
                .toString();
    }

    @Override
    public String sid() {
        return sid;
    }
}
