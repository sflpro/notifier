package com.sflpro.notifier.externalclients.sms.msgam.model.request;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlValue;

/**
 * Company: SFL LLC
 * Date: 18/05/2017
 * Time: 5:46 PM
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class Message {

    @XmlAttribute(name = "sms_id")
    private long id;

    @XmlAttribute(name = "number")
    private String phoneNumber;

    @XmlAttribute(name = "source_number")
    private String sourceNumber;

    @XmlValue
    private String text;

    public Message() {
        super();
    }

    public Message(final long id, final String senderNumber, final String recipientNumber, final String messageBody) {
        this.id = id;
        sourceNumber = senderNumber;
        text = messageBody;
        phoneNumber = recipientNumber;
    }

    public long getId() {
        return id;
    }

    public void setId(final long id) {
        this.id = id;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getSourceNumber() {
        return sourceNumber;
    }

    public void setSourceNumber(String sourceNumber) {
        this.sourceNumber = sourceNumber;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (!(o instanceof Message)) return false;

        Message that = (Message) o;

        return new EqualsBuilder()
                .append(id, that.id)
                .append(phoneNumber, that.phoneNumber)
                .append(sourceNumber, that.sourceNumber)
                .append(text, that.text)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(id)
                .append(phoneNumber)
                .append(sourceNumber)
                .append(text)
                .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("id", id)
                .append("phoneNumber", phoneNumber)
                .append("sourceNumber", sourceNumber)
                .append("text", text)
                .toString();
    }
}
