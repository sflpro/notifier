package com.sflpro.notifier.services.notification.dto;

import com.sflpro.notifier.db.entities.notification.Notification;
import com.sflpro.notifier.db.entities.notification.NotificationProviderType;
import com.sflpro.notifier.db.entities.notification.NotificationType;
import com.sflpro.notifier.db.entities.notification.email.NotificationProperty;
import com.sflpro.notifier.services.common.dto.AbstractDomainEntityModelDto;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.springframework.util.Assert;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

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

    private Map<String, String> properties;

    private String templateName;

    private NotificationProviderType providerType;

    private boolean hasSecureProperties;

    private Locale locale;

    /* Constructors */
    public NotificationDto(final NotificationType type,
                           final String content,
                           final String subject,
                           final String clientIpAddress,
                           final NotificationProviderType providerType) {
        this.type = type;
        this.content = content;
        this.clientIpAddress = clientIpAddress;
        this.subject = subject;
        this.providerType = providerType;
        this.properties = new HashMap<>();
    }

    public NotificationDto(final NotificationType type, final String content, final String subject, final String clientIpAddress, final String templateName, final NotificationProviderType providerType) {
        this.type = type;
        this.content = content;
        this.subject = subject;
        this.clientIpAddress = clientIpAddress;
        this.templateName = templateName;
        this.providerType = providerType;
        this.properties = new HashMap<>();
    }

    public NotificationDto(final NotificationType type) {
        this.type = type;
        this.properties = new HashMap<>();
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

    public Map<String, String> getProperties() {
        return properties;
    }

    public void setProperties(final Map<String, String> properties) {
        this.properties = properties;
    }

    public String getTemplateName() {
        return templateName;
    }

    public void setTemplateName(final String templateName) {
        this.templateName = templateName;
    }

    public NotificationProviderType getProviderType() {
        return providerType;
    }

    public void setProviderType(final NotificationProviderType providerType) {
        this.providerType = providerType;
    }

    public boolean isHasSecureProperties() {
        return hasSecureProperties;
    }

    public void setHasSecureProperties(final boolean hasSecureProperties) {
        this.hasSecureProperties = hasSecureProperties;
    }

    public Locale getLocale() {
        return locale;
    }

    public void setLocale(Locale locale) {
        this.locale = locale;
    }

    /* Public interface methods */
    @Override
    public void updateDomainEntityProperties(final T notification) {
        Assert.notNull(notification, "Notification should not be null");
        notification.setContent(getContent());
        notification.setClientIpAddress(getClientIpAddress());
        notification.setSubject(getSubject());
        notification.setProviderType(providerType);
        notification.setProperties(getProperties().entrySet()
                .stream()
                .map(entry -> new NotificationProperty(entry.getKey(), entry.getValue()))
                .collect(Collectors.toList()));
        notification.setHasSecureProperties(isHasSecureProperties());
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
        builder.append(this.getProperties(), that.getProperties());
        builder.append(this.getProviderType(), that.getProviderType());
        builder.append(this.isHasSecureProperties(), that.isHasSecureProperties());
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
        builder.append(this.getProperties());
        builder.append(this.getProviderType());
        builder.append(this.isHasSecureProperties());
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
        builder.append("properties", this.getProperties());
        builder.append("providerType", this.getProviderType());
        builder.append("hasSecureProperties", this.isHasSecureProperties());
        return builder.build();
    }
}
