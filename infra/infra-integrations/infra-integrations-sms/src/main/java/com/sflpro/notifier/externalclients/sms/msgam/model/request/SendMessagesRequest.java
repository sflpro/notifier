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

    private String recipientNumber;

    private String messageBody;

    private long messageId;

    private String senderNumber;

    public SendMessagesRequest(final long messageId, final String senderNumber, final String recipientNumber, final String messageBody) {
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
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SendMessagesRequest)) {
            return false;
        }
        final SendMessagesRequest request = (SendMessagesRequest) o;
        return new EqualsBuilder()
                .append(messageId, request.messageId)
                .append(recipientNumber, request.recipientNumber)
                .append(messageBody, request.messageBody)
                .append(senderNumber, request.senderNumber)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(recipientNumber)
                .append(messageBody)
                .append(messageId)
                .append(senderNumber)
                .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("recipientNumber", recipientNumber)
                .append("messageBody", messageBody)
                .append("messageId", messageId)
                .append("senderNumber", senderNumber)
                .toString();
    }
}
