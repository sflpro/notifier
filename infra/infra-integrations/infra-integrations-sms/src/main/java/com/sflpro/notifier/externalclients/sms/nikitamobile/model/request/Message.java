package com.sflpro.notifier.externalclients.sms.nikitamobile.model.request;

import com.sflpro.notifier.externalclients.sms.nikitamobile.model.Content;
import com.sflpro.notifier.externalclients.sms.nikitamobile.model.NikitamobileDateTimeAdapter;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.time.LocalDateTime;

/**
 * Created by Hayk Mkrtchyan.
 * Date: 6/24/19
 * Time: 10:40 AM
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class Message {

    private static final int HIGHEST_PRIORITY = 9;
    private static final int DEFAULT_MESSAGE_COUNT = 1;

    @XmlAttribute(name = "id")
    private Long id;

    @XmlAttribute(name = "msisdn")
    private String senderNumber;

    @XmlAttribute(name = "service-number")
    private String recipientNumber;

    @XmlAttribute(name = "submit-date")
    @XmlJavaTypeAdapter(NikitamobileDateTimeAdapter.class)
    private LocalDateTime submitDate;

    @XmlAttribute(name = "priority")
    private int priority;

    @XmlAttribute(name = "operator_id")
    private String operatorId;

    @XmlAttribute(name = "operator")
    private String operator;

    @XmlAttribute(name = "message-count")
    private int messageCount;

    @XmlElement
    private Content content;

    public Message() {
        super();
    }

    public Message(final Long id,
                   final String senderNumber,
                   final String recipientNumber,
                   final Content content) {
        this.id = id;
        this.senderNumber = senderNumber;
        this.recipientNumber = recipientNumber;
        this.submitDate = LocalDateTime.now();
        this.priority = HIGHEST_PRIORITY;
        this.content = content;
        this.messageCount = DEFAULT_MESSAGE_COUNT;
    }


    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public Content getContent() {
        return content;
    }

    public void setContent(final Content content) {
        this.content = content;
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

    public LocalDateTime getSubmitDate() {
        return submitDate;
    }

    public void setSubmitDate(final LocalDateTime submitDate) {
        this.submitDate = submitDate;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(final int priority) {
        this.priority = priority;
    }

    public String getOperatorId() {
        return operatorId;
    }

    public void setOperatorId(final String operatorId) {
        this.operatorId = operatorId;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(final String operator) {
        this.operator = operator;
    }

    public int getMessageCount() {
        return messageCount;
    }

    public void setMessageCount(final int messageCount) {
        this.messageCount = messageCount;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Message)) {
            return false;
        }
        final Message message = (Message) o;
        return new EqualsBuilder()
                .append(priority, message.priority)
                .append(messageCount, message.messageCount)
                .append(id, message.id)
                .append(senderNumber, message.senderNumber)
                .append(recipientNumber, message.recipientNumber)
                .append(submitDate, message.submitDate)
                .append(operatorId, message.operatorId)
                .append(operator, message.operator)
                .append(content, message.content)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(id)
                .append(senderNumber)
                .append(recipientNumber)
                .append(submitDate)
                .append(priority)
                .append(operatorId)
                .append(operator)
                .append(messageCount)
                .append(content)
                .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("id", id)
                .append("senderNumber", senderNumber)
                .append("recipientNumber", recipientNumber)
                .append("submitDate", submitDate)
                .append("priority", priority)
                .append("operatorId", operatorId)
                .append("operator", operator)
                .append("messageCount", messageCount)
                .append("content", content)
                .toString();
    }
}
