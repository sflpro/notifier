package com.sflpro.notifier.externalclients.sms.nikitamobile.model.response;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Company: SFL LLC
 * Date: 4/9/15
 * Time: 5:29 PM
 */
@XmlRootElement(name = "sms-response")
@XmlAccessorType(XmlAccessType.FIELD)
public class SendMessageResponse   {

    /* Properties */
    @XmlElement
    private Message message;

    public SendMessageResponse() {
        super();
    }

    public SendMessageResponse(final Message message) {
        this.message = message;
    }

    public Message getMessage() {
        return message;
    }

    public void setMessage(final Message message) {
        this.message = message;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o){ return true;}
        if (!(o instanceof SendMessageResponse)) {return false;}
        final SendMessageResponse that = (SendMessageResponse) o;

        return new EqualsBuilder()
                .append(message, that.message)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(message)
                .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("message", message)
                .toString();
    }
}
