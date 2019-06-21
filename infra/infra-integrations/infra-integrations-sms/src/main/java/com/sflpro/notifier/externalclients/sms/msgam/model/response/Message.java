package com.sflpro.notifier.externalclients.sms.msgam.model.response;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import java.math.BigInteger;

/**
 * User: Tigran Tserunyan
 * Company: SFL LLC
 * Date: 18/05/2017
 * Time: 5:46 PM
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class Message {

    @XmlAttribute(name = "sms_id")
    private BigInteger id;

    @XmlAttribute(name = "number")
    private String phoneNumber;

    @XmlAttribute(name = "push_id")
    private BigInteger sourceNumber;

    @XmlAttribute(name = "sms_count")
    private int count;

    public Message() {
    }

    public BigInteger getId() {
        return id;
    }

    public void setId(BigInteger id) {
        this.id = id;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public BigInteger getSourceNumber() {
        return sourceNumber;
    }

    public void setSourceNumber(BigInteger sourceNumber) {
        this.sourceNumber = sourceNumber;
    }

    public int getCount() {
        return count;
    }

    public void setCount(final int count) {
        this.count = count;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Message)) {
            return false;
        }
        final Message that = (Message) o;
        return new EqualsBuilder()
                .append(id, that.id)
                .append(phoneNumber, that.phoneNumber)
                .append(sourceNumber, that.sourceNumber)
                .append(count, that.count)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(id)
                .append(phoneNumber)
                .append(sourceNumber)
                .append(count)
                .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("id", id)
                .append("phoneNumber", phoneNumber)
                .append("sourceNumber", sourceNumber)
                .append("count", count)
                .toString();
    }
}
