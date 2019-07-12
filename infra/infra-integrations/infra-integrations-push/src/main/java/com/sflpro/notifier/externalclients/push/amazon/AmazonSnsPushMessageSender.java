package com.sflpro.notifier.externalclients.push.amazon;

import com.sflpro.notifier.externalclients.push.amazon.communicator.AmazonSnsApiCommunicator;
import com.sflpro.notifier.externalclients.push.amazon.model.request.SendPushNotificationRequestMessageInformation;
import com.sflpro.notifier.spi.push.PushMessage;
import com.sflpro.notifier.spi.push.PushMessageSender;
import com.sflpro.notifier.spi.push.PushMessageSendingResult;
import org.springframework.util.Assert;

/**
 * Created by Hayk Mkrtchyan.
 * Date: 7/3/19
 * Time: 11:04 AM
 */
public class AmazonSnsPushMessageSender implements PushMessageSender {

    private final AmazonSnsApiCommunicator amazonSnsApiCommunicator;

    AmazonSnsPushMessageSender(final AmazonSnsApiCommunicator amazonSnsApiCommunicator) {
        this.amazonSnsApiCommunicator = amazonSnsApiCommunicator;
    }

    @Override
    public PushMessageSendingResult send(final PushMessage message) {
        Assert.notNull(message, "Null was passed as an argument for parameter 'message'.");
        final SendPushNotificationRequestMessageInformation messageInformation = new SendPushNotificationRequestMessageInformation(
                message.subject(), message.body(), message.properties(), message.platformType()
        );
        return PushMessageSendingResult.of(amazonSnsApiCommunicator
                .sendPushNotification(messageInformation,
                        message.destinationRouteToken()
                )
                .getMessageId()
        );
    }
}
