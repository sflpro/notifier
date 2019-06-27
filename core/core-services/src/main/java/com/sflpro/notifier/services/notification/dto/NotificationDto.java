package com.sflpro.notifier.services.notification.dto;

import com.sflpro.notifier.db.entities.notification.Notification;
import com.sflpro.notifier.db.entities.notification.NotificationType;
import com.sflpro.notifier.services.common.dto.AbstractDomainEntityModelDto;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.springframework.util.Assert;

import java.util.Collections;
import java.util.Map;

/**
 * User: Ruben Dilanyan
 * Company: SFL LLC
 * Date: 3/21/15
 * Time: 7:24 PM
 */
public abstract class NotificationDto<T extends Notification> extends AbstractDomainEntityModelDto<T> {
    private static final long serialVersionUID = -4673477751721337650L;

    /* Properties */
    private final NotificationType type;

    private String content;

    private String subject;

    private String clientIpAddress;

    private Map<String, String> secureProperties;

    private String templateName;

    /* Constructors */
    public NotificationDto(final NotificationType type, final String content, final String subject, final String clientIpAddress) {
        this.type = type;
        this.content = content;
        this.clientIpAddress = clientIpAddress;
        this.subject = subject;
    }

    public NotificationDto(final NotificationType type, final String content, final String subject, final String clientIpAddress,  final String templateName) {
        this.type = type;
        this.content = content;
        this.subject = subject;
        this.clientIpAddress = clientIpAddress;
        this.secureProperties = secureProperties;
        this.templateName = templateName;
    }

    public NotificationDto(final NotificationType type) {
        this.type = type;
        this.secureProperties = Collections.emptyMap();
    }

    /* Properties getters and setters */
    public NotificationType getType() {
        return type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(final String content) {
        this.content = content;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(final String subject) {
        this.subject = subject;
    }

    public String getClientIpAddress() {
        return clientIpAddress;
    }

    public void setClientIpAddress(final String clientIpAddress) {
        this.clientIpAddress = clientIpAddress;
    }

    public Map<String, String> getSecureProperties() {
        return secureProperties;
    }

    public void setSecureProperties(final Map<String, String> secureProperties) {
        this.secureProperties = secureProperties;
    }

    public String getTemplateName() {
        return templateName;
    }

    public void setTemplateName(final String templateName) {
        this.templateName = templateName;
    }

    /* Public interface methods */
    @Override
    public void updateDomainEntityProperties(final T notification) {
        Assert.notNull(notification, "Notification should not be null");
        notification.setContent(getContent());
        notification.setClientIpAddress(getClientIpAddress());
        notification.setSubject(getSubject());
    }

    /* Equals, HashCode and ToString */
    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof NotificationDto)) {
            return false;
        }
        final NotificationDto that = (NotificationDto) o;
        final EqualsBuilder builder = new EqualsBuilder();
        builder.appendSuper(super.equals(that));
        builder.append(this.getType(), that.getType());
        builder.append(this.getClientIpAddress(), that.getClientIpAddress());
        builder.append(this.getContent(), that.getContent());
        builder.append(this.getSubject(), that.getSubject());
        builder.append(this.getSecureProperties(), that.getSecureProperties());
        return builder.isEquals();
    }

    @Override
    public int hashCode() {
        final HashCodeBuilder builder = new HashCodeBuilder();
        builder.appendSuper(super.hashCode());
        builder.append(this.getType());
        builder.append(this.getClientIpAddress());
        builder.append(this.getContent());
        builder.append(this.getSubject());
        builder.append(this.getSecureProperties());
        return builder.build();
    }


    @Override
    public String toString() {
        final ToStringBuilder builder = new ToStringBuilder(this);
        builder.appendSuper(super.toString());
        builder.append("type", this.getType());
        builder.append("clientIpAddress", this.getClientIpAddress());
        builder.append("content", this.getContent());
        builder.append("subject", this.getSubject());
        builder.append("hasSecureProperties", this.getSecureProperties());
        return builder.build();
    }
}
