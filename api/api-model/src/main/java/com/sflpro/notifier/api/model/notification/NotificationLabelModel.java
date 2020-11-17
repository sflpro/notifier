package com.sflpro.notifier.api.model.notification;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class NotificationLabelModel {

    /* Properties */
    @JsonProperty("labelName")
    private String labelName;

    /* Constructors */
    public NotificationLabelModel() {
    }

    /* Properties Getters and Setters */
    public String getLabelName() {
        return labelName;
    }

    public void setLabelName(String labelName) {
        this.labelName = labelName;
    }

    /* Equals, HashCode and ToString */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        NotificationLabelModel that = (NotificationLabelModel) o;

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
