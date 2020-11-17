package com.sflpro.notifier.api.facade.endpoints.notification;

import com.sflpro.notifier.api.model.notification.NotificationModel;
import com.sflpro.notifier.db.entities.notification.email.EmailNotification;
import com.sflpro.notifier.services.notification.NotificationService;
import org.apache.commons.lang3.RandomUtils;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class NotificationResourceTest {

    @Mock
    private NotificationService notificationService;

    @InjectMocks
    private NotificationResource notificationResource;

    @Test
    public void get() {
        final Long id = RandomUtils.nextLong(1, 100);
        final EmailNotification emailNotification = new EmailNotification();
        emailNotification.setId(id);
        Mockito.when(notificationService.getNotificationById(id)).thenReturn(emailNotification);

        final NotificationModel notificationModel = notificationResource.get(id);
        Assert.assertEquals(id, notificationModel.getId());
    }
}