package com.sflpro.notifier.externalclients.sms.twillio.model.request;

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
public class SendMessageRequest{

    /* Properties */
    private final String senderNumber;

    private final String recipientNumber;

    private final String messageBody;

    /* Constructors */
    public SendMessageRequest(final String senderNumber, final String recipientNumber, final String messageBody) {
        Assert.hasText(senderNumber, "Message sender phone number should not be empty");
        Assert.hasText(recipientNumber, "Message recipient phone number should not be empty");
        Assert.hasText(messageBody, "Message body should not be empty");
        this.senderNumber = senderNumber;
        this.recipientNumber = recipientNumber;
        this.messageBody = messageBody;
    }

    /* Getters and setters */
    public String getSenderNumber() {
        return senderNumber;
    }

    public String getRecipientNumber() {
        return recipientNumber;
    }

    public String getMessageBody() {
        return messageBody;
    }

    /* Equals, HashCode and ToString */
    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SendMessageRequest)) {
            return false;
        }
        SendMessageRequest that = (SendMessageRequest) o;
        final EqualsBuilder builder = new EqualsBuilder();
        builder.append(getSenderNumber(), that.getSenderNumber());
        builder.append(getMessageBody(), that.getMessageBody());
        builder.append(getRecipientNumber(), that.getRecipientNumber());
        return builder.build();
    }

    @Override
    public int hashCode() {
        final HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(getSenderNumber());
        builder.append(getMessageBody());
        builder.append(getRecipientNumber());
        return builder.build();
    }

    @Override
    public String toString() {
        final ToStringBuilder builder = new ToStringBuilder(this);
        builder.appendSuper(super.toString());
        builder.append("sender", getSenderNumber());
        builder.append("recipientNumber", getRecipientNumber());
        builder.append("messageBody", getMessageBody());
        return builder.build();
    }
}
