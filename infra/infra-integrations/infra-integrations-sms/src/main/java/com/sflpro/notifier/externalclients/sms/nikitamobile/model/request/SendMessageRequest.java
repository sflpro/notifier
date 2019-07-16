package com.sflpro.notifier.externalclients.sms.nikitamobile.model.request;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.xml.bind.annotation.*;

/**
 * User: Mher Sargsyan
 * Company: SFL LLC
 * Date: 4/9/15
 * Time: 5:29 PM
 */

@XmlRootElement(name = "bulk-request")
@XmlAccessorType(XmlAccessType.FIELD)
public class SendMessageRequest {

    @XmlAttribute(name = "login")
    private String login;

    @XmlAttribute(name = "password")
    private String password;

    @XmlAttribute(name = "delivery-notification-requested")
    private boolean requested = true;

    @XmlAttribute(name = "version")
    private String version;

    @XmlAttribute(name = "ref-id")
    private String refId;

    @XmlElement(name = "message")
    private Message message;

    public SendMessageRequest() {
        super();
    }

    public SendMessageRequest(final Message message) {
        this.message = message;
    }

    public Message getMessage() {
        return message;
    }

    public void setMessage(final Message message) {
        this.message = message;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isRequested() {
        return requested;
    }

    public void setRequested(boolean requested) {
        this.requested = requested;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getRefId() {
        return refId;
    }

    public void setRefId(String refId) {
        this.refId = refId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SendMessageRequest)) {
            return false;
        }
        final SendMessageRequest that = (SendMessageRequest) o;
        return new EqualsBuilder()
                .append(requested, that.requested)
                .append(login, that.login)
                .append(password, that.password)
                .append(version, that.version)
                .append(refId, that.refId)
                .append(message, that.message)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(login)
                .append(password)
                .append(requested)
                .append(version)
                .append(refId)
                .append(message)
                .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("login", login)
                .append("password", password)
                .append("requested", requested)
                .append("version", version)
                .append("refId", refId)
                .append("message", message)
                .toString();
    }
}
