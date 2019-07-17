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

    private static final int HIGHEST_PRIORITY = 1;

    @XmlAttribute(name = "id")
    private Long id;

    @XmlAttribute(name = "msisdn")
    private String recipientNumber;

    @XmlAttribute(name = "service-number")
    private String senderNumber;

    @XmlAttribute(name = "defer-date")
    @XmlJavaTypeAdapter(NikitamobileDateTimeAdapter.class)
    private LocalDateTime deferDate;

    @XmlAttribute(name = "validity-period")
    private int validityPeriod;

    @XmlAttribute(name = "priority")
    private int priority;

    @XmlElement(name = "content")
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
        this.deferDate = LocalDateTime.now();
        this.priority = HIGHEST_PRIORITY;
        this.content = content;
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

    public LocalDateTime getDeferDate() {
        return deferDate;
    }

    public void setDeferDate(LocalDateTime deferDate) {
        this.deferDate = deferDate;
    }

    public int getValidityPeriod() {
        return validityPeriod;
    }

    public void setValidityPeriod(int validityPeriod) {
        this.validityPeriod = validityPeriod;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(final int priority) {
        this.priority = priority;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Message)) {
            return false;
        }
        final Message message = (Message) o;
        return new EqualsBuilder()
                .append(validityPeriod, message.validityPeriod)
                .append(priority, message.priority)
                .append(id, message.id)
                .append(recipientNumber, message.recipientNumber)
                .append(senderNumber, message.senderNumber)
                .append(deferDate, message.deferDate)
                .append(content, message.content)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(id)
                .append(recipientNumber)
                .append(senderNumber)
                .append(deferDate)
                .append(validityPeriod)
                .append(priority)
                .append(content)
                .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("id", id)
                .append("recipientNumber", recipientNumber)
                .append("senderNumber", senderNumber)
                .append("deferDate", deferDate)
                .append("validityPeriod", validityPeriod)
                .append("priority", priority)
                .append("content", content)
                .toString();
    }
}
