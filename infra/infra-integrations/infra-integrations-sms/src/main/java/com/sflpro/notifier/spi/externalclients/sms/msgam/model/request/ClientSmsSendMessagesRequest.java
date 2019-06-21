package com.sflpro.notifier.externalclients.sms.msgam.model.request;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.xml.bind.annotation.*;

/**
 * User: Tigran Tserunyan
 * Company: SFL LLC
 * Date: 18/05/2017
 * Time: 5:08 PM
 */
@XmlRootElement(name = "xml_request")
@XmlAccessorType(XmlAccessType.FIELD)
public class ClientSmsSendMessagesRequest {

    @XmlAttribute(name = "name")
    private String name;

    @XmlElement(name = "xml_user")
    private User user;

    @XmlElement(name = "sms")
    private Message message;

    public ClientSmsSendMessagesRequest() {
        super();
    }

    public ClientSmsSendMessagesRequest(
            final String name,
            final long messageId,
            final String senderNumber,
            final String senderLogin,
            final String senderPass,
            final String recipientNumber,
            final String messageBody) {
        this.name = name;
        this.user = new User(senderLogin, senderPass);
        this.message = new Message(messageId,
                senderNumber,
                recipientNumber,
                messageBody);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ClientSmsSendMessagesRequest)) {
            return false;
        }
        final ClientSmsSendMessagesRequest that = (ClientSmsSendMessagesRequest) o;
        return new EqualsBuilder()
                .appendSuper(super.equals(o))
                .append(user, that.user)
                .append(name, that.name)
                .append(message, that.message)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .appendSuper(super.hashCode())
                .append(user)
                .append(name)
                .append(message)
                .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("user", user)
                .append("name", name)
                .append("message", message)
                .toString();
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
