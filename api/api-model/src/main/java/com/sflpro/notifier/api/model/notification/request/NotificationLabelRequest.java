package com.sflpro.notifier.api.model.notification.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.io.Serializable;

public class NotificationLabelRequest implements Serializable {

    private static final long serialVersionUID = -337850853635048937L;

    /* Properties */
    @JsonProperty("labelName")
    private String labelName;

    /* Constructors */
    public NotificationLabelRequest() {
    }

    /* Properties Getters and Setters */
    public String getLabelName() {
        return this.labelName;
    }

    public void setLabelName(final String labelName) {
        this.labelName = labelName;
    }

    /* Equals, HashCode and ToString */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        NotificationLabelRequest that = (NotificationLabelRequest) o;

        return new EqualsBuilder()
                .append(labelName, that.labelName)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(labelName)
                .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("labelName", labelName)
                .toString();
    }
}