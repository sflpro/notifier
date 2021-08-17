package com.sflpro.notifier.externalclients.sms.webhook.request;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * User: Armen Nazaretyan
 * Company: SFL LLC
 * Date: 6/24/21
 * Time: 5:09 PM
 */
public class WebhookSmsSendMessagesRequest {

    private long messageId;
    private String senderNumber;
    private String recipientNumber;
    private String messageBody;

    public WebhookSmsSendMessagesRequest(final long messageId, final String senderNumber, final String recipientNumber, final String messageBody) {
        this.messageId = messageId;
        this.senderNumber = senderNumber;
        this.recipientNumber = recipientNumber;
        this.messageBody = messageBody;
    }

    public long getMessageId() {
        return messageId;
    }

    public void setMessageId(final long messageId) {
        this.messageId = messageId;
    }

    public String getSenderNumber() {
        return senderNumber;
    }

    public void setSenderNumber(final String senderNumber) {
        this.senderNumber = senderNumber;
    }

    public String getRecipientNumber() {
        return recipientNumber;
    }

    public void setRecipientNumber(final String recipientNumber) {
        this.recipientNumber = recipientNumber;
    }

    public String getMessageBody() {
        return messageBody;
    }

    public void setMessageBody(final String messageBody) {
        this.messageBody = messageBody;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        final WebhookSmsSendMessagesRequest that = (WebhookSmsSendMessagesRequest) o;
        return new EqualsBuilder()
                .append(messageId, that.messageId)
                .append(senderNumber, that.senderNumber)
                .append(recipientNumber, that.recipientNumber)
                .append(messageBody, that.messageBody)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(messageId)
                .append(senderNumber)
                .append(recipientNumber)
                .append(messageBody)
                .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("messageId", messageId)
                .append("senderNumber", senderNumber)
                .append("recipientNumber", recipientNumber)
                .append("messageBody", messageBody)
                .toString();
    }
}
