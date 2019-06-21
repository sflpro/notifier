package com.sflpro.notifier.externalclients.sms.msgam.model.request;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * User: Tigran Tserunyan
 * Company: SFL LLC
 * Date: 18/05/2017
 * Time: 5:08 PM
 */
public class SendMessagesRequest {

    private static final long serialVersionUID = 5623687823133511155L;

    private String recipientNumber;

    private String messageBody;

    private long messageId;

    private String senderNumber;

    public SendMessagesRequest(final long messageId,final String senderNumber, final String recipientNumber, final String messageBody) {
        this.senderNumber = senderNumber;
        this.messageId = messageId;
        this.recipientNumber = recipientNumber;
        this.messageBody = messageBody;
    }

    public long getMessageId() {
        return messageId;
    }

    public void setMessageId(final long messageId) {
        this.messageId = messageId;
    }

    public String getRecipientNumber() {
        return recipientNumber;
    }

    public void setRecipientNumber(String recipientNumber) {
        this.recipientNumber = recipientNumber;
    }

    public String getMessageBody() {
        return messageBody;
    }

    public void setMessageBody(String messageBody) {
        this.messageBody = messageBody;
    }

    public String getSenderNumber() {
        return senderNumber;
    }

    public void setSenderNumber(final String senderNumber) {
        this.senderNumber = senderNumber;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SendMessagesRequest)) {
            return false;
        }
        final SendMessagesRequest that = (SendMessagesRequest) o;
        return new EqualsBuilder()
                .appendSuper(super.equals(o))
                .append(messageId, that.messageId)
                .append(recipientNumber, that.recipientNumber)
                .append(messageBody, that.messageBody)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .appendSuper(super.hashCode())
                .append(messageId)
                .append(recipientNumber)
                .append(messageBody)
                .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("messageId", messageId)
                .append("recipientNumber", recipientNumber)
                .append("messageBody", messageBody)
                .toString();
    }
}
