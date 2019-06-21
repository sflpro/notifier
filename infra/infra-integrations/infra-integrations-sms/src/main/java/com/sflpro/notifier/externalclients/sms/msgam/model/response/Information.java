package com.sflpro.notifier.externalclients.sms.msgam.model.response;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlValue;

/**
 * User: Tigran Tserunyan
 * Company: SFL LLC
 * Date: 22/05/2017
 * Time: 4:51 PM
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class Information {

    @XmlAttribute(name = "version")
    private String version;

    @XmlValue
    private String code;

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Information() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (!(o instanceof Information)) return false;

        Information that = (Information) o;

        return new EqualsBuilder()
                .append(version, that.version)
                .append(code, that.code)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(version)
                .append(code)
                .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("version", version)
                .append("code", code)
                .toString();
    }
}
