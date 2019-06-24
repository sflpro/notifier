package com.sflpro.notifier.externalclients.sms.twillio.model.response;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.springframework.util.Assert;

/**
 * User: Mher Sargsyan
 * Company: SFL LLC
 * Date: 4/9/15
 * Time: 5:29 PM
 */
public class SendMessageResponse{

    private static final long serialVersionUID = 9055257034174161692L;

    /* Properties */
    private final String sid;

    private final String recipientNumber;

    private final String messageBody;

    /* Constructors */
    public SendMessageResponse(final String sid, final String recipientNumber, final String messageBody) {
        Assert.hasText(sid, "Message sid should not be empty");
        Assert.hasText(recipientNumber, "Message recipient phone number should not be empty");
        Assert.hasText(messageBody, "Message body should not be empty");
        this.sid = sid;
        this.recipientNumber = recipientNumber;
        this.messageBody = messageBody;
    }

    /* Getters and setters */
    public String getRecipientNumber() {
        return recipientNumber;
    }

    public String getMessageBody() {
        return messageBody;
    }

    public String getSid() {
        return sid;
    }

    /* Equals, HashCode and ToString */
    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SendMessageResponse)) {
            return false;
        }
        final SendMessageResponse that = (SendMessageResponse) o;
        final EqualsBuilder builder = new EqualsBuilder();
        builder.appendSuper(super.equals(that));
        builder.append(getSid(), that.getSid());
        builder.append(getMessageBody(), that.getMessageBody());
        builder.append(getRecipientNumber(), that.getRecipientNumber());
        return builder.build();
    }

    @Override
    public int hashCode() {
        final HashCodeBuilder builder = new HashCodeBuilder();
        builder.appendSuper(super.hashCode());
        builder.append(getSid());
        builder.append(getMessageBody());
        builder.append(getRecipientNumber());
        return builder.build();
    }

    @Override
    public String toString() {
        final ToStringBuilder builder = new ToStringBuilder(this);
        builder.appendSuper(super.toString());
        builder.append("sid", getSid());
        builder.append("recipientNumber", getRecipientNumber());
        builder.append("messageBody", getMessageBody());
        return builder.build();
    }
}
