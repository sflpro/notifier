package com.sflpro.notifier.services.notification.email.model;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.Map;

/**
 * Company: SFL LLC
 * Created on 04/12/2017
 *
 * @author Davit Harutyunyan
 */
public class EmailData {
    /* Properties */
    private String to = null;

    private String templateName = null;

    private Map<String, Object> data = null;

    private Map<String, String> contentMap = null;

    /* Constructors */
    public EmailData(final String to, final String templateName, final Map<String, Object> data, final Map<String, String> contentMap) {
        this(to, templateName, data);
        this.contentMap = contentMap;
    }

    public EmailData() {
        //default constructor
    }

    public EmailData(final String to, final String templateName, final Map<String, Object> data) {
        if (to == null) {
            throw new IllegalArgumentException("Email <to> is null");
        }
        this.to = to;
        this.templateName = templateName;
        this.data = data;
    }

    /* Properties getters and setters */
    public String getTo() {
        return to;
    }

    public String getTemplateName() {
        return templateName;
    }

    public Map<String, Object> getData() {
        return data;
    }

    public Map<String, String> getContentMap() {
        return contentMap;
    }

    public void setTo(final String to) {
        this.to = to;
    }

    public void setTemplateName(final String templateName) {
        this.templateName = templateName;
    }

    public void setData(final Map<String, Object> data) {
        this.data = data;
    }

    public void setContentMap(final Map<String, String> contentMap) {
        this.contentMap = contentMap;
    }

    /* Hash code, equals and toString */
    @Override
    public boolean equals(final Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj == this) {
            return true;
        }
        if (obj.getClass() != getClass()) {
            return false;
        }
        final EmailData rhs = (EmailData) obj;
        return new EqualsBuilder()
                .append(this.to, rhs.to)
                .append(this.templateName, rhs.templateName)
                .append(this.data, rhs.data)
                .append(this.contentMap, rhs.contentMap)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .append(to)
                .append(templateName)
                .append(data)
                .append(contentMap)
                .toHashCode();
    }


    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("to", to)
                .append("templateName", templateName)
                .append("data", data)
                .append("contentMap", contentMap)
                .toString();
    }
}
