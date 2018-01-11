package com.sflpro.notifier.externalclients.push.amazon.model.request;

import com.sflpro.notifier.externalclients.push.amazon.model.AbstractAmazonSnsApiCommunicatorModel;
import com.sflpro.notifier.externalclients.push.amazon.model.AmazonSNSPlatformType;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.springframework.util.Assert;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * User: Ruben Dilanyan
 * Company: SFL LLC
 * Date: 4/9/15
 * Time: 5:29 PM
 */
public class SendPushNotificationRequestMessageInformation extends AbstractAmazonSnsApiCommunicatorModel {
    private static final long serialVersionUID = 7363590368333135655L;

    /* Properties */
    private final String messageSubject;

    private final String messageBody;

    private final Map<String, String> messageProperties;

    private final AmazonSNSPlatformType amazonSNSPlatformType;

    /* Constructors */
    public SendPushNotificationRequestMessageInformation(final String messageSubject, final String messageBody, final Map<String, String> messageProperties, final AmazonSNSPlatformType amazonSNSPlatformType) {
        Assert.hasText(messageBody, "Message body should not be empty");
        this.messageSubject = messageSubject;
        this.messageBody = messageBody;
        this.messageProperties = new LinkedHashMap<>(messageProperties);
        this.amazonSNSPlatformType = amazonSNSPlatformType;
    }

    /* Getters and setters */
    public String getMessageSubject() {
        return messageSubject;
    }

    public String getMessageBody() {
        return messageBody;
    }

    public Map<String, String> getMessageProperties() {
        return messageProperties;
    }

    public AmazonSNSPlatformType getAmazonSNSPlatformType() {
        return amazonSNSPlatformType;
    }

    /* Equals, HashCode and ToString */
    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SendPushNotificationRequestMessageInformation)) {
            return false;
        }
        SendPushNotificationRequestMessageInformation that = (SendPushNotificationRequestMessageInformation) o;
        final EqualsBuilder builder = new EqualsBuilder();
        builder.appendSuper(super.equals(that));
        builder.append(this.getMessageBody(), that.getMessageBody());
        builder.append(this.getMessageSubject(), that.getMessageSubject());
        builder.append(this.getMessageProperties(), that.getMessageProperties());
        builder.append(this.getAmazonSNSPlatformType(), that.getAmazonSNSPlatformType());
        return builder.build();
    }

    @Override
    public int hashCode() {
        final HashCodeBuilder builder = new HashCodeBuilder();
        builder.appendSuper(super.hashCode());
        builder.append(this.getMessageBody());
        builder.append(this.getMessageSubject());
        builder.append(this.getMessageProperties());
        builder.append(this.getAmazonSNSPlatformType());
        return builder.build();
    }

    @Override
    public String toString() {
        final ToStringBuilder builder = new ToStringBuilder(this);
        builder.appendSuper(super.toString());
        builder.append("messageSubject", this.getMessageSubject());
        builder.append("messageBody", this.getMessageBody());
        builder.append("messageProperties", this.getMessageProperties());
        builder.append("amazonSNSPlatformType", this.getAmazonSNSPlatformType());
        return builder.build();
    }
}
