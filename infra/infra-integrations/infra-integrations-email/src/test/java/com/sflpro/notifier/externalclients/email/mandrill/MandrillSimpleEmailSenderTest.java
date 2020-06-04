package com.sflpro.notifier.externalclients.email.mandrill;

import com.sflpro.notifier.externalclients.email.mandrill.communicator.MandrillApiCommunicator;
import com.sflpro.notifier.externalclients.email.test.AbstractEmailNotificationUnitTest;
import com.sflpro.notifier.spi.email.SimpleEmailMessage;
import com.sflpro.notifier.spi.email.SimpleEmailSender;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;

/**
 * Created by Hayk Mkrtchyan.
 * Date: 7/2/19
 * Time: 4:05 PM
 */
public class MandrillSimpleEmailSenderTest extends AbstractEmailNotificationUnitTest {

    private SimpleEmailSender mandrillSimpleEmailSender;

    @Mock
    private MandrillApiCommunicator mandrillApiCommunicator;

    @Before
    public void prepare(){
        mandrillSimpleEmailSender = new MandrillSimpleEmailSender(mandrillApiCommunicator);
    }

    @Test
    public void testSendWithInvalidArguments(){
       assertThatThrownBy(()-> mandrillSimpleEmailSender.send(null))
               .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    public void testSend(){
        final SimpleEmailMessage message = SimpleEmailMessage.of(uuid(), uuid(), uuid(), uuid(), Collections.emptyList());
        mandrillSimpleEmailSender.send(message);
        verify(mandrillApiCommunicator).sendEmail(message);
    }
}
