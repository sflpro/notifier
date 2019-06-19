package com.sflpro.notifier.services.helper;

import com.sflpro.notifier.db.entities.device.UserDevice;
import com.sflpro.notifier.db.entities.device.mobile.DeviceOperatingSystemType;
import com.sflpro.notifier.db.entities.notification.Notification;
import com.sflpro.notifier.db.entities.notification.NotificationProviderType;
import com.sflpro.notifier.db.entities.notification.NotificationState;
import com.sflpro.notifier.db.entities.notification.UserNotification;
import com.sflpro.notifier.db.entities.notification.email.EmailNotification;
import com.sflpro.notifier.db.entities.notification.email.EmailNotificationProperty;
import com.sflpro.notifier.db.entities.notification.push.*;
import com.sflpro.notifier.db.entities.notification.push.sns.PushNotificationSnsRecipient;
import com.sflpro.notifier.db.entities.notification.sms.SmsNotification;
import com.sflpro.notifier.db.entities.user.User;
import com.sflpro.notifier.services.device.dto.UserDeviceDto;
import com.sflpro.notifier.services.notification.dto.NotificationDto;
import com.sflpro.notifier.services.notification.dto.UserNotificationDto;
import com.sflpro.notifier.services.notification.dto.email.EmailNotificationDto;
import com.sflpro.notifier.services.notification.dto.email.EmailNotificationPropertyDto;
import com.sflpro.notifier.services.notification.dto.push.PushNotificationDto;
import com.sflpro.notifier.services.notification.dto.push.PushNotificationPropertyDto;
import com.sflpro.notifier.services.notification.dto.push.PushNotificationSubscriptionDto;
import com.sflpro.notifier.services.notification.dto.push.PushNotificationSubscriptionRequestDto;
import com.sflpro.notifier.services.notification.dto.push.sns.PushNotificationSnsRecipientDto;
import com.sflpro.notifier.services.notification.dto.sms.SmsNotificationDto;
import com.sflpro.notifier.services.user.dto.UserDto;
import org.junit.Assert;

import java.util.*;
import java.util.stream.Collectors;

import static org.junit.Assert.assertNotNull;

/**
 * User: Ruben Dilanyan
 * Company: SFL LLC
 * Date: 12/24/15
 * Time: 6:54 PM
 */
public class ServicesImplTestHelper {

