package com.sflpro.notifier.externalclients.sms.nikitamobile.model;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlValue;

/**
 * Created by Hayk Mkrtchyan.
 * Date: 6/24/19
 * Time: 11:01 AM
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class Content {

    @XmlAttribute(name = "type")
    private String type;

    @XmlValue
    private String text;

    public Content() {
        super();
    }

    public Content(final String type, final String text) {
        this.type = type;
        this.text = text;
    }

    public String getType() {
        return type;
    }

    public void setType(final String type) {
        this.type = type;
    }

    public String getText() {
        return text;
    }

    public void setText(final String text) {
        this.text = text;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Content)) {
            return false;
        }
        final Content content = (Content) o;
        return new EqualsBuilder()
                .append(type, content.type)
                .append(text, content.text)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(type)
                .append(text)
                .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("type", type)
                .append("text", text)
                .toString();
    }
}
