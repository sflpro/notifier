package com.sflpro.notifier.api.internal.facade.helper;

import com.sflpro.notifier.core.api.internal.model.device.DeviceOperatingSystemClientType;
import com.sflpro.notifier.core.api.internal.model.email.EmailNotificationModel;
import com.sflpro.notifier.core.api.internal.model.email.request.CreateEmailNotificationRequest;
import com.sflpro.notifier.core.api.internal.model.notification.NotificationClientType;
import com.sflpro.notifier.core.api.internal.model.notification.NotificationModel;
import com.sflpro.notifier.core.api.internal.model.notification.NotificationStateClientType;
import com.sflpro.notifier.core.api.internal.model.push.PushNotificationModel;
import com.sflpro.notifier.core.api.internal.model.push.PushNotificationPropertyModel;
import com.sflpro.notifier.core.api.internal.model.push.PushNotificationRecipientModel;
import com.sflpro.notifier.core.api.internal.model.push.request.CreatePushNotificationRequest;
import com.sflpro.notifier.core.api.internal.model.push.request.UpdatePushNotificationSubscriptionRequest;
import com.sflpro.notifier.core.api.internal.model.sms.SmsNotificationModel;
import com.sflpro.notifier.core.api.internal.model.sms.request.CreateSmsNotificationRequest;
import com.sflpro.notifier.services.device.dto.UserDeviceDto;
import com.sflpro.notifier.services.device.model.UserDevice;
import com.sflpro.notifier.services.device.model.mobile.DeviceOperatingSystemType;
import com.sflpro.notifier.services.notification.dto.NotificationDto;
import com.sflpro.notifier.services.notification.dto.UserNotificationDto;
import com.sflpro.notifier.services.notification.dto.email.EmailNotificationDto;
import com.sflpro.notifier.services.notification.dto.push.PushNotificationDto;
import com.sflpro.notifier.services.notification.dto.push.PushNotificationPropertyDto;
import com.sflpro.notifier.services.notification.dto.push.PushNotificationSubscriptionRequestDto;
import com.sflpro.notifier.services.notification.dto.push.sns.PushNotificationSnsRecipientDto;
import com.sflpro.notifier.services.notification.dto.sms.SmsNotificationDto;
import com.sflpro.notifier.services.notification.model.Notification;
import com.sflpro.notifier.services.notification.model.NotificationProviderType;
import com.sflpro.notifier.services.notification.model.NotificationState;
import com.sflpro.notifier.services.notification.model.UserNotification;
import com.sflpro.notifier.services.notification.model.email.EmailNotification;
import com.sflpro.notifier.services.notification.model.push.*;
import com.sflpro.notifier.services.notification.model.push.sns.PushNotificationSnsRecipient;
import com.sflpro.notifier.services.notification.model.sms.SmsNotification;
import com.sflpro.notifier.services.user.dto.UserDto;
import com.sflpro.notifier.services.user.model.User;
import org.apache.commons.lang3.mutable.MutableInt;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class ServiceFacadeImplTestHelper {

    /* Constructors */
    public ServiceFacadeImplTestHelper() {
    }

    /* User */
    public UserDto createUserDto() {
        final UserDto userDto = new UserDto();
        userDto.setUuId(UUID.randomUUID().toString());
        return userDto;
    }

    public User createUser(final UserDto userDto) {
        final User user = new User();
        userDto.updateDomainEntityProperties(user);
        return user;
    }

    public User createUser() {
        return createUser(createUserDto());
    }

    public void assertUser(final User user, final UserDto userDto) {
        assertNotNull(user);
        assertEquals(userDto.getUuId(), user.getUuId());
    }

    /* Create email request */
    public CreateEmailNotificationRequest createCreateEmailNotificationRequest() {
        final CreateEmailNotificationRequest request = new CreateEmailNotificationRequest();
        request.setRecipientEmail("dummy_recipient@dummy.com");
        request.setSenderEmail("dummy_sender@dummy.com");
        request.setSubject("Email subject");
        request.setBody("Email body");
        request.setClientIpAddress("127.0.0.1");
        request.setUserUuId("UGITYDTGUIGFITYDTDTFYKTYCL");
        return request;
    }

    /* Create SMS request */
    public CreateSmsNotificationRequest createCreateSmsNotificationRequest() {
        final CreateSmsNotificationRequest request = new CreateSmsNotificationRequest();
        request.setRecipientNumber("+37455000000");
        request.setBody("SMS body");
        request.setClientIpAddress("127.0.0.1");
        request.setUserUuId("UGITYDTGUIGFITYDTDTFYKTYCL");
        return request;
    }

    /* Create Push notifications request */
    public CreatePushNotificationRequest createCreatePushNotificationRequest() {
        final CreatePushNotificationRequest request = new CreatePushNotificationRequest();
        request.setBody("Push notification body");
        request.setClientIpAddress("127.0.0.1");
        request.setUserUuId("UGITYDTGUIGFITYDTDTFYKTYCL");
        request.setProperties(createProperties(10));
        return request;
    }

    private List<PushNotificationPropertyModel> createProperties(final int count) {
        final List<PushNotificationPropertyModel> propertyModels = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            propertyModels.add(createPushNotificationPropertyModel(i));
        }
        return propertyModels;
    }

    private PushNotificationPropertyModel createPushNotificationPropertyModel(final int index) {
        return new PushNotificationPropertyModel("Property key - " + index, "Property value - " + index);
    }

    /* Create Push notifications subscription request */
    public UpdatePushNotificationSubscriptionRequest createUpdatePushNotificationSubscriptionRequest() {
        final UpdatePushNotificationSubscriptionRequest request = new UpdatePushNotificationSubscriptionRequest();
        request.setUserUuId(UUID.randomUUID().toString());
        request.setDeviceOperatingSystemType(DeviceOperatingSystemClientType.IOS);
        request.setApplication("mobileapp");
        request.setDeviceUuId(UUID.randomUUID().toString());
        request.setLastUsedSubscriptionRequestUuId(UUID.randomUUID().toString());
        request.setSubscribe(Boolean.TRUE);
        request.setUserDeviceToken(UUID.randomUUID().toString());
        return request;
    }

    /* Email notification */
    public EmailNotificationDto createEmailNotificationDto() {
        final EmailNotificationDto notificationDto = new EmailNotificationDto();
        notificationDto.setRecipientEmail("dummy_recipient@dummy.com");
        notificationDto.setSenderEmail("dummy_sender@sflpro.com");
        notificationDto.setClientIpAddress("127.0.0.1");
        notificationDto.setContent("YoYoYo");
        notificationDto.setSubject("YoYo");
        notificationDto.setProviderType(NotificationProviderType.SMTP_SERVER);
        return notificationDto;
    }

    public EmailNotification createEmailNotification() {
        return createEmailNotification(createEmailNotificationDto());
    }

    public EmailNotification createEmailNotification(final EmailNotificationDto notificationDto) {
        final EmailNotification notification = new EmailNotification(true);
        notificationDto.updateDomainEntityProperties(notification);
        return notification;
    }

    public void assertEmailNotification(final EmailNotification notification, final EmailNotificationDto notificationDto) {
        assertNotification(notification, notificationDto);
        assertEquals(notificationDto.getRecipientEmail(), notification.getRecipientEmail());
        assertEquals(notificationDto.getSenderEmail(), notification.getSenderEmail());
        assertEquals(notificationDto.getProviderType(), notification.getProviderType());
    }

    /* SMS notification */
    public SmsNotificationDto createSmsNotificationDto() {
        final SmsNotificationDto notificationDto = new SmsNotificationDto();
        notificationDto.setRecipientMobileNumber("+37455000000");
        notificationDto.setClientIpAddress("127.0.0.1");
        notificationDto.setContent("YoYoYo");
        notificationDto.setSubject(null);
        notificationDto.setProviderType(NotificationProviderType.TWILLIO);
        return notificationDto;
    }

    public SmsNotification createSmsNotification() {
        return createSmsNotification(createSmsNotificationDto());
    }

    public SmsNotification createSmsNotification(final SmsNotificationDto notificationDto) {
        final SmsNotification notification = new SmsNotification(true);
        notificationDto.updateDomainEntityProperties(notification);
        return notification;
    }

    public void assertSmsNotification(final SmsNotification notification, final SmsNotificationDto notificationDto) {
        assertNotification(notification, notificationDto);
        assertEquals(notificationDto.getRecipientMobileNumber(), notification.getRecipientMobileNumber());
        assertEquals(notificationDto.getProviderType(), notification.getProviderType());
    }

    /* Push notification */
    public PushNotificationDto createPushNotificationDto() {
        final PushNotificationDto notificationDto = new PushNotificationDto();
        notificationDto.setClientIpAddress("127.0.0.1");
        notificationDto.setContent("YoYoYo");
        notificationDto.setSubject("YoYo");
        return notificationDto;
    }

    public PushNotification createPushNotification() {
        return createPushNotification(createPushNotificationDto());
    }

    public PushNotification createPushNotification(final PushNotificationDto notificationDto) {
        final PushNotification notification = new PushNotification(true);
        notificationDto.updateDomainEntityProperties(notification);
        return notification;
    }

    public void assertPushNotification(final PushNotification notification, final PushNotificationDto notificationDto) {
        assertNotification(notification, notificationDto);
    }

    public void assertNotification(final Notification notification, final NotificationDto<? extends Notification> notificationDto) {
        assertNotNull(notification);
        assertEquals(notificationDto.getClientIpAddress(), notification.getClientIpAddress());
        assertEquals(notificationDto.getContent(), notification.getContent());
        assertEquals(notificationDto.getSubject(), notification.getSubject());
        assertEquals(notificationDto.getType(), notification.getType());
        assertEquals(NotificationState.CREATED, notification.getState());
    }

    /* User notification */
    public UserNotificationDto createUserNotificationDto() {
        return new UserNotificationDto();
    }

    public UserNotification createUserNotification() {
        return createUserNotification(createUserNotificationDto());
    }

    public UserNotification createUserNotification(final UserNotificationDto notificationDto) {
        final UserNotification notification = new UserNotification();
        notificationDto.updateDomainEntityProperties(notification);
        return notification;
    }

    public void assertUserNotification(final UserNotification notification, final UserNotificationDto notificationDto) {
        assertNotNull(notification);
    }

    /* Assert SMS notification model */
    public void assertSmsNotificationModel(final SmsNotification smsNotification, final SmsNotificationModel smsNotificationModel) {
        assertNotificationModel(smsNotification, smsNotificationModel);
        assertEquals(NotificationClientType.SMS, smsNotificationModel.getType());
        assertEquals(smsNotification.getRecipientMobileNumber(), smsNotificationModel.getRecipientNumber());
    }

    /* Assert Email notification model */
    public void assertEmailNotificationModel(final EmailNotification emailNotification, final EmailNotificationModel emailNotificationModel) {
        assertNotificationModel(emailNotification, emailNotificationModel);
        assertEquals(NotificationClientType.EMAIL, emailNotificationModel.getType());
        assertEquals(emailNotification.getSenderEmail(), emailNotificationModel.getSenderEmail());
        assertEquals(emailNotification.getRecipientEmail(), emailNotificationModel.getRecipientEmail());
    }

    /* Assert PUSH notification model */
    public void assertPushNotificationModel(final PushNotification pushNotification, final PushNotificationModel pushNotificationModel) {
        assertNotificationModel(pushNotification, pushNotificationModel);
        assertEquals(NotificationClientType.PUSH, pushNotificationModel.getType());
        // Assert recipient
        assertPushNotificationRecipientModel(pushNotification.getRecipient(), pushNotificationModel.getRecipient());
        // Assert properties
        assertEquals(pushNotification.getProperties().size(), pushNotificationModel.getProperties().size());
        final MutableInt counter = new MutableInt(0);
        pushNotification.getProperties().forEach(property -> {
            final PushNotificationPropertyModel propertyModel = pushNotificationModel.getProperties().get(counter.intValue());
            assertPushNotificationPropertyModel(property, propertyModel);
            counter.increment();
        });
    }

    public void assertPushNotificationPropertyModel(final PushNotificationProperty pushNotificationProperty, final PushNotificationPropertyModel pushNotificationPropertyModel) {
        assertEquals(pushNotificationProperty.getPropertyKey(), pushNotificationPropertyModel.getPropertyKey());
        assertEquals(pushNotificationProperty.getPropertyValue(), pushNotificationPropertyModel.getPropertyValue());
    }

    public void assertPushNotificationRecipientModel(final PushNotificationRecipient recipient, final PushNotificationRecipientModel recipientModel) {
        assertEquals(recipient.getApplicationType(), recipientModel.getApplicationType());
        assertEquals(recipient.getDeviceOperatingSystemType().name(), recipientModel.getDeviceOperatingSystemType());
    }

    public void assertNotificationModel(final Notification notification, final NotificationModel notificationModel) {
        assertEquals(notification.getUuId(), notificationModel.getUuId());
        assertEquals(notification.getContent(), notificationModel.getBody());
        assertEquals(notification.getSubject(), notificationModel.getSubject());
        assertEquals(NotificationStateClientType.valueOf(notification.getState().name()), notificationModel.getState());
    }

    /* Push notification recipient */
    public PushNotificationSnsRecipientDto createPushNotificationSnsRecipientDto() {
        final PushNotificationSnsRecipientDto recipientDto = new PushNotificationSnsRecipientDto();
        recipientDto.setDestinationRouteToken("JHYFTTDRSDESYRSESRDYESESESRTDESHDSSDD");
        recipientDto.setDeviceOperatingSystemType(DeviceOperatingSystemType.IOS);
        recipientDto.setApplicationType("application");
        recipientDto.setPlatformApplicationArn("arn:aws:sns:eu-west-1:6554779700788:app/GCM/prod_sfl_android");
        return recipientDto;
    }

    public PushNotificationSnsRecipient createPushNotificationSnsRecipient(final PushNotificationSnsRecipientDto recipientDto) {
        final PushNotificationSnsRecipient recipient = new PushNotificationSnsRecipient(true);
        recipientDto.updateDomainEntityProperties(recipient);
        return recipient;
    }

    public PushNotificationSnsRecipient createPushNotificationSnsRecipient() {
        return createPushNotificationSnsRecipient(createPushNotificationSnsRecipientDto());
    }

    public void assertPushNotificationSnsRecipient(final PushNotificationSnsRecipient recipient, final PushNotificationSnsRecipientDto recipientDto) {
        assertNotNull(recipient);
        assertEquals(recipient.getType(), recipientDto.getType());
        assertEquals(recipient.getDestinationRouteToken(), recipientDto.getDestinationRouteToken());
        assertEquals(recipient.getDeviceOperatingSystemType(), recipientDto.getDeviceOperatingSystemType());
        assertEquals(recipient.getApplicationType(), recipientDto.getApplicationType());
        assertEquals(recipient.getStatus(), PushNotificationRecipientStatus.ENABLED);
        assertEquals(recipient.getPlatformApplicationArn(), recipientDto.getPlatformApplicationArn());
    }

    /* Push notification property */
    public PushNotificationPropertyDto createPushNotificationPropertyDto() {
        final PushNotificationPropertyDto pushNotificationPropertyDto = new PushNotificationPropertyDto();
        pushNotificationPropertyDto.setPropertyKey("testPropertyKey");
        pushNotificationPropertyDto.setPropertyValue("testPropertyValue");
        return pushNotificationPropertyDto;
    }

    public PushNotificationProperty createPushNotificationProperty(final PushNotificationPropertyDto notificationPropertyDto) {
        final PushNotificationProperty pushNotificationProperty = new PushNotificationProperty();
        notificationPropertyDto.updateDomainEntityProperties(pushNotificationProperty);
        return pushNotificationProperty;
    }

    public PushNotificationProperty createPushNotificationProperty() {
        return createPushNotificationProperty(createPushNotificationPropertyDto());
    }

    public void assertPushNotificationProperty(final PushNotificationProperty pushNotificationProperty, final PushNotificationPropertyDto pushNotificationPropertyDto) {
        assertEquals(pushNotificationPropertyDto.getPropertyKey(), pushNotificationProperty.getPropertyKey());
        assertEquals(pushNotificationPropertyDto.getPropertyValue(), pushNotificationProperty.getPropertyValue());
    }

    /* Push notification subscription request */
    public PushNotificationSubscriptionRequestDto createPushNotificationSubscriptionRequestDto() {
        final PushNotificationSubscriptionRequestDto subscriptionDto = new PushNotificationSubscriptionRequestDto();
        subscriptionDto.setSubscribe(true);
        subscriptionDto.setUserDeviceToken("KULGYIFTIDTDIR^R(CJFDRFGRTD^%586durtX");
        subscriptionDto.setPreviousSubscriptionRequestUuId("*Y&)^$%$&#&%%&^(^$%*TLGFTRTCRDR");
        subscriptionDto.setApplicationType("application");
        return subscriptionDto;
    }

    public PushNotificationSubscriptionRequest createPushNotificationSubscriptionRequest(final PushNotificationSubscriptionRequestDto requestDto) {
        final PushNotificationSubscriptionRequest subscription = new PushNotificationSubscriptionRequest(true);
        requestDto.updateDomainEntityProperties(subscription);
        return subscription;
    }

    public PushNotificationSubscriptionRequest createPushNotificationSubscriptionRequest() {
        return createPushNotificationSubscriptionRequest(createPushNotificationSubscriptionRequestDto());
    }

    /* User mobile device */
    public UserDeviceDto createUserDeviceDto() {
        final UserDeviceDto userDeviceDto = new UserDeviceDto();
        userDeviceDto.setUuId(UUID.randomUUID().toString());
        userDeviceDto.setOsType(DeviceOperatingSystemType.IOS);
        return userDeviceDto;
    }

    public UserDevice createUserDevice(final UserDeviceDto userDeviceDto) {
        final UserDevice userDevice = new UserDevice();
        userDeviceDto.updateDomainEntityProperties(userDevice);
        return userDevice;
    }

    public UserDevice createUserDevice() {
        return createUserDevice(createUserDeviceDto());
    }

}
