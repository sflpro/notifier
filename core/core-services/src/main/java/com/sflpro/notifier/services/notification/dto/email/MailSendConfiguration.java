package com.sflpro.notifier.services.notification.dto.email;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.io.Serializable;

/**
 * User: Ruben Dilanyan
 * Company: SFL LLC
 * Date: 1/11/16
 * Time: 10:56 AM
 */
public class MailSendConfiguration implements Serializable {

    /* Properties */
    private String from;

    private String to;

    private String content;

    private String subject;

    /* Constructors */
    public MailSendConfiguration() {
        super();
    }

    /* Properties getters and setters */
    public String getFrom() {
        return from;
    }

    public void setFrom(final String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(final String to) {
        this.to = to;
    }

    public String getContent() {
        return content;
    }

    public void setContent(final String content) {
        this.content = content;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(final String subject) {
        this.subject = subject;
    }

    /* Equals, HashCode and ToString */
    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof MailSendConfiguration)) {
            return false;
        }
        final MailSendConfiguration that = (MailSendConfiguration) o;
        final EqualsBuilder builder = new EqualsBuilder();
        builder.appendSuper(super.equals(that));
        builder.append(this.getFrom(), that.getFrom());
        builder.append(this.getTo(), that.getTo());
        builder.append(this.getContent(), that.getContent());
        builder.append(this.getSubject(), that.getSubject());
        return builder.isEquals();
    }

    @Override
    public int hashCode() {
        final HashCodeBuilder builder = new HashCodeBuilder();
        builder.appendSuper(super.hashCode());
        builder.append(this.getFrom());
        builder.append(this.getTo());
        builder.append(this.getContent());
        builder.append(this.getSubject());
        return builder.build();
    }


    @Override
    public String toString() {
        final ToStringBuilder builder = new ToStringBuilder(this);
        builder.appendSuper(super.toString());
        builder.append("from", this.getFrom());
        builder.append("to", this.getTo());
        builder.append("content", this.getContent());
        builder.append("subject", this.getSubject());
        return builder.build();
    }
}
