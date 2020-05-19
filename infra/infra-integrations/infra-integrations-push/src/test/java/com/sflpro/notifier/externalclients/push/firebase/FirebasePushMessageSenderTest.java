package com.sflpro.notifier.externalclients.push.firebase;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.sflpro.notifier.externalclients.push.test.AbstractPushNotificationUnitTest;
import com.sflpro.notifier.spi.push.PlatformType;
import com.sflpro.notifier.spi.push.PushMessage;
import com.sflpro.notifier.spi.push.PushMessageSender;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.*;

/**
 * Created by Hayk Mkrtchyan.
 * Date: 7/8/19
 * Time: 4:04 PM
 */
public class FirebasePushMessageSenderTest extends AbstractPushNotificationUnitTest {

    private PushMessageSender pushMessageSender;

    @Mock
    private FirebaseMessaging firebaseMessaging;
    @Mock
    private Properties defaultAndroidConfig;
    @Mock
    private Properties defaultApnsConfig;

    @Before
    public void prepare() {
        pushMessageSender = new FirebasePushMessageSender(firebaseMessaging, defaultAndroidConfig, defaultApnsConfig);
    }

    @Test
    public void testSendWithIllegalArguments() {
        assertThatThrownBy(() -> pushMessageSender.send(null)).isInstanceOf(IllegalArgumentException.class);
        verifyZeroInteractions(firebaseMessaging, defaultAndroidConfig, defaultApnsConfig);
    }

    @Test
    public void testSendToAndroidDeviceWithDefaultConfigsOnly() throws FirebaseMessagingException {
        final Map<String, String> properties = new HashMap<>();
        properties.put(uuid(), uuid());
        final PushMessage message = PushMessage.of(
                uuid(),
                uuid(),
                uuid(),
                PlatformType.GCM,
                properties
        );
        final String messageId = uuid();
        final String ttlKey = "ttl";
        final String priorityKey = "priority";
        final String collapseKey = "collapseKey";
        final String restrictedPackageNameKey = "restrictedPackageName";
        when(defaultAndroidConfig.getProperty(ttlKey)).thenReturn("10");
        when(defaultAndroidConfig.getProperty(priorityKey)).thenReturn("HIGH");
        when(defaultAndroidConfig.getProperty(collapseKey)).thenReturn(uuid());
        when(defaultAndroidConfig.getProperty(restrictedPackageNameKey)).thenReturn(uuid());
        when(firebaseMessaging.send(isA(Message.class))).thenAnswer(invocation -> {
            checkProperties(message, invocation.getArgument(0));
            return messageId;
        });
        assertThat(pushMessageSender.send(message).messageId()).isEqualTo(messageId);
        verify(firebaseMessaging).send(isA(Message.class));
        verify(defaultAndroidConfig).getProperty(ttlKey);
        verify(defaultAndroidConfig).getProperty(priorityKey);
        verify(defaultAndroidConfig).getProperty(collapseKey);
        verify(defaultAndroidConfig).getProperty(restrictedPackageNameKey);
        verifyZeroInteractions(defaultApnsConfig);
    }

    @Test
    public void testSendToAndroidDeviceWithProvidedConfigs() throws FirebaseMessagingException {
        final Map<String, String> properties = new HashMap<>();
        properties.put(uuid(), uuid());
        final PushMessage message = PushMessage.of(
                uuid(),
                uuid(),
                uuid(),
                PlatformType.GCM,
                properties
        );
        final String messageId = uuid();
        final String ttlKey = "ttl";
        final String priorityKey = "priority";
        final String collapseKey = "collapseKey";
        final String restrictedPackageNameKey = "restrictedPackageName";
        properties.put(ttlKey, "10");
        properties.put(priorityKey, "HIGH");
        properties.put(collapseKey, uuid());
        properties.put(restrictedPackageNameKey, uuid());
        properties.put("title", uuid());
        properties.put("body", uuid());
        when(firebaseMessaging.send(isA(Message.class))).thenAnswer(invocation -> {
            checkProperties(message, invocation.getArgument(0));
            return messageId;
        });
        assertThat(pushMessageSender.send(message).messageId()).isEqualTo(messageId);
        verify(firebaseMessaging).send(isA(Message.class));
        verifyZeroInteractions(defaultApnsConfig, defaultAndroidConfig);
    }

    @Test
    public void testSendToIOSDeviceWithDefaultConfigsOnly() throws FirebaseMessagingException {
        final Map<String, String> properties = new HashMap<>();
        properties.put(uuid(), uuid());
        final PushMessage pushMessage = PushMessage.of(
                uuid(),
                uuid(),
                uuid(),
                PlatformType.APNS,
                properties
        );
        final String messageId = uuid();
        final String badgeKey = "badge";
        final String categoryKey = "category";
        final String soundKey = "sound";
        final String alertKey = "alert";
        when(defaultApnsConfig.getProperty(badgeKey)).thenReturn("1");
        when(defaultApnsConfig.getProperty(categoryKey)).thenReturn(uuid());
        when(defaultApnsConfig.getProperty(soundKey)).thenReturn(uuid());
        when(defaultApnsConfig.getProperty(alertKey)).thenReturn(uuid());
        when(firebaseMessaging.send(isA(Message.class))).thenAnswer(invocation -> {
            checkProperties(pushMessage, invocation.getArgument(0));
            return messageId;
        });
        assertThat(pushMessageSender.send(pushMessage).messageId()).isEqualTo(messageId);
        verify(firebaseMessaging).send(isA(Message.class));
        verify(defaultApnsConfig).getProperty(badgeKey);
        verify(defaultApnsConfig).getProperty(categoryKey);
        verify(defaultApnsConfig).getProperty(soundKey);
        verify(defaultApnsConfig).getProperty(alertKey);
        verifyZeroInteractions(defaultAndroidConfig, defaultApnsConfig);
    }

    @Test
    public void testSendToIOSDeviceWithProvidedConfigs() throws FirebaseMessagingException {
        final String messageId = uuid();
        final String badgeKey = "badge";
        final String categoryKey = "category";
        final String soundKey = "sound";
        final String alertKey = "alert";
        final Map<String, String> properties = new HashMap<>();
        properties.put(uuid(), uuid());
        properties.put(badgeKey, "1");
        properties.put(categoryKey, uuid());
        properties.put(soundKey, uuid());
        properties.put(alertKey, uuid());
        final PushMessage pushMessage = PushMessage.of(
                uuid(),
                uuid(),
                uuid(),
                PlatformType.APNS,
                properties
        );

        when(firebaseMessaging.send(isA(Message.class))).thenAnswer(invocation -> {
            checkProperties(pushMessage, invocation.getArgument(0));
            return messageId;
        });
        assertThat(pushMessageSender.send(pushMessage).messageId()).isEqualTo(messageId);
        verify(firebaseMessaging).send(isA(Message.class));
        verifyZeroInteractions(defaultAndroidConfig, defaultApnsConfig);
    }

    private static void checkProperties(final PushMessage pushMessage, final Message message) {
        assertThat(message)
                .hasFieldOrPropertyWithValue("data", pushMessage.properties())
                .hasFieldOrPropertyWithValue("token", pushMessage.destinationRouteToken());
    }
}
