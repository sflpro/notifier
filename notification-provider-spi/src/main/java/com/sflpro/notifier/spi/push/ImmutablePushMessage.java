package com.sflpro.notifier.spi.push;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.Map;

/**
 * Created by Hayk Mkrtchyan.
 * Date: 7/3/19
 * Time: 10:46 AM
 */
final class ImmutablePushMessage implements PushMessage {


    private String destinationRouteToken;
    private String subject;
    private String body;
    private PlatformType platformType;
    private final Map<String, String> properties;

    ImmutablePushMessage(final String destinationRouteToken,
                         final String subject,
                         final String body,
                         final PlatformType platformType,
                         final Map<String, String> properties) {
        this.destinationRouteToken = destinationRouteToken;
        this.subject = subject;
        this.body = body;
        this.platformType = platformType;
        this.properties = properties;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ImmutablePushMessage)) {
            return false;
        }
        final ImmutablePushMessage that = (ImmutablePushMessage) o;
        return new EqualsBuilder()
                .append(destinationRouteToken, that.destinationRouteToken)
                .append(subject, that.subject)
                .append(body, that.body)
                .append(platformType, that.platformType)
                .append(properties, that.properties)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(destinationRouteToken)
                .append(subject)
                .append(body)
                .append(platformType)
                .append(properties)
                .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("deviceEndpointArn", destinationRouteToken)
                .append("subject", subject)
                .append("body", body)
                .append("platformType", platformType)
                .append("properties", properties)
                .toString();
    }

    @Override
    public String destinationRouteToken() {
        return destinationRouteToken;
    }

    @Override
    public String subject() {
        return subject;
    }

    @Override
    public String body() {
        return body;
    }

    @Override
    public Map<String, String> properties() {
        return properties;
    }

    @Override
    public PlatformType platformType() {
        return platformType;
    }
}
