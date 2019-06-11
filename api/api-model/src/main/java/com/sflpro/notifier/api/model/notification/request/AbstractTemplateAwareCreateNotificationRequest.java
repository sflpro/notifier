package com.sflpro.notifier.api.model.notification.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * User: Syuzanna Eprikyan
 * Company: SFL LLC
 * Date: 6/11/19
 * Time: 3:05 PM
 */
public abstract class AbstractTemplateAwareCreateNotificationRequest extends AbstractCreateNotificationRequest {
    private static final long serialVersionUID = -356204233144800774L;

    /* properties */
    @JsonProperty("templateName")
    private String templateName;

    @JsonProperty("properties")
    private Map<String, String> properties;

    @JsonProperty("secureProperties")
    private Map<String, String> secureProperties;


    /* Constructors */
    public AbstractTemplateAwareCreateNotificationRequest() {
        super();
        properties = new LinkedHashMap<>();
        secureProperties = new LinkedHashMap<>();
    }

    /* Properties getters and setters */
    public String getTemplateName() {
        return templateName;
    }

    public void setTemplateName(final String templateName) {
        this.templateName = templateName;
    }

    public Map<String, String> getProperties() {
        return properties;
    }

    public void setProperties(final Map<String, String> properties) {
        this.properties = properties;
    }

    public Map<String, String> getSecureProperties() {
        return secureProperties;
    }

    public void setSecureProperties(final Map<String, String> secureProperties) {
        this.secureProperties = secureProperties;
    }

    /* Equals, HashCode and ToString */
    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AbstractTemplateAwareCreateNotificationRequest)) {
            return false;
        }
        final AbstractTemplateAwareCreateNotificationRequest that = (AbstractTemplateAwareCreateNotificationRequest) o;
        return new EqualsBuilder()
                .appendSuper(super.equals(o))
                .append(this.getTemplateName(), that.getTemplateName())
                .append(this.getProperties(), that.getProperties())
                .append(this.getSecureProperties(), that.getSecureProperties())
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .appendSuper(super.hashCode())
                .append(this.getTemplateName())
                .append(this.getProperties())
                .append(this.getSecureProperties())
                .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .appendSuper(super.toString())
                .append("templateName", this.getTemplateName())
                .append("properties", this.getProperties())
                .append("secureProperties", this.getSecureProperties())
                .toString();
    }
}
