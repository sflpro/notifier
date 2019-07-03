package com.sflpro.notifier.externalclients.push.amazon;

import com.sflpro.notifier.externalclients.push.amazon.communicator.AmazonSnsApiCommunicator;
import com.sflpro.notifier.externalclients.push.amazon.model.request.SendPushNotificationRequestMessageInformation;
import com.sflpro.notifier.externalclients.push.amazon.model.response.SendPushNotificationResponse;
import com.sflpro.notifier.externalclients.push.test.AbstractPushNotificationUnitTest;
import com.sflpro.notifier.spi.push.PlatformType;
import com.sflpro.notifier.spi.push.PushMessage;
import com.sflpro.notifier.spi.push.PushMessageSender;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by Hayk Mkrtchyan.
 * Date: 7/3/19
 * Time: 2:29 PM
 */
public class AmazonSnsPushMessageSenderTest extends AbstractPushNotificationUnitTest {

    private PushMessageSender pushMessageSender;

    @Mock
    private AmazonSnsApiCommunicator amazonSnsApiCommunicator;

    @Before
    public void prepare() {
        pushMessageSender = new AmazonSnsPushMessageSender(amazonSnsApiCommunicator);
    }

    @Test
    public void testSendWithInvalidArguments() {
        Assertions.assertThatThrownBy(() -> pushMessageSender.send(null))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    public void testSend() {
        final PushMessage message = PushMessage.of(
                uuid(), uuid(), uuid(), PlatformType.APNS, Collections.singletonMap(uuid(), uuid()));
        final String messageId = uuid();
        when(amazonSnsApiCommunicator.sendPushNotification(isA(SendPushNotificationRequestMessageInformation.class),eq(message.deviceEndpointArn()))).then(invocation -> {
            assertThat((SendPushNotificationRequestMessageInformation)invocation.getArgument(0))
                    .hasFieldOrPropertyWithValue("messageSubject",message.subject())
                    .hasFieldOrPropertyWithValue("messageBody",message.body())
                    .hasFieldOrPropertyWithValue("messageProperties",message.properties())
                    .hasFieldOrPropertyWithValue("amazonSNSPlatformType",message.platformType());
            assertThat((String)invocation.getArgument(1)).isEqualTo(message.deviceEndpointArn());
            return new SendPushNotificationResponse(messageId);
        });
        assertThat( pushMessageSender.send(message).messageId()).isEqualTo(messageId);
        verify(amazonSnsApiCommunicator).sendPushNotification(isA(SendPushNotificationRequestMessageInformation.class),eq(message.deviceEndpointArn()));
    }
}
