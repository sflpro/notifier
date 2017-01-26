package com.sfl.nms.core.api.internal.model.notification;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sfl.nms.core.api.internal.model.common.AbstractApiModel;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * User: Ruben Dilanyan
 * Company: SFL LLC
 * Date: 1/12/16
 * Time: 7:19 PM
 */
public abstract class NotificationModel extends AbstractApiModel {

    private static final long serialVersionUID = 3288267458302047890L;

    /* Properties */
    @JsonProperty("uuId")
    private String uuId;

    @JsonProperty("subject")
    private String subject;

    @JsonProperty("body")
    private String body;

    @JsonProperty("type")
    private NotificationClientType type;

    @JsonProperty("state")
    private NotificationStateClientType state;

    /* Constructors */
    public NotificationModel() {
    }

    /* Properties getters and setters */
    public String getUuId() {
        return uuId;
    }

    public void setUuId(final String uuId) {
        this.uuId = uuId;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(final String subject) {
        this.subject = subject;
    }

    public String getBody() {
        return body;
    }

    public void setBody(final String body) {
        this.body = body;
    }

    public NotificationClientType getType() {
        return type;
    }

    public void setType(final NotificationClientType type) {
        this.type = type;
    }

    public NotificationStateClientType getState() {
        return state;
    }

    public void setState(final NotificationStateClientType state) {
        this.state = state;
    }

    /* Equals, HashCode and ToString */
    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof NotificationModel)) {
            return false;
        }
        final NotificationModel that = (NotificationModel) o;
        final EqualsBuilder builder = new EqualsBuilder();
        builder.appendSuper(super.equals(o));
        builder.append(this.getUuId(), that.getUuId());
        builder.append(this.getBody(), that.getBody());
        builder.append(this.getSubject(), that.getSubject());
        builder.append(this.getType(), that.getType());
        builder.append(this.getState(), that.getState());
        return builder.isEquals();
    }

    @Override
    public int hashCode() {
        final HashCodeBuilder builder = new HashCodeBuilder();
        builder.appendSuper(super.hashCode());
        builder.append(this.getUuId());
        builder.append(this.getBody());
        builder.append(this.getSubject());
        builder.append(this.getType());
        builder.append(this.getState());
        return builder.build();
    }

    @Override
    public String toString() {
        final ToStringBuilder builder = new ToStringBuilder(this);
        builder.appendSuper(super.toString());
        builder.append("uuId", this.getUuId());
        builder.append("body", this.getBody());
        builder.append("subject", this.getSubject());
        builder.append("type", this.getType());
        builder.append("state", this.getState());
        return builder.build();
    }
}
