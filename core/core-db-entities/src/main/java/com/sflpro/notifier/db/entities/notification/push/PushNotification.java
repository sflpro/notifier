package com.sflpro.notifier.db.entities.notification.push;


import com.sflpro.notifier.db.entities.AbstractDomainEntityModel;
import com.sflpro.notifier.db.entities.notification.Notification;
import com.sflpro.notifier.db.entities.notification.NotificationType;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.persistence.*;
import java.util.Locale;

/**
 * User: Ruben Dilanyan
 * Company: SFL LLC
 * Date: 8/11/15
 * Time: 2:32 PM
 */
@Entity
@DiscriminatorValue(value = "PUSH")
@Table(name = "notification_push")
@NamedEntityGraph(
        name = "PushNotification.ProcessingFlow",
        attributeNodes = {
                @NamedAttributeNode(value = "recipient", subgraph = "recipient.properties"),
                @NamedAttributeNode("properties")
        },
        subgraphs = @NamedSubgraph(
                name = "recipient.devices",
                attributeNodes = {
                        @NamedAttributeNode("devices"),
                        @NamedAttributeNode("lastDevice")
                }
        )
)
public class PushNotification extends Notification {

    private static final long serialVersionUID = -803404819866716200L;

    //region Properties

    @Column(name = "template_name")
    private String templateName;

    @Column(name = "locale")
    private Locale locale;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "recipient_id", nullable = false)
    private PushNotificationRecipient recipient;

    //endregion

    //region Constants

    public PushNotification() {
        initializeDefaults();
    }

    public PushNotification(final boolean generateUuId) {
        super(generateUuId);
        initializeDefaults();
    }

    //endregion

    //region Getters and Setters

    public String getTemplateName() {
        return templateName;
    }

    public void setTemplateName(final String templateName) {
        this.templateName = templateName;
    }

    public Locale getLocale() {
        return locale;
    }

    public void setLocale(final Locale locale) {
        this.locale = locale;
    }

    public PushNotificationRecipient getRecipient() {
        return recipient;
    }

    public void setRecipient(final PushNotificationRecipient subscription) {
        this.recipient = subscription;
    }

    //endregion

    /* Private utility methods */
    private void initializeDefaults() {
        setType(NotificationType.PUSH);
    }

    //region Equals, HashCode and ToString

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj == this) {
            return true;
        }
        if (obj.getClass() != getClass()) {
            return false;
        }
        PushNotification rhs = (PushNotification) obj;
        return new EqualsBuilder()
                .appendSuper(super.equals(obj))
                .append(this.templateName, rhs.templateName)
                .append(this.locale, rhs.locale)
                .append(AbstractDomainEntityModel.getIdOrNull(this.recipient), AbstractDomainEntityModel.getIdOrNull(rhs.recipient))
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .appendSuper(super.hashCode())
                .append(templateName)
                .append(locale)
                .append(AbstractDomainEntityModel.getIdOrNull(recipient))
                .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .appendSuper(super.toString())
                .append("templateName", templateName)
                .append("locale", locale)
                .append("recipient", AbstractDomainEntityModel.getIdOrNull(recipient))
                .toString();
    }

    //endregion
}
