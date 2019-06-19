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
import com.sflpro.notifier.db.entities.notification.sms.SmsNotificationProperty;
import com.sflpro.notifier.db.entities.user.User;
import com.sflpro.notifier.services.device.UserDeviceService;
import com.sflpro.notifier.services.device.dto.UserDeviceDto;
import com.sflpro.notifier.services.notification.UserNotificationService;
import com.sflpro.notifier.services.notification.dto.NotificationDto;
import com.sflpro.notifier.services.notification.dto.UserNotificationDto;
import com.sflpro.notifier.services.notification.dto.email.EmailNotificationDto;
import com.sflpro.notifier.services.notification.dto.email.EmailNotificationPropertyDto;
import com.sflpro.notifier.services.notification.dto.push.*;
import com.sflpro.notifier.services.notification.dto.push.sns.PushNotificationSnsRecipientDto;
import com.sflpro.notifier.services.notification.dto.sms.SmsNotificationDto;
import com.sflpro.notifier.services.notification.dto.sms.SmsNotificationPropertyDto;
import com.sflpro.notifier.services.notification.email.EmailNotificationService;
import com.sflpro.notifier.services.notification.push.PushNotificationService;
import com.sflpro.notifier.services.notification.push.PushNotificationSubscriptionProcessingService;
import com.sflpro.notifier.services.notification.push.PushNotificationSubscriptionRequestService;
import com.sflpro.notifier.services.notification.push.PushNotificationSubscriptionService;
import com.sflpro.notifier.services.notification.push.sns.PushNotificationSnsRecipientService;
import com.sflpro.notifier.services.notification.sms.SmsNotificationService;
import com.sflpro.notifier.services.user.UserService;
import com.sflpro.notifier.services.user.dto.UserDto;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.Assert.*;

/**
 * User: Ruben Dilanyan
 * Company: SFL LLC
 * Date: 12/24/15
 * Time: 7:16 PM
 */
@Component
public class ServicesTestHelper {

    /* Constants */
    private static final int IOS_TOKEN_LENGTH = 64;

    private static final String HEX_CHARACTER_SET = "ABCDEF0123456789";

    private static final String YO_YO = "YoYo";

    private static final String EMAIL_FROM = "dummy_sender@dummy.com";

    private static final String EMAIL_TO = "dummy_recipient@dummy.com";

    private static final String IP_ADDRESS = "127.0.0.1";

    private static final String MOBILE_NUMBER = "+37455000000";

    private static final int PUSH_NOTIFICATION_PROPERTIES_COUNT = 10;

    public static final String APPLICATION_TYPE = "customer";

    /* Dependencies */
    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private UserService userService;

    @Autowired
    private PushNotificationSubscriptionService pushNotificationSubscriptionService;

    @Autowired
    private PushNotificationSnsRecipientService pushNotificationSnsRecipientService;

    @Autowired
    private PushNotificationService pushNotificationService;

    @Autowired
    private PushNotificationSubscriptionRequestService pushNotificationSubscriptionRequestService;

    @Autowired
    private PushNotificationSubscriptionProcessingService pushNotificationSubscriptionProcessingService;

    @Autowired
    private EmailNotificationService emailNotificationService;

    @Autowired
    private SmsNotificationService smsNotificationService;

    @Autowired
    private UserNotificationService userNotificationService;

    @Autowired
    private UserDeviceService userDeviceService;

    /* Constructors */
    public ServicesTestHelper() {
        super();
    }

    /* User */
    public UserDto createUserDto() {
        final UserDto userDto = new UserDto();
        userDto.setUuId(UUID.randomUUID().toString());
        return userDto;
    }

    public User createUser(final UserDto userDto) {
        return userService.createUser(userDto);
    }

    public User createUser() {
        return createUser(createUserDto());
    }

    public void assertUser(final User user, final UserDto userDto) {
        assertNotNull(user);
        assertNotNull(user.getId());
        assertEquals(userDto.getUuId(), user.getUuId());
    }

    /* User device */
    public UserDeviceDto createUserDeviceDto() {
        final UserDeviceDto userDeviceDto = new UserDeviceDto();
        userDeviceDto.setOsType(DeviceOperatingSystemType.ANDROID);
        userDeviceDto.setUuId(UUID.randomUUID().toString());
        return userDeviceDto;
    }

    public UserDevice createUserDevice() {
        return createUserDevice(createUser(), createUserDeviceDto());
    }

    public UserDevice createUserDevice(final User user, final UserDeviceDto userDeviceDto) {
        return userDeviceService.createUserDevice(user.getId(), userDeviceDto);
    }

    public void assertUserDevice(final UserDevice userDevice, final UserDeviceDto userDeviceDto) {
        assertNotNull(userDevice);
        assertNotNull(userDevice.getId());
        assertEquals(userDeviceDto.getUuId(), userDevice.getUuId());
        Assert.assertEquals(userDeviceDto.getOsType(), userDevice.getOsType());
    }

