package com.sflpro.notifier.externalclients.push.amazon.communicator;

import com.amazonaws.services.sns.AmazonSNSClient;
import com.amazonaws.services.sns.model.PublishRequest;
import com.amazonaws.services.sns.model.PublishResult;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sflpro.notifier.externalclients.push.amazon.model.request.SendPushNotificationRequestMessageInformation;
import com.sflpro.notifier.externalclients.push.test.AbstractPushNotificationUnitTest;
import com.sflpro.notifier.spi.push.PlatformType;
import org.junit.Test;
import org.mockito.Mock;

import java.util.Collections;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by Hayk Mkrtchyan.
 * Date: 7/3/19
 * Time: 2:50 PM
 */
public class AmazonSnsApiCommunicatorImplTest extends AbstractPushNotificationUnitTest {

    @Mock
    private AmazonSNSClient amazonSNSClient;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    public void testSendPushNotificationWithInvalidArgs() {
        final AmazonSnsApiCommunicator communicator = new AmazonSnsApiCommunicatorImpl(
                amazonSNSClient,
                false
        );
        assertThatThrownBy(() -> communicator.sendPushNotification(null, uuid()))
                .isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> communicator.sendPushNotification(new SendPushNotificationRequestMessageInformation(
                uuid(), uuid(), Collections.emptyMap(), PlatformType.APNS
        ), null))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    public void testSendPushNotificationWhenAmazonSnsDevelopmentModeEnabled() {
        final AmazonSnsApiCommunicator communicator = new AmazonSnsApiCommunicatorImpl(
                amazonSNSClient,
                true
        );
        final String key = uuid();
        final String value = uuid();
        final SendPushNotificationRequestMessageInformation messageInformation = new SendPushNotificationRequestMessageInformation(
                uuid(), uuid(), Collections.singletonMap(key, value), PlatformType.APNS
        );
        final String deviceEndpointArn = uuid();
        final String messageId = uuid();
        when(amazonSNSClient.publish(isA(PublishRequest.class))).then(invocation -> {
            final PublishRequest request = invocation.getArgument(0);
            assertThat(request)
                    .hasFieldOrPropertyWithValue("subject", messageInformation.getMessageSubject())
                    .hasFieldOrPropertyWithValue("targetArn", deviceEndpointArn)
                    .hasFieldOrPropertyWithValue("messageStructure", "json");
            final Map<String, String> payload = objectMapper.readerFor(Map.class).readValue(request.getMessage());
            final Map<String, String> props = ((Map<String, Map<String, String>>) objectMapper.readerFor(Map.class).readValue(payload.get("APNS_SANDBOX"))).get("aps");
            assertThat(props.get(key))
                    .isEqualTo(value);
            assertThat(props.get("alert")).isEqualTo(messageInformation.getMessageBody());
            final PublishResult result = new PublishResult();
            result.setMessageId(messageId);
            return result;
        });
        communicator.sendPushNotification(messageInformation, deviceEndpointArn);
        verify(amazonSNSClient).publish(isA(PublishRequest.class));
    }

    @Test
    public void testSendPushNotificationWhenAmazonSnsDevelopmentModeDisabled() {
        final AmazonSnsApiCommunicator communicator = new AmazonSnsApiCommunicatorImpl(
                amazonSNSClient,
                false
        );
        final String key = uuid();
        final String value = uuid();
        final SendPushNotificationRequestMessageInformation messageInformation = new SendPushNotificationRequestMessageInformation(
                uuid(), uuid(), Collections.singletonMap(key, value), PlatformType.APNS
        );
        final String deviceEndpointArn = uuid();
        final String messageId = uuid();
        when(amazonSNSClient.publish(isA(PublishRequest.class))).then(invocation -> {
            final PublishRequest request = invocation.getArgument(0);
            assertThat(request)
                    .hasFieldOrPropertyWithValue("subject", messageInformation.getMessageSubject())
                    .hasFieldOrPropertyWithValue("targetArn", deviceEndpointArn)
                    .hasFieldOrPropertyWithValue("messageStructure", "json");
            final Map<String, String> payload = objectMapper.readerFor(Map.class).readValue(request.getMessage());
            final Map<String, String> props = ((Map<String, Map<String, String>>) objectMapper.readerFor(Map.class).readValue(payload.get("APNS"))).get("aps");
            assertThat(props.get(key))
                    .isEqualTo(value);
            assertThat(props.get("alert"))
                    .isEqualTo(messageInformation.getMessageBody());
            final PublishResult result = new PublishResult();
            result.setMessageId(messageId);
            return result;
        });
        communicator.sendPushNotification(messageInformation, deviceEndpointArn);
        verify(amazonSNSClient).publish(isA(PublishRequest.class));
    }

    @Test
    public void testSendPushNotificationForGCM() {
        final AmazonSnsApiCommunicator communicator = new AmazonSnsApiCommunicatorImpl(
                amazonSNSClient,
                false
        );
        final String key = uuid();
        final String value = uuid();
        final SendPushNotificationRequestMessageInformation messageInformation = new SendPushNotificationRequestMessageInformation(
                uuid(), uuid(), Collections.singletonMap(key, value), PlatformType.GCM
        );
        final String deviceEndpointArn = uuid();
        final String messageId = uuid();
        when(amazonSNSClient.publish(isA(PublishRequest.class))).then(invocation -> {
            final PublishRequest request = invocation.getArgument(0);
            assertThat(request)
                    .hasFieldOrPropertyWithValue("subject", messageInformation.getMessageSubject())
                    .hasFieldOrPropertyWithValue("targetArn", deviceEndpointArn)
                    .hasFieldOrPropertyWithValue("messageStructure", "json");
            final Map<String, String> payload = objectMapper.readerFor(Map.class).readValue(request.getMessage());
            final Map<String, Map<String, String>> rootObj = objectMapper.readerFor(Map.class).readValue(payload.get("GCM"));
            final Map<String, String> data = rootObj.get("data");
            assertThat(data.get(key))
                    .isEqualTo(value);
            assertThat(data.get("message"))
                    .isEqualTo(messageInformation.getMessageBody());
            final PublishResult result = new PublishResult();
            result.setMessageId(messageId);
            return result;
        });
        communicator.sendPushNotification(messageInformation, deviceEndpointArn);
        verify(amazonSNSClient).publish(isA(PublishRequest.class));
    }

}
