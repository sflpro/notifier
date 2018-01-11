package com.sflpro.notifier.externalclients.push.amazon.model.response;

import com.sflpro.notifier.externalclients.push.amazon.model.AbstractAmazonSnsApiCommunicatorModel;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.springframework.util.Assert;

/**
 * User: Ruben Dilanyan
 * Company: SFL LLC
 * Date: 4/9/15
 * Time: 5:29 PM
 */
public class SendPushNotificationResponse extends AbstractAmazonSnsApiCommunicatorModel {

    private static final long serialVersionUID = 9055257034174161692L;

    /* Properties */
    private final String messageId;

    /* Constructors */
    public SendPushNotificationResponse(final String messageId) {
        Assert.notNull(messageId, "Message is should not be null");
        this.messageId = messageId;
    }

    /* Getters and setters */

    public String getMessageId() {
        return messageId;
    }

    /* Equals, HashCode and ToString */
    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SendPushNotificationResponse)) {
            return false;
        }
        SendPushNotificationResponse that = (SendPushNotificationResponse) o;
        final EqualsBuilder builder = new EqualsBuilder();
        builder.appendSuper(super.equals(that));
        builder.append(getMessageId(), that.getMessageId());
        return builder.build();
    }

    @Override
    public int hashCode() {
        final HashCodeBuilder builder = new HashCodeBuilder();
        builder.appendSuper(super.hashCode());
        builder.append(getMessageId());
        return builder.build();
    }

    @Override
    public String toString() {
        final ToStringBuilder builder = new ToStringBuilder(this);
        builder.appendSuper(super.toString());
        builder.append("messageId", getMessageId());
        return builder.build();
    }
}
