package com.sflpro.notifier.externalclients.sms.nikitamobile;

import com.sflpro.notifier.externalclients.sms.test.AbstractSmsIntegrationTest;
import com.sflpro.notifier.spi.sms.SimpleSmsMessage;
import com.sflpro.notifier.spi.sms.SimpleSmsSender;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.context.TestPropertySource;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by Hayk Mkrtchyan.
 * Date: 6/20/19
 * Time: 6:19 PM
 */
@TestPropertySource(properties = {
        "nikitamobile.enabled=true",
        "nikitamobile.api.url=http://31.47.195.66:80/broker",
        "nikitamobile.login=sslpro",
        "nikitamobile.password=sslpro",
        "sms.sender=Hishecum"
})
public class NikitaMobileSimpleSmsSenderIntegrationTest extends AbstractSmsIntegrationTest {

    @Value("${integrationtest.send.real.sms:true}")
    private Boolean sendRealSms = Boolean.FALSE;

    @Value("${sms.sender}")
    private String sender;

    @Autowired(required = false)
    @Qualifier("nikitamobileSimpleSmsSender")
    private SimpleSmsSender nikitamobileSimpleSmsSender;

    @Test
    public void testSend() {
        if (!sendRealSms) {
            return;
        }
        assertThat(nikitamobileSimpleSmsSender.send(SimpleSmsMessage.of(
                1L,
                sender,
                "+37494668425",
                "Hi!"
        ))).isNotNull();
    }
}
