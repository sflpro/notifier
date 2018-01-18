package com.sflpro.notifier.services.device.dto;

import com.sflpro.notifier.services.common.dto.AbstractDomainEntityModelDto;
import com.sflpro.notifier.services.device.model.UserDevice;
import com.sflpro.notifier.db.entities.device.mobile.DeviceOperatingSystemType;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.springframework.util.Assert;

/**
 * User: Ruben Dilanyan
 * Company: SFL LLC
 * Date: 1/9/16
 * Time: 6:29 PM
 */
public class UserDeviceDto extends AbstractDomainEntityModelDto<UserDevice> {

    private static final long serialVersionUID = 2770517452071374944L;

    /* Properties */
    private String uuId;

    private DeviceOperatingSystemType osType;

    /* Constructors */
    public UserDeviceDto() {
    }

    public UserDeviceDto(final String uuId, final DeviceOperatingSystemType osType) {
        this.uuId = uuId;
        this.osType = osType;
    }

    /* Properties getters and setters */
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

    /* Public interface methods */
    @Override
    public void updateDomainEntityProperties(final UserDevice userDevice) {
        Assert.notNull(userDevice, "User device should not be null");
        userDevice.setUuId(getUuId());
        userDevice.setOsType(getOsType());
    }

    /* Equals, HashCode and ToString */
    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof UserDeviceDto)) {
            return false;
        }
        final UserDeviceDto that = (UserDeviceDto) o;
        final EqualsBuilder builder = new EqualsBuilder();
        builder.appendSuper(super.equals(that));
        builder.append(this.getUuId(), that.getUuId());
        builder.append(this.getOsType(), that.getOsType());
        return builder.isEquals();
    }

    @Override
    public int hashCode() {
        final HashCodeBuilder builder = new HashCodeBuilder();
        builder.appendSuper(super.hashCode());
        builder.append(this.getUuId());
        builder.append(this.getOsType());
        return builder.build();
    }


    @Override
    public String toString() {
        final ToStringBuilder builder = new ToStringBuilder(this);
        builder.appendSuper(super.toString());
        builder.append("uuId", this.getUuId());
        builder.append("osType", this.getOsType());
        return builder.build();
    }
}
