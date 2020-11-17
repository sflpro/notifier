package com.sflpro.notifier.api.facade.services;

import com.sflpro.notifier.api.model.email.EmailNotificationModel;
import com.sflpro.notifier.api.model.notification.NotificationModel;
import com.sflpro.notifier.api.model.push.PushNotificationModel;
import com.sflpro.notifier.api.model.sms.SmsNotificationModel;
import com.sflpro.notifier.db.entities.device.mobile.DeviceOperatingSystemType;
import com.sflpro.notifier.db.entities.notification.Notification;
import com.sflpro.notifier.db.entities.notification.NotificationProviderType;
import com.sflpro.notifier.db.entities.notification.NotificationState;
import com.sflpro.notifier.db.entities.notification.email.EmailNotification;
import com.sflpro.notifier.db.entities.notification.email.NotificationProperty;
import com.sflpro.notifier.db.entities.notification.push.PushNotification;
import com.sflpro.notifier.db.entities.notification.push.PushNotificationProviderType;
import com.sflpro.notifier.db.entities.notification.push.PushNotificationRecipient;
import com.sflpro.notifier.db.entities.notification.sms.SmsNotification;
import org.junit.Assert;
import org.junit.Test;

import java.util.Collections;

import static org.apache.commons.lang3.RandomStringUtils.random;
import static org.apache.commons.lang3.RandomUtils.nextInt;
import static org.apache.commons.lang3.RandomUtils.nextLong;

public class NotificationConverterHelperImplTest {

    @Test
    public void convertSmsNotification() {
        final SmsNotification notification = new SmsNotification();
        generateRandomNotification(notification);
        notification.setRecipientMobileNumber(random(5));


        final SmsNotificationModel notificationModel = (SmsNotificationModel) NotificationConverterHelper.convert(notification);
        assertAbstractNotification(notification, notificationModel);
        Assert.assertEquals(notification.getRecipientMobileNumber(), notificationModel.getRecipientNumber());
    }

    @Test
    public void convertPushNotification() {
        final PushNotification notification = new PushNotification();
        generateRandomNotification(notification);
        final PushNotificationRecipient recipient = new PushNotificationRecipient();
        recipient.setApplicationType(random(5));
        recipient.setDeviceOperatingSystemType(DeviceOperatingSystemType.values()[nextInt(0, DeviceOperatingSystemType.values().length)]);
        notification.setRecipient(recipient);


        final PushNotificationModel notificationModel = (PushNotificationModel) NotificationConverterHelper.convert(notification);
        assertAbstractNotification(notification, notificationModel);
        Assert.assertEquals(notification.getRecipient().getApplicationType(), notificationModel.getRecipient().getApplicationType());
        Assert.assertEquals(notification.getRecipient().getDeviceOperatingSystemType().name(), notificationModel.getRecipient().getDeviceOperatingSystemType());
    }

    @Test
    public void convertEmailNotification() {
        final EmailNotification notification = new EmailNotification();
        generateRandomNotification(notification);
        notification.setSenderEmail(random(5));
        notification.setRecipientEmail( random(5));
        notification.setReplyToEmails(Collections.singleton( random(5)));


        final EmailNotificationModel notificationModel = (EmailNotificationModel) NotificationConverterHelper.convert(notification);
        assertAbstractNotification(notification, notificationModel);
        Assert.assertEquals(notification.getSenderEmail(), notificationModel.getSenderEmail());
        Assert.assertEquals(notification.getRecipientEmail(), notificationModel.getRecipientEmail());
        Assert.assertEquals(notification.getReplyToEmails(), notificationModel.getReplyToEmails());
    }

    private void assertAbstractNotification(Notification notification, NotificationModel notificationModel) {
        Assert.assertEquals(notification.getId(), notificationModel.getId());
        Assert.assertEquals(notification.getContent(), notificationModel.getBody());
        Assert.assertEquals(notification.getSubject(), notificationModel.getSubject());
        Assert.assertEquals(notification.getUuId(), notificationModel.getUuId());
        Assert.assertEquals(notification.getType().name(), notificationModel.getType().name());
        Assert.assertEquals(notification.getState().name(), notificationModel.getState().name());
    }

    private void generateRandomNotification(final Notification notification) {
        notification.setId(nextLong(1, 100));
        notification.setContent(random(5));
        notification.setUuId(random(10));
        notification.setSubject(random(5));
        notification.setState(NotificationState.values()[nextInt(0, NotificationState.values().length)]);
        notification.setProperties(Collections.singletonList(new NotificationProperty(random(4), random(5))));
        notification.setProviderType(NotificationProviderType.values()[nextInt(0, NotificationProviderType.values().length)]);
    }
}