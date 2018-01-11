package com.sflpro.notifier.externalclients.push.amazon.model.request;

import com.sflpro.notifier.externalclients.push.amazon.model.AbstractAmazonSnsApiCommunicatorModel;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * User: Ruben Dilanyan
 * Company: SFL LLC
 * Date: 8/18/15
 * Time: 10:20 AM
 */
public class RegisterUserDeviceTokenRequest extends AbstractAmazonSnsApiCommunicatorModel {

    private static final long serialVersionUID = -1011337107319850740L;

    /* Properties */
    private final String userDeviceToken;

    private final String platformApplicationArn;

    /* Constructors */
    public RegisterUserDeviceTokenRequest(final String userDeviceToken, final String platformApplicationArn) {
        this.userDeviceToken = userDeviceToken;
        this.platformApplicationArn = platformApplicationArn;
    }

    /* Properties getters and setters */
    public String getUserDeviceToken() {
        return userDeviceToken;
    }

    public String getPlatformApplicationArn() {
        return platformApplicationArn;
    }

    /* Equals, HashCode and ToString */
    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof RegisterUserDeviceTokenRequest)) {
            return false;
        }
        RegisterUserDeviceTokenRequest that = (RegisterUserDeviceTokenRequest) o;
        final EqualsBuilder builder = new EqualsBuilder();
        builder.appendSuper(super.equals(that));
        builder.append(this.getPlatformApplicationArn(), that.getPlatformApplicationArn());
        builder.append(this.getUserDeviceToken(), that.getUserDeviceToken());
        return builder.build();
    }

    @Override
    public int hashCode() {
        final HashCodeBuilder builder = new HashCodeBuilder();
        builder.appendSuper(super.hashCode());
        builder.append(this.getPlatformApplicationArn());
        builder.append(this.getUserDeviceToken());
        return builder.build();
    }

    @Override
    public String toString() {
        final ToStringBuilder builder = new ToStringBuilder(this);
        builder.appendSuper(super.toString());
        builder.append("platformApplicationArn", this.getPlatformApplicationArn());
        builder.append("userDeviceToken", this.getUserDeviceToken());
        return builder.build();
    }
}
