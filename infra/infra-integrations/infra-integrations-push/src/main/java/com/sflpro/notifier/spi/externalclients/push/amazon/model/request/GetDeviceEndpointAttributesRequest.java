package com.sflpro.notifier.externalclients.push.amazon.model.request;

import com.sflpro.notifier.externalclients.push.amazon.model.AbstractAmazonSnsApiCommunicatorModel;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * User: Ruben Dilanyan
 * Company: SFL LLC
 * Date: 8/18/15
 * Time: 11:26 AM
 */
public class GetDeviceEndpointAttributesRequest extends AbstractAmazonSnsApiCommunicatorModel {

    private static final long serialVersionUID = 7901944220099058001L;

    /* Properties */
    private final String deviceEndpointArn;

    /* Constructors */
    public GetDeviceEndpointAttributesRequest(final String deviceEndpointArn) {
        this.deviceEndpointArn = deviceEndpointArn;
    }

    /* Properties getters and setters */
    public String getDeviceEndpointArn() {
        return deviceEndpointArn;
    }

    /* Equals, HashCode and ToString */
    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof GetDeviceEndpointAttributesRequest)) {
            return false;
        }
        final GetDeviceEndpointAttributesRequest that = (GetDeviceEndpointAttributesRequest) o;
        final EqualsBuilder builder = new EqualsBuilder();
        builder.appendSuper(super.equals(that));
        builder.append(this.getDeviceEndpointArn(), that.getDeviceEndpointArn());
        return builder.build();
    }

    @Override
    public int hashCode() {
        final HashCodeBuilder builder = new HashCodeBuilder();
        builder.appendSuper(super.hashCode());
        builder.append(this.getDeviceEndpointArn());
        return builder.build();
    }

    @Override
    public String toString() {
        final ToStringBuilder builder = new ToStringBuilder(this);
        builder.appendSuper(super.toString());
        builder.append("deviceEndpointArn", this.getDeviceEndpointArn());
        return builder.build();
    }
}