    /* Push notification subscription */
    public PushNotificationSubscriptionDto createPushNotificationSubscriptionDto() {
        return new PushNotificationSubscriptionDto();
    }

    public PushNotificationSubscription createPushNotificationSubscription(final User user, final PushNotificationSubscriptionDto subscriptionDto) {
        return pushNotificationSubscriptionService.createPushNotificationSubscription(user.getId(), subscriptionDto);
    }

    public PushNotificationSubscription createPushNotificationSubscription() {
        return createPushNotificationSubscription(createUser(), createPushNotificationSubscriptionDto());
    }

    public void assertPushNotificationSubscription(final PushNotificationSubscription subscription) {
        assertNotNull(subscription);
        assertNotNull(subscription.getId());
    }

    /* Push notification recipient */
    public PushNotificationSnsRecipientDto createPushNotificationSnsRecipientDto() {
        final PushNotificationSnsRecipientDto recipientDto = new PushNotificationSnsRecipientDto();
        recipientDto.setDestinationRouteToken("JHYFTTDRSDESYRSESRDYESESESRTDESHDSSDD");
        recipientDto.setDeviceOperatingSystemType(DeviceOperatingSystemType.IOS);
        recipientDto.setApplicationType(APPLICATION_TYPE);
        recipientDto.setPlatformApplicationArn("arn:aws:sns:eu-west-1:6554779700788:app/GCM/prod_sfl_android");
        return recipientDto;
    }

    public PushNotificationSnsRecipient createPushNotificationSnsRecipient(final PushNotificationSubscription subscription, final PushNotificationSnsRecipientDto recipientDto) {
        return pushNotificationSnsRecipientService.createPushNotificationRecipient(subscription.getId(), recipientDto);
    }

    public PushNotificationSnsRecipient createPushNotificationSnsRecipient() {
        return createPushNotificationSnsRecipient(createPushNotificationSubscription(), createPushNotificationSnsRecipientDto());
    }

    public void assertPushNotificationSnsRecipient(final PushNotificationSnsRecipient recipient, final PushNotificationSnsRecipientDto recipientDto) {
        assertNotNull(recipient);
        Assert.assertEquals(recipient.getType(), recipientDto.getType());
        Assert.assertEquals(recipient.getDestinationRouteToken(), recipientDto.getDestinationRouteToken());
        Assert.assertEquals(recipient.getDeviceOperatingSystemType(), recipientDto.getDeviceOperatingSystemType());
        Assert.assertEquals(recipient.getApplicationType(), recipientDto.getApplicationType());
        Assert.assertEquals(PushNotificationRecipientStatus.ENABLED, recipient.getStatus());
        assertEquals(recipient.getPlatformApplicationArn(), recipientDto.getPlatformApplicationArn());
    }

    public PushNotificationRecipient createPushNotificationRecipientForIOSDeviceAndRegisterWithAmazonSns(final User user, final String iOSDeviceToken) {
        // Create user mobile device
        final UserDeviceDto userMobileDeviceDto = createUserDeviceDto();
        userMobileDeviceDto.setOsType(DeviceOperatingSystemType.IOS);
        final UserDevice userMobileDevice = createUserDevice(user, userMobileDeviceDto);
        // Create token processing parameters
        final PushNotificationSubscriptionProcessingParameters parameters = new PushNotificationSubscriptionProcessingParameters();
        parameters.setUserId(user.getId());
        parameters.setDeviceToken(iOSDeviceToken);
        parameters.setSubscribe(true);
        parameters.setUserMobileDeviceId(userMobileDevice.getId());
        parameters.setCurrentPushNotificationProviderType(null);
        parameters.setCurrentProviderToken(null);
        parameters.setApplicationType(APPLICATION_TYPE);
        // Process subscription
        final PushNotificationRecipient recipient = pushNotificationSubscriptionProcessingService.processPushNotificationSubscriptionChange(parameters);
        flush();
        return recipient;
    }

    /* Push notification request request */
    public PushNotificationSubscriptionRequestDto createPushNotificationSubscriptionRequestDto() {
        final PushNotificationSubscriptionRequestDto subscriptionDto = new PushNotificationSubscriptionRequestDto();
        subscriptionDto.setSubscribe(true);
        subscriptionDto.setUserDeviceToken("KULGYIFTIDTDIR^R(CJFDRFGRTD^%586durtX");
        subscriptionDto.setPreviousSubscriptionRequestUuId("*Y&)^$%$&#&%%&^(^$%*TLGFTRTCRDR");
        subscriptionDto.setApplicationType(APPLICATION_TYPE);
        return subscriptionDto;
    }

