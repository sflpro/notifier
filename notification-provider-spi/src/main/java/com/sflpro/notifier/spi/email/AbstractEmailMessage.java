package com.sflpro.notifier.spi.email;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.Set;

/**
 * Created by Hayk Mkrtchyan.
 * Date: 6/19/19
 * Time: 10:43 AM
 */
abstract class AbstractEmailMessage implements EmailMessage {

    private final String from;
    private final String to;
    private final Set<String> replyTo;
    private final String subject;
    private final Set<SpiEmailNotificationFileAttachment> fileAttachments;

    AbstractEmailMessage(final String from, final String to, final Set<String> replyTo) {
        this(from, to, replyTo, null, null);
    }


    AbstractEmailMessage(
            final String from,
            final String to,
            final Set<String> replyTo,
            final String subject,
            final Set<SpiEmailNotificationFileAttachment> fileAttachments
    ) {
        this.from = from;
        this.to = to;
        this.replyTo = replyTo;
        this.subject = subject;
        this.fileAttachments = fileAttachments;
    }


    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AbstractEmailMessage)) {
            return false;
        }
        final AbstractEmailMessage that = (AbstractEmailMessage) o;
        return new EqualsBuilder()
                .append(from, that.from)
                .append(to, that.to)
                .append(subject, that.subject)
                .append(fileAttachments, that.fileAttachments)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(from)
                .append(to)
                .append(fileAttachments)
                .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("from", from)
                .append("to", to)
                .append("subject", subject)
                .append("fileAttachments", fileAttachments)
                .toString();
    }

    @Override
    public String from() {
        return from;
    }

    @Override
    public String to() {
        return to;
    }

    @Override
    public Set<String> replyTo() {
        return replyTo;
    }

    @Override
    public Set<SpiEmailNotificationFileAttachment> fileAttachments() {
        return fileAttachments;
    }

    public String getSubject() {
        return subject;
    }
}
