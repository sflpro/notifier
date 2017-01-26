package com.sfl.nms.queue.amqp.rpc.message;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.io.Serializable;

/**
 * User: Ruben Dilanyan
 * Company: SFL LLC
 * Date: 12/12/14
 * Time: 1:43 PM
 */
public class RPCMessage implements Serializable {

    @JsonProperty(value = "callIdentifier", required = true)
    private String callIdentifier;

    @JsonProperty(value = "payload", required = true)
    private String payload;

    /* Constructors */
    public RPCMessage() {

    }

    /* Properties getters and setters */
    public String getCallIdentifier() {
        return callIdentifier;
    }

    public void setCallIdentifier(final String callIdentifier) {
        this.callIdentifier = callIdentifier;
    }

    public String getPayload() {
        return payload;
    }

    public void setPayload(final String payload) {
        this.payload = payload;
    }

    /* Equals, HashCode and ToString */
    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof RPCMessage)) {
            return false;
        }
        final RPCMessage that = (RPCMessage) o;
        final EqualsBuilder builder = new EqualsBuilder();
        builder.append(this.callIdentifier, that.getCallIdentifier());
        builder.append(this.payload, that.getPayload());
        return builder.build();
    }

    @Override
    public int hashCode() {
        final HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(this.callIdentifier);
        builder.append(this.payload);
        return builder.build();
    }

    @Override
    public String toString() {
        final ToStringBuilder builder = new ToStringBuilder(this);
        builder.append("callIdentifier", callIdentifier);
        builder.append("payload", payload);
        return builder.build();
    }
}
