package com.sflpro.notifier.externalclients.push.firebase;

import com.sflpro.notifier.externalclients.push.test.AbstractPushNotificationIntegrationTest;
import com.sflpro.notifier.spi.push.PlatformType;
import com.sflpro.notifier.spi.push.PushMessage;
import com.sflpro.notifier.spi.push.PushMessageSender;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.TestPropertySource;

import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by Hayk Mkrtchyan.
 * Date: 7/5/19
 * Time: 6:01 PM
 */
@TestPropertySource(properties = "firebase.push.enabled=true")
public class FirebasePushMessageSenderIntegrationTest extends AbstractPushNotificationIntegrationTest {


    @Autowired
    private PushMessageSender pushMessageSender;

    @Ignore
    @Test
    public void testSend(){
        assertThat(pushMessageSender.send(PushMessage.of(
                "AAAAaq6D4Jg:APA91bEuyitADDXu9tm7NtyIoSU3PmjFDC0F5rzRWl2afEkkv6aBBmt60NBAAN5YrVFA2i18MdLdtBpXF5Zr-JNS4CrlPnfB3JXn4_UPjRh6dPuX92LoK8f0hvCaeKSkBRcyjt1DUKtb", // TODO remove secure variables and private keys from project
                "Hey!",
                "Hey amigo!",
                PlatformType.GCM,
                Collections.emptyMap()
        )).messageId()).isNotNull();
    }
}
