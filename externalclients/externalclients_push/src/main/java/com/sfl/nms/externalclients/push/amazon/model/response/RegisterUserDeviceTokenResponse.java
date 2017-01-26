package com.sfl.nms.externalclients.push.amazon.model.response;

import com.sfl.nms.externalclients.push.amazon.model.AbstractAmazonSnsApiCommunicatorModel;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * User: Ruben Dilanyan
 * Company: SFL LLC
 * Date: 8/18/15
 * Time: 10:20 AM
 */
public class RegisterUserDeviceTokenResponse extends AbstractAmazonSnsApiCommunicatorModel {

    private static final long serialVersionUID = -1011337107319850740L;

    /* Properties */
    private final String userDeviceToken;

    private final String platformApplicationArn;

    private final String deviceEndpointArn;

    /* Constructors */
    public RegisterUserDeviceTokenResponse(final String userDeviceToken, final String platformApplicationArn, final String deviceEndpointArn) {
        this.userDeviceToken = userDeviceToken;
        this.platformApplicationArn = platformApplicationArn;
        this.deviceEndpointArn = deviceEndpointArn;
    }

    /* Properties getters and setters */
    public String getUserDeviceToken() {
        return userDeviceToken;
    }

    public String getPlatformApplicationArn() {
        return platformApplicationArn;
    }

    public String getDeviceEndpointArn() {
        return deviceEndpointArn;
    }

    /* Equals, HashCode and ToString */
    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof RegisterUserDeviceTokenResponse)) {
            return false;
        }
        RegisterUserDeviceTokenResponse that = (RegisterUserDeviceTokenResponse) o;
        final EqualsBuilder builder = new EqualsBuilder();
        builder.appendSuper(super.equals(that));
        builder.append(this.getPlatformApplicationArn(), that.getPlatformApplicationArn());
        builder.append(this.getUserDeviceToken(), that.getUserDeviceToken());
        builder.append(this.getDeviceEndpointArn(), that.getDeviceEndpointArn());
        return builder.build();
    }

    @Override
    public int hashCode() {
        final HashCodeBuilder builder = new HashCodeBuilder();
        builder.appendSuper(super.hashCode());
        builder.append(this.getPlatformApplicationArn());
        builder.append(this.getUserDeviceToken());
        builder.append(this.getDeviceEndpointArn());
        return builder.build();
    }

    @Override
    public String toString() {
        final ToStringBuilder builder = new ToStringBuilder(this);
        builder.appendSuper(super.toString());
        builder.append("platformApplicationArn", this.getPlatformApplicationArn());
        builder.append("userDeviceToken", this.getUserDeviceToken());
        builder.append("deviceEndpointArn", this.getDeviceEndpointArn());
        return builder.build();
    }
}
