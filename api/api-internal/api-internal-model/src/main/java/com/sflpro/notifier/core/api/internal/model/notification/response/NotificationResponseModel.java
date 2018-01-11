package com.sflpro.notifier.core.api.internal.model.notification.response;

import com.sflpro.notifier.core.api.internal.model.common.AbstractResponseModel;
import com.sflpro.notifier.core.api.internal.model.notification.NotificationModel;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hovsepharutyunyan on 12/20/16.
 */
public class NotificationResponseModel extends AbstractResponseModel
{
    private List<NotificationModel> notifications;

    public NotificationResponseModel()
    {
        notifications = new ArrayList<>();
    }

    public NotificationResponseModel(List<NotificationModel> notifications)
    {
        this.notifications = notifications;
    }

    public List<NotificationModel> getNotifications()
    {
        return notifications;
    }

    public void setNotifications(List<NotificationModel> notifications)
    {
        this.notifications = notifications;
    }

    public void addNotificationModel(NotificationModel notificationModel)
    {
        notifications.add(notificationModel);
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o)
        {
            return true;
        }
        if (!(o instanceof NotificationResponseModel))
        {
            return false;
        }
        NotificationResponseModel that = (NotificationResponseModel) o;
        return new EqualsBuilder()
                .appendSuper(super.equals(o))
                .append(notifications, that.notifications)
                .isEquals();
    }

    @Override
    public int hashCode()
    {
        return new HashCodeBuilder()
                .appendSuper(super.hashCode())
                .append(notifications)
                .toHashCode();
    }

    @Override
    public String toString()
    {
        return new ToStringBuilder(this)
                .append("notifications", notifications)
                .toString();
    }
}