    public PushNotificationSubscriptionRequest createPushNotificationSubscriptionRequest(final User user, final UserDevice userMobileDevice, final PushNotificationSubscriptionRequestDto requestDto) {
        return pushNotificationSubscriptionRequestService.createPushNotificationSubscriptionRequest(user.getId(), userMobileDevice.getId(), requestDto);
    }

    public PushNotificationSubscriptionRequest createPushNotificationSubscriptionRequest() {
        final User user = createUser();
        final UserDevice userMobileDevice = createUserDevice(user, createUserDeviceDto());
        return createPushNotificationSubscriptionRequest(user, userMobileDevice, createPushNotificationSubscriptionRequestDto());
    }

    public void assertPushNotificationSubscriptionRequest(final PushNotificationSubscriptionRequest request, final PushNotificationSubscriptionRequestDto requestDto) {
        assertNotNull(request);
        assertNotNull(request.getId());
        assertEquals(requestDto.getUserDeviceToken(), request.getUserDeviceToken());
        assertEquals(requestDto.isSubscribe(), request.isSubscribe());
        assertEquals(requestDto.getPreviousSubscriptionRequestUuId(), request.getPreviousSubscriptionRequestUuId());
        assertEquals(requestDto.getApplicationType(), request.getApplicationType());
        Assert.assertEquals(PushNotificationSubscriptionRequestState.CREATED, request.getState());
    }

    /* Email notification */
    public EmailNotificationDto createEmailNotificationDto() {
        final EmailNotificationDto notificationDto = new EmailNotificationDto();
        notificationDto.setRecipientEmail(EMAIL_TO);
        notificationDto.setSenderEmail(EMAIL_FROM);
        notificationDto.setClientIpAddress(IP_ADDRESS);
        notificationDto.setContent(YO_YO + YO_YO);
        notificationDto.setSubject(YO_YO);
        notificationDto.setProviderType(NotificationProviderType.SMTP_SERVER);
        notificationDto.setTemplateName(YO_YO + "_template");
        return notificationDto;
    }

    public EmailNotification createEmailNotification() {
        return createEmailNotification(createEmailNotificationDto(), createEmailNotificationPropertyDtos(5));
    }

    public EmailNotification createEmailNotification(final EmailNotificationDto notificationDto, final List<EmailNotificationPropertyDto> emailNotificationPropertyDtos) {
        return emailNotificationService.createAndSendEmailNotification(notificationDto, emailNotificationPropertyDtos);
    }

    public void assertEmailNotification(final EmailNotification notification, final EmailNotificationDto notificationDto, final List<EmailNotificationPropertyDto> emailNotificationPropertyDtos) {
        assertNotification(notification, notificationDto);
        assertEquals(notificationDto.getRecipientEmail(), notification.getRecipientEmail());
        assertEquals(notificationDto.getSenderEmail(), notification.getSenderEmail());
        assertEquals(notificationDto.getTemplateName(), notification.getTemplateName());
        Assert.assertEquals(notificationDto.getProviderType(), notification.getProviderType());
        assertEquals(emailNotificationPropertyDtos.size(), notification.getProperties().size());
        assertEquals(!notificationDto.getSecureProperties().isEmpty(),notification.hasSecureProperties());
        emailNotificationPropertyDtos.forEach(emailNotificationPropertyDto -> {
            final Optional<EmailNotificationProperty> emailNotificationProperty = notification.getProperties()
                    .stream()
                    .filter(property -> property.getPropertyKey().equals(emailNotificationPropertyDto.getPropertyKey()))
                    .findFirst();
            assertTrue(emailNotificationProperty.isPresent());
            assertEquals(emailNotificationPropertyDto.getPropertyValue(), emailNotificationProperty.get().getPropertyValue());
            assertEquals(notification.getId(), emailNotificationProperty.get().getEmailNotification().getId());
        });
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
        notificationDto.setRecipientMobileNumber(MOBILE_NUMBER);
        notificationDto.setClientIpAddress(IP_ADDRESS);
        notificationDto.setContent(YO_YO + YO_YO);
        notificationDto.setSubject(YO_YO);
        notificationDto.setProviderType(NotificationProviderType.TWILLIO);
        return notificationDto;
    }

    public SmsNotification createSmsNotification() {
        return createSmsNotification(createSmsNotificationDto(), createSmsNotificationPropertyDtos(5));
    }

    public SmsNotification createSmsNotification(final SmsNotificationDto notificationDto, final List<SmsNotificationPropertyDto> smsNotificationPropertyDtos) {
        return smsNotificationService.createSmsNotification(notificationDto, smsNotificationPropertyDtos);
    }

