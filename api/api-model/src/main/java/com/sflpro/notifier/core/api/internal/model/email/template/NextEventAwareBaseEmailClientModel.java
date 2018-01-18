package com.sflpro.notifier.core.api.internal.model.email.template;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * User: Davit Yeghiazaryan
 * Company: SFL LLC
 * Date 6/13/16
 * Time 12:28 PM
 */
public class NextEventAwareBaseEmailClientModel extends BaseEmailClientModel {
    private static final long serialVersionUID = -5675923335461433885L;

    /* Properties */
    @JsonProperty("eventDate")
    private String eventDate;

    @JsonProperty("eventDescription")
    private String eventDescription;

    @JsonProperty("eventType")
    private String eventType;

    public NextEventAwareBaseEmailClientModel() {
        //default constructor
    }

    public String getEventDate()
    {
        return eventDate;
    }

    public void setEventDate(String eventDate)
    {
        this.eventDate = eventDate;
    }

    public String getEventDescription()
    {
        return eventDescription;
    }

    public void setEventDescription(String eventDescription)
    {
        this.eventDescription = eventDescription;
    }

    public String getEventType()
    {
        return eventType;
    }

    public void setEventType(String eventType)
    {
        this.eventType = eventType;
    }

    /* Equals, HashCode and ToString */
    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof NextEventAwareBaseEmailClientModel)) {
            return false;
        }
        final NextEventAwareBaseEmailClientModel that = (NextEventAwareBaseEmailClientModel) o;
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
