package com.sflpro.notifier.db.entities;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.joda.time.MutableDateTime;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * User: Ruben Dilanyan
 * Company: SFL LLC
 * Date: 10/01/14
 * Time: 10:18 PM
 */
@MappedSuperclass
public abstract class AbstractDomainEntityModel implements Serializable {

    private static final long serialVersionUID = 1053842483021848948L;

    /* Properties */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;

    @Column(name = "created", nullable = false)
    private Date created;

    @Column(name = "removed", nullable = true)
    private Date removed;

    @Column(name = "updated", nullable = false)
    private Date updated;

    /* Constructors */
    public AbstractDomainEntityModel() {
        setCreated(new Date());
        setUpdated(getCreated());
    }

    /* Getters and setters */
    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public Date getCreated() {
        return cloneDateIfNotNull(created);
    }

    public void setCreated(final Date created) {
        this.created = cloneDateIfNotNullAndStripOffMillisOfSecond(created);
    }

    public Date getRemoved() {
        return cloneDateIfNotNull(removed);
    }

    public void setRemoved(final Date removed) {
        this.removed = cloneDateIfNotNullAndStripOffMillisOfSecond(removed);
    }

    public Date getUpdated() {
        return cloneDateIfNotNull(updated);
    }

    public void setUpdated(final Date updated) {
        this.updated = cloneDateIfNotNullAndStripOffMillisOfSecond(updated);
    }

    /* Static utility methods */
    public static Date cloneDateIfNotNull(final Date date) {
        if (date == null) {
            return null;
        }
        return (Date) date.clone();
    }

    public static Date cloneDateIfNotNullAndStripOffMillisOfSecond(final Date date) {
        if (date == null) {
            return null;
        }
        final MutableDateTime mutableDateTime = new MutableDateTime(date);
        mutableDateTime.setMillisOfSecond(0);
        return mutableDateTime.toDate();
    }

    public static Long getIdOrNull(final AbstractDomainEntityModel entity) {
        return entity != null ? entity.getId() : null;
    }

    public static Double getDoubleValueOrNull(final BigDecimal value) {
        return value != null ? value.doubleValue() : null;
    }

    /* Equals, HashCode and ToString */
    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AbstractDomainEntityModel)) {
            return false;
        }
        final AbstractDomainEntityModel that = (AbstractDomainEntityModel) o;
        final EqualsBuilder builder = new EqualsBuilder();
        builder.append(getId(), that.getId());
        return builder.isEquals();
    }

    @Override
    public int hashCode() {
        final HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(getId());
        return builder.build();
    }

    @Override
    public String toString() {
        final ToStringBuilder builder = new ToStringBuilder(this);
        builder.append("id", getId());
        builder.append("created", getCreated());
        builder.append("removed", getRemoved());
        builder.append("updated", getUpdated());
        return builder.build();
    }
}