    /* Constructors */
    public ServicesImplTestHelper() {
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
        Assert.assertEquals(userDto.getUuId(), user.getUuId());
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

    public void assertUserDevice(final UserDevice userDevice, final UserDeviceDto userDeviceDto) {
        assertNotNull(userDevice);
        Assert.assertEquals(userDeviceDto.getUuId(), userDevice.getUuId());
        Assert.assertEquals(userDeviceDto.getOsType(), userDevice.getOsType());
    }

    /* Email notification */
    public EmailNotificationDto createEmailNotificationDto() {
        final EmailNotificationDto notificationDto = new EmailNotificationDto();
        notificationDto.setRecipientEmail("dummy_recipient@dummy.com");
        notificationDto.setSenderEmail("dummy_sender@dummy.com");
        notificationDto.setClientIpAddress("127.0.0.1");
        notificationDto.setContent("YoYoYo");
        notificationDto.setSubject("YoYo");
        notificationDto.setProviderType(NotificationProviderType.SMTP_SERVER);
        notificationDto.setTemplateName("YoTemplate");
        notificationDto.setSecureProperties(Collections.singletonMap("token", UUID.randomUUID().toString()));
        return notificationDto;
    }

    public EmailNotification createEmailNotification() {
        return createEmailNotification(createEmailNotificationDto());
    }

    public EmailNotification createEmailNotification(final EmailNotificationDto notificationDto) {
        final EmailNotification notification = new EmailNotification(true);
        final List<EmailNotificationPropertyDto> propertyDtos = createEmailNotificationPropertyDtos(3);
        final Set<EmailNotificationProperty> properties = propertyDtos.stream().map(dto -> {
            final EmailNotificationProperty emailNotificationProperty = new EmailNotificationProperty();
            dto.updateDomainEntityProperties(emailNotificationProperty);
            return emailNotificationProperty;
        }).collect(Collectors.toSet());
        notificationDto.updateDomainEntityProperties(notification);
        notification.setProperties(properties);
        return notification;
    }

    public void assertEmailNotification(final EmailNotification notification, final EmailNotificationDto notificationDto) {
        assertNotification(notification, notificationDto);
        Assert.assertEquals(notificationDto.getRecipientEmail(), notification.getRecipientEmail());
        Assert.assertEquals(notificationDto.getSenderEmail(), notification.getSenderEmail());
        Assert.assertEquals(notificationDto.getProviderType(), notification.getProviderType());
    }

    public List<EmailNotificationPropertyDto> createEmailNotificationPropertyDtos(final int count) {
        final List<EmailNotificationPropertyDto> propertyDtos = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            propertyDtos.add(new EmailNotificationPropertyDto("key" + i, "value" + i));
        }
        return propertyDtos;
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
        Assert.assertEquals(notificationDto.getRecipientMobileNumber(), notification.getRecipientMobileNumber());
        Assert.assertEquals(notificationDto.getProviderType(), notification.getProviderType());
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
        Assert.assertEquals(notificationDto.getClientIpAddress(), notification.getClientIpAddress());
        Assert.assertEquals(notificationDto.getContent(), notification.getContent());
        Assert.assertEquals(notificationDto.getSubject(), notification.getSubject());
        Assert.assertEquals(notificationDto.getType(), notification.getType());
        Assert.assertEquals(NotificationState.CREATED, notification.getState());
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
        Assert.assertEquals(pushNotificationPropertyDto.getPropertyKey(), pushNotificationProperty.getPropertyKey());
        Assert.assertEquals(pushNotificationPropertyDto.getPropertyValue(), pushNotificationProperty.getPropertyValue());
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

    public void assertUserNotification(final UserNotification notification) {
        assertNotNull(notification);
    }

    /* Push notification request */
    public PushNotificationSubscriptionDto createPushNotificationSubscriptionDto() {
        final PushNotificationSubscriptionDto subscriptionDto = new PushNotificationSubscriptionDto();
        return subscriptionDto;
    }

    public PushNotificationSubscription createPushNotificationSubscription(final PushNotificationSubscriptionDto subscriptionDto) {
        final PushNotificationSubscription subscription = new PushNotificationSubscription();
        subscriptionDto.updateDomainEntityProperties(subscription);
        return subscription;
    }

    public PushNotificationSubscription createPushNotificationSubscription() {
        return createPushNotificationSubscription(createPushNotificationSubscriptionDto());
    }

    public void assertPushNotificationSubscription(final PushNotificationSubscription subscription) {
        assertNotNull(subscription);
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
        Assert.assertEquals(recipient.getType(), recipientDto.getType());
        Assert.assertEquals(recipient.getDestinationRouteToken(), recipientDto.getDestinationRouteToken());
        Assert.assertEquals(recipient.getDeviceOperatingSystemType(), recipientDto.getDeviceOperatingSystemType());
        Assert.assertEquals(recipient.getApplicationType(), recipientDto.getApplicationType());
        Assert.assertEquals(PushNotificationRecipientStatus.ENABLED, recipient.getStatus());
        Assert.assertEquals(recipient.getPlatformApplicationArn(), recipientDto.getPlatformApplicationArn());
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

    public void assertPushNotificationSubscriptionRequest(final PushNotificationSubscriptionRequest request, final PushNotificationSubscriptionRequestDto requestDto) {
        assertNotNull(request);
        Assert.assertEquals(requestDto.getUserDeviceToken(), request.getUserDeviceToken());
        Assert.assertEquals(requestDto.isSubscribe(), request.isSubscribe());
        Assert.assertEquals(requestDto.getPreviousSubscriptionRequestUuId(), request.getPreviousSubscriptionRequestUuId());
        Assert.assertEquals(requestDto.getApplicationType(), request.getApplicationType());
        Assert.assertEquals(PushNotificationSubscriptionRequestState.CREATED, request.getState());
    }
}
