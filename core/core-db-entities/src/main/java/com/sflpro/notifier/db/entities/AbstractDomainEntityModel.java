package com.sflpro.notifier.db.entities;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

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
    private LocalDateTime created;

    @Column(name = "removed", nullable = true)
    private LocalDateTime removed;

    @Column(name = "updated", nullable = false)
    private LocalDateTime updated;

    /* Constructors */
    public AbstractDomainEntityModel() {
        created = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);
        updated = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);
    }

    /* Getters and setters */
    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public LocalDateTime getCreated() {
        return created;
    }

    public AbstractDomainEntityModel setCreated(final LocalDateTime created) {
        this.created = created;
        return this;
    }

    public LocalDateTime getRemoved() {
        return removed;
    }

    public AbstractDomainEntityModel setRemoved(final LocalDateTime removed) {
        this.removed = removed;
        return this;
    }

    public LocalDateTime getUpdated() {
        return updated;
    }

    public AbstractDomainEntityModel setUpdated(final LocalDateTime updated) {
        this.updated = updated;
        return this;
    }

    /* Static utility methods */
    public static Long getIdOrNull(final AbstractDomainEntityModel entity) {
        return entity != null ? entity.getId() : null;
    }

    public static Double getDoubleValueOrNull(final BigDecimal value) {
        return value != null ? value.doubleValue() : null;
    }

    /* Equals, HashCode and ToString */
    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        final AbstractDomainEntityModel that = (AbstractDomainEntityModel) o;

        return new EqualsBuilder()
                .append(getId(), that.getId())
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(getId())
                .toHashCode();
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
