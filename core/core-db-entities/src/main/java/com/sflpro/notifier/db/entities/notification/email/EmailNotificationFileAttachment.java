package com.sflpro.notifier.db.entities.notification.email;

import com.sflpro.notifier.db.entities.AbstractDomainEntityModel;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * User: Arthur Hakobyan
 * Company: SFL LLC
 * Date: 05/29/2020
 */
@Entity
@Table(name = "notification_email_file_attachment")
public class EmailNotificationFileAttachment extends AbstractDomainEntityModel {

    private static final long serialVersionUID = -736625849821741940L;

    /* Properties */
    @Column(name = "name")
    private String fileName;

    @Column(name = "mime_type")
    private String mimeType;

    @Column(name = "url")
    private String fileUrl;

    /* Constructors */
    public EmailNotificationFileAttachment() {
        super();
    }

    /* Properties getters and setters */
    public String getFileName() {
        return fileName;
    }

    public EmailNotificationFileAttachment setFileName(String fileName) {
        this.fileName = fileName;
        return this;
    }

    public String getMimeType() {
        return mimeType;
    }

    public EmailNotificationFileAttachment setMimeType(String mimeType) {
        this.mimeType = mimeType;
        return this;
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public EmailNotificationFileAttachment setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
        return this;
    }

    /* Equals, HashCode and ToString */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        EmailNotificationFileAttachment that = (EmailNotificationFileAttachment) o;

        return new EqualsBuilder()
                .appendSuper(super.equals(o))
                .append(fileName, that.fileName)
                .append(mimeType, that.mimeType)
                .append(fileUrl, that.fileUrl)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .appendSuper(super.hashCode())
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
