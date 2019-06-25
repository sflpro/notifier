package com.sflpro.notifier.externalclients.sms.msgam;

import com.sflpro.notifier.externalclients.sms.test.AbstractSmsIntegrationTest;
import com.sflpro.notifier.spi.sms.SimpleSmsMessage;
import com.sflpro.notifier.spi.sms.SimpleSmsSender;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by Hayk Mkrtchyan.
 * Date: 6/20/19
 * Time: 6:19 PM
 */
public class MsgAmSimpleSmsSenderIntegrationTest extends AbstractSmsIntegrationTest {

    @Value("${integrationtest.send.real.sms:false}")
    private Boolean sendRealSms = Boolean.FALSE;

    @Autowired(required = false)
    @Qualifier("msgAmSimpleSmsSender")
    private SimpleSmsSender msgAmSimpleSmsSender;

    @Test
    public void testSend() {
        if (!sendRealSms) {
            return;
        }
        assertThat(msgAmSimpleSmsSender.send(SimpleSmsMessage.of(
                1L,
                "Rate.am",
                "+37494668425",
                "Hi!"
        ))).isNotNull();
    }
}
