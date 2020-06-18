package com.sflpro.notifier.api.model.email.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.io.Serializable;

/**
 * User: Arthur Hakobyan
 * Company: SFL LLC
 * Date: 05/24/2020
 */
public class EmailNotificationFileAttachmentRequest implements Serializable {

    private static final long serialVersionUID = -2738802140434754413L;

    /* Properties */
    @JsonProperty("fileName")
    private String fileName;

    @JsonProperty("mimeType")
    private String mimeType;

    @JsonProperty("fileUrl")
    private String fileUrl;

    /* Constructors */
    public EmailNotificationFileAttachmentRequest() {
    }

    /* Properties Getters and Setters */
    public String getFileName() {
        return this.fileName;
    }

    public void setFileName(final String fileName) {
        this.fileName = fileName;
    }

    public String getMimeType() {
        return this.mimeType;
    }

    public void setMimeType(final String mimeType) {
        this.mimeType = mimeType;
    }

    public String getFileUrl() {
        return this.fileUrl;
    }

    public void setFileUrl(final String fileUrl) {
        this.fileUrl = fileUrl;
    }

    /* Equals, HashCode and ToString */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        EmailNotificationFileAttachmentRequest that = (EmailNotificationFileAttachmentRequest) o;

        return new EqualsBuilder()
                .append(fileName, that.fileName)
                .append(mimeType, that.mimeType)
                .append(fileUrl, that.fileUrl)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(fileName)
                .append(mimeType)
                .append(fileUrl)
                .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("fileName", fileName)
                .append("mimeType", mimeType)
                .append("fileUrl", fileUrl)
                .toString();
    }
}
