package com.sflpro.notifier.externalclients.push.amazon.model.response;

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
public class GetDeviceEndpointAttributesResponse extends AbstractAmazonSnsApiCommunicatorModel {

    private static final long serialVersionUID = 7901944220099058001L;

    /* Properties */
    private final String deviceEndpointArn;

    private final String token;

    private final boolean enabled;

    private final boolean exists;

    /* Constructors */
    public GetDeviceEndpointAttributesResponse(final String deviceEndpointArn, final String token, final boolean enabled, final boolean exists) {
        this.deviceEndpointArn = deviceEndpointArn;
        this.token = token;
        this.enabled = enabled;
        this.exists = exists;
    }

    /* Properties getters and setters */
    public String getDeviceEndpointArn() {
        return deviceEndpointArn;
    }

    public String getToken() {
        return token;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public boolean isExists() {
        return exists;
    }

    /* Equals, HashCode and ToString */
    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof GetDeviceEndpointAttributesResponse)) {
            return false;
        }
        final GetDeviceEndpointAttributesResponse that = (GetDeviceEndpointAttributesResponse) o;
        final EqualsBuilder builder = new EqualsBuilder();
        builder.appendSuper(super.equals(that));
        builder.append(this.getDeviceEndpointArn(), that.getDeviceEndpointArn());
        builder.append(this.getToken(), that.getToken());
        builder.append(this.isEnabled(), that.isEnabled());
        builder.append(this.isExists(), that.isExists());
        return builder.build();
    }

    @Override
    public int hashCode() {
        final HashCodeBuilder builder = new HashCodeBuilder();
        builder.appendSuper(super.hashCode());
        builder.append(this.getDeviceEndpointArn());
        builder.append(this.getToken());
        builder.append(this.isEnabled());
        builder.append(this.isExists());
        return builder.build();
    }

    @Override
    public String toString() {
        final ToStringBuilder builder = new ToStringBuilder(this);
        builder.appendSuper(super.toString());
        builder.append("deviceEndpointArn", this.getDeviceEndpointArn());
        builder.append("token", this.getToken());
        builder.append("enabled", this.isEnabled());
        builder.append("exists", this.isExists());
        return builder.build();
    }
}

