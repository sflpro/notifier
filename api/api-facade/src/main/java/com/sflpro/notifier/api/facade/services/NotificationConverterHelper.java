package com.sflpro.notifier.api.facade.services;

import com.sflpro.notifier.api.model.email.EmailNotificationModel;
import com.sflpro.notifier.api.model.notification.NotificationClientType;
import com.sflpro.notifier.api.model.notification.NotificationModel;
import com.sflpro.notifier.api.model.notification.NotificationStateClientType;
import com.sflpro.notifier.api.model.push.PushNotificationModel;
import com.sflpro.notifier.api.model.push.PushNotificationRecipientModel;
import com.sflpro.notifier.api.model.sms.SmsNotificationModel;
import com.sflpro.notifier.db.entities.notification.Notification;
import com.sflpro.notifier.db.entities.notification.email.EmailNotification;
import com.sflpro.notifier.db.entities.notification.email.NotificationProperty;
import com.sflpro.notifier.db.entities.notification.push.PushNotification;
import com.sflpro.notifier.db.entities.notification.push.PushNotificationRecipient;
import com.sflpro.notifier.db.entities.notification.sms.SmsNotification;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Company: SFL LLC
 * Created on 16/11/2020
 *
 * @author Norik Aslanyan
 */
public class NotificationConverterHelper {

    public static NotificationModel convert(final Notification notification) {
        switch (notification.getType()) {
            case SMS:  return convertToSMSNotificationModel((SmsNotification) notification);
            case PUSH: return convertToPushNotificationModel((PushNotification) notification);
            case EMAIL: return convertToEmailNotificationModel((EmailNotification) notification);
            default: throw new IllegalArgumentException(String.format("Can't find converter for type:%s", notification.getType()));
        }
    }

    private static SmsNotificationModel convertToSMSNotificationModel(final SmsNotification notification) {
        final SmsNotificationModel smsNotificationModel = new SmsNotificationModel();
        convertToNotificationModel(notification, smsNotificationModel);
        smsNotificationModel.setRecipientNumber(notification.getRecipientMobileNumber());
        return smsNotificationModel;
    }

    private static PushNotificationModel convertToPushNotificationModel(final PushNotification pushNotification) {
        final PushNotificationModel pushNotificationModel = new PushNotificationModel();
        convertToNotificationModel(pushNotification, pushNotificationModel);
        final Map<String, String> properties = Optional.ofNullable(pushNotification.getProperties())
                .orElse(Collections.emptyList()).stream()
                .collect(Collectors.toMap(NotificationProperty::getPropertyKey, NotificationProperty::getPropertyValue));
        pushNotificationModel.setProperties(properties);
        final PushNotificationRecipient recipient = pushNotification.getRecipient();
        final PushNotificationRecipientModel recipientModel = new PushNotificationRecipientModel();
        recipientModel.setApplicationType(recipient.getApplicationType());
        recipientModel.setDeviceOperatingSystemType(recipient.getDeviceOperatingSystemType().name());
        pushNotificationModel.setRecipient(recipientModel);
        return pushNotificationModel;
    }

    private static EmailNotificationModel convertToEmailNotificationModel(final EmailNotification emailNotification) {
        final EmailNotificationModel emailNotificationModel = new EmailNotificationModel();
        convertToNotificationModel(emailNotification, emailNotificationModel);
        emailNotificationModel.setRecipientEmail(emailNotification.getRecipientEmail());
        emailNotificationModel.setSenderEmail(emailNotification.getSenderEmail());
        emailNotificationModel.setReplyToEmails(emailNotification.getReplyToEmails());
        return emailNotificationModel;
    }

    public static void convertToNotificationModel(final Notification notification, final NotificationModel model) {
        model.setId(notification.getId());
        model.setUuId(notification.getUuId());
        model.setBody(notification.getContent());
        model.setState(NotificationStateClientType.valueOf(notification.getState().name()));
        model.setSubject(notification.getSubject());
        model.setType(NotificationClientType.valueOf(notification.getType().name()));
    }
}
