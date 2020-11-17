package com.sflpro.notifier.db.entities.notification;

import com.sflpro.notifier.db.entities.AbstractDomainEntityModel;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "notification_label")
public class NotificationLabel extends AbstractDomainEntityModel {

    private static final long serialVersionUID = -1556003343038789691L;

    /* Properties */
    @Column(name = "label_name", nullable = false)
    private String labelName;

    /* Constructors */
    public NotificationLabel() {

    }

    public NotificationLabel(String labelName) {
        this.labelName = labelName;
    }

    public String getLabelName() {
        return labelName;
    }

    public NotificationLabel setLabelName(String labelName) {
        this.labelName = labelName;
        return this;
    }

    /* Equals, HashCode and ToString */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        NotificationLabel that = (NotificationLabel) o;

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
