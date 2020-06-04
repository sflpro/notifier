package com.sflpro.notifier.spi.email;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.springframework.util.Assert;

/**
 * User: Arthur Hakobyan
 * Company: SFL LLC
 * Date: 05/29/2020
 */

public class SpiEmailNotificationFileAttachment {

    /* Properties */
    private String fileName;

    private String mimeType;

    private String fileUrl;

    /* Constructor */
    public SpiEmailNotificationFileAttachment() {
    }

    public SpiEmailNotificationFileAttachment(final String fileName,
                                              final String mimeType,
                                              final String fileUrl) {
        Assert.notNull(fileName, "File name has not to be null");
        Assert.notNull(fileUrl, "File URL has not to be null");
        this.fileName = fileName;
        this.mimeType = mimeType;
        this.fileUrl = fileUrl;
    }

    /* Getters and setters */
    public String getFileName() {
        return fileName;
    }

    public void setFileName(final String fileName) {
        this.fileName = fileName;
    }

    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(final String mimeType) {
        this.mimeType = mimeType;
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(final String fileUrl) {
        this.fileUrl = fileUrl;
    }

    /* Equals, hashCode, toString */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        SpiEmailNotificationFileAttachment that = (SpiEmailNotificationFileAttachment) o;

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
