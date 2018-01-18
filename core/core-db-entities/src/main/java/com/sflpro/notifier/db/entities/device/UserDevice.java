package com.sflpro.notifier.db.entities.device;

import com.sflpro.notifier.services.common.model.AbstractDomainEntityModel;
import com.sflpro.notifier.db.entities.device.mobile.DeviceOperatingSystemType;
import com.sflpro.notifier.services.user.model.User;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.persistence.*;

/**
 * User: Ruben Dilanyan
 * Company: SFL LLC
 * Date: 4/3/15
 * Time: 2:42 PM
 */
@Entity
@Table(name = "device_user", uniqueConstraints = {
        @UniqueConstraint(name = "UK_nms_user_id_uuid", columnNames = {"nms_user_id", "uuid"})})
public class UserDevice extends AbstractDomainEntityModel {
    private static final long serialVersionUID = 1764280488464094684L;

    /* Properties */
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "nms_user_id", nullable = false)
    private User user;

    @Column(name = "uuid", nullable = false)
    private String uuId;

    @Enumerated(EnumType.STRING)
    @Column(name = "os_type", nullable = false)
    private DeviceOperatingSystemType osType;

    /* Constructors */
    public UserDevice() {
    }


    /* Properties getters and setters */
    public User getUser() {
        return user;
    }

    public void setUser(final User user) {
        this.user = user;
    }

    public String getUuId() {
        return uuId;
    }

    public void setUuId(final String uuId) {
        this.uuId = uuId;
    }

    public DeviceOperatingSystemType getOsType() {
        return osType;
    }

    public void setOsType(final DeviceOperatingSystemType osType) {
        this.osType = osType;
    }

    /* Equals, HashCode and ToString */
    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof UserDevice)) {
            return false;
        }
        final UserDevice that = (UserDevice) o;
        final EqualsBuilder builder = new EqualsBuilder();
        builder.appendSuper(super.equals(that));
        builder.append(this.getUuId(), that.getUuId());
        builder.append(this.getOsType(), that.getOsType());
        builder.append(getIdOrNull(this.getUser()), getIdOrNull(that.getUser()));
        return builder.isEquals();
    }

    @Override
    public int hashCode() {
        final HashCodeBuilder builder = new HashCodeBuilder();
        builder.appendSuper(super.hashCode());
        builder.append(this.getUuId());
        builder.append(this.getOsType());
        builder.append(getIdOrNull(this.getUser()));
        return builder.build();
    }


    @Override
    public String toString() {
        final ToStringBuilder builder = new ToStringBuilder(this);
        builder.appendSuper(super.toString());
        builder.append("uuId", this.getUuId());
        builder.append("osType", this.getOsType());
        builder.append("user", getIdOrNull(this.getUser()));
        return builder.build();
    }
}
