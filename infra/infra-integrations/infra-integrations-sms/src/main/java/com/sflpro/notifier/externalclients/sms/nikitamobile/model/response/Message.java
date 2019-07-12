package com.sflpro.notifier.externalclients.sms.nikitamobile.model.response;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;

/**
 * Created by Hayk Mkrtchyan.
 * Date: 6/24/19
 * Time: 10:40 AM
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class Message {

    @XmlAttribute(name = "id")
    private Long id;

    public Message() {
        super();
    }

    public Message(final Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {return true;}
        if (!(o instanceof Message)) {return false;}
        final Message message = (Message) o;
        return new EqualsBuilder()
                .append(id, message.id)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(id)
                .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("id", id)
                .toString();
    }
}
