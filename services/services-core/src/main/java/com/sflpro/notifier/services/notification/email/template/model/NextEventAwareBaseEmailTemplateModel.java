package com.sflpro.notifier.services.notification.email.template.model;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * @author Ruben Dilanyan
 *         <p>
 *         Jul 11, 2014
 */
public class NextEventAwareBaseEmailTemplateModel extends BaseEmailTemplateModel {
    private static final long serialVersionUID = 1L;

    /* Properties */
    private String eventDate;

    private String eventDescription;

    private String eventType;

    public NextEventAwareBaseEmailTemplateModel() {
        //default constructor
    }

    public NextEventAwareBaseEmailTemplateModel(String url, String emailcode) {
        super(url, emailcode);
    }

    public String getEventDate() {
        return eventDate;
    }

    public void setEventDate(String eventDate) {
        this.eventDate = eventDate;
    }

    public String getEventDescription() {
        return eventDescription;
    }

    public void setEventDescription(String eventDescription) {
        this.eventDescription = eventDescription;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    /* Equals, HashCode and ToString */
    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof NextEventAwareBaseEmailTemplateModel)) {
            return false;
        }
        final NextEventAwareBaseEmailTemplateModel that = (NextEventAwareBaseEmailTemplateModel) o;
        final EqualsBuilder builder = new EqualsBuilder();
        builder.appendSuper(super.equals(o));
        builder.append(this.getEventType(), that.getEventType());
        builder.append(this.getEventDate(), that.getEventDate());
        builder.append(this.getEventDescription(), that.getEventDescription());
        return builder.isEquals();
    }

    @Override
    public int hashCode() {
        final HashCodeBuilder builder = new HashCodeBuilder();
        builder.appendSuper(super.hashCode());
        builder.append(this.getEventType());
        builder.append(this.getEventDate());
        builder.append(this.getEventDescription());
        return builder.build();
    }


    @Override
    public String toString() {
        final ToStringBuilder builder = new ToStringBuilder(this);
        builder.appendSuper(super.toString());
        builder.append("eventType", this.getEventType());
        builder.append("eventDate", this.getEventDate());
        builder.append("eventDescription", this.getEventDescription());
        return builder.build();
    }
}