    public void assertSmsNotification(final SmsNotification notification, final SmsNotificationDto notificationDto, final List<SmsNotificationPropertyDto> smsNotificationPropertyDtos) {
        assertNotification(notification, notificationDto);
        assertEquals(notificationDto.getRecipientMobileNumber(), notification.getRecipientMobileNumber());
        Assert.assertEquals(notificationDto.getProviderType(), notification.getProviderType());
        assertEquals(notificationDto.getTemplateName(), notification.getTemplateName());
        assertEquals(smsNotificationPropertyDtos.size(), notification.getProperties().size());
        assertEquals(!notificationDto.getSecureProperties().isEmpty(),notification.hasSecureProperties());
        smsNotificationPropertyDtos.forEach(propertyDto -> {
            final Optional<SmsNotificationProperty> smsNotificationProperty = notification.getProperties()
                    .stream()
                    .filter(property -> property.getPropertyKey().equals(propertyDto.getPropertyKey()))
                    .findFirst();
            assertTrue(smsNotificationProperty.isPresent());
            assertEquals(propertyDto.getPropertyValue(), smsNotificationProperty.get().getPropertyValue());
            assertEquals(notification.getId(), smsNotificationProperty.get().getSmsNotification().getId());
        });
    }

    public List<SmsNotificationPropertyDto> createSmsNotificationPropertyDtos(final int count) {
        final List<SmsNotificationPropertyDto> propertyDtos = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            propertyDtos.add(new SmsNotificationPropertyDto("key" + i, "value" + i));
        }
        return propertyDtos;
    }

    /* Push notification */
    public PushNotificationDto createPushNotificationDto() {
        final PushNotificationDto notificationDto = new PushNotificationDto();
        notificationDto.setClientIpAddress(IP_ADDRESS);
        notificationDto.setContent(YO_YO + YO_YO);
        notificationDto.setSubject(YO_YO);
        return notificationDto;
    }

    public PushNotification createPushNotification() {
        return createPushNotification(createPushNotificationSnsRecipient(), createPushNotificationDto(), createPushNotificationPropertyDTOs(PUSH_NOTIFICATION_PROPERTIES_COUNT));
    }

    public PushNotification createPushNotification(final PushNotificationRecipient recipient, final PushNotificationDto notificationDto, final List<PushNotificationPropertyDto> pushNotificationPropertyDTos) {
        return pushNotificationService.createNotification(recipient.getId(), notificationDto, pushNotificationPropertyDTos);
    }

    public void assertPushNotification(final PushNotification notification, final PushNotificationDto notificationDto) {
        assertNotification(notification, notificationDto);
    }

    public void assertNotification(final Notification notification, final NotificationDto<? extends Notification> notificationDto) {
        assertNotNull(notification);
        assertNotNull(notification.getId());
        assertEquals(notificationDto.getClientIpAddress(), notification.getClientIpAddress());
        assertEquals(notificationDto.getContent(), notification.getContent());
        assertEquals(notificationDto.getSubject(), notification.getSubject());
        assertEquals(notificationDto.getType(), notification.getType());
        Assert.assertEquals(NotificationState.CREATED, notification.getState());
    }

    /* Push notifications properties */
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

    public void assertPushNotificationProperty(final PushNotificationProperty pushNotificationProperty, final PushNotificationPropertyDto pushNotificationPropertyDto) {
        assertEquals(pushNotificationPropertyDto.getPropertyKey(), pushNotificationProperty.getPropertyKey());
        assertEquals(pushNotificationPropertyDto.getPropertyValue(), pushNotificationProperty.getPropertyValue());
    }

    public List<PushNotificationPropertyDto> createPushNotificationPropertyDTOs(final int count) {
        final List<PushNotificationPropertyDto> pushNotificationPropertyDTOs = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            final PushNotificationPropertyDto pushNotificationPropertyDto = createPushNotificationPropertyDto();
            pushNotificationPropertyDto.setPropertyKey(pushNotificationPropertyDto.getPropertyKey() + "_" + i);
            pushNotificationPropertyDto.setPropertyValue(pushNotificationPropertyDto.getPropertyValue() + "_" + i);
            // Add to the list of notifications
            pushNotificationPropertyDTOs.add(pushNotificationPropertyDto);
        }
        return pushNotificationPropertyDTOs;
    }

    /* User notification */
    public UserNotificationDto createUserNotificationDto() {
        return new UserNotificationDto();
    }

    public UserNotification createUserNotification() {
        return createUserNotification(createUserNotificationDto());
    }

    public UserNotification createUserNotification(final UserNotificationDto notificationDto) {
        final User user = createUser();
        final Notification notification = createSmsNotification();
        return userNotificationService.createUserNotification(user.getId(), notification.getId(), notificationDto);
    }

    public void assertUserNotification(final UserNotification notification) {
        assertNotNull(notification);
    }

    /* Device tokens */
    public String generateIOSToken() {
        return RandomStringUtils.random(IOS_TOKEN_LENGTH, HEX_CHARACTER_SET);
    }

    /* Utility methods */
    private void flush() {
        entityManager.flush();
    }
}
