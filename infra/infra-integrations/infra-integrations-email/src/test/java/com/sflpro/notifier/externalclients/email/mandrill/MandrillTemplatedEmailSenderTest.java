package com.sflpro.notifier.externalclients.email.mandrill;

import com.sflpro.notifier.externalclients.email.mandrill.communicator.MandrillApiCommunicator;
import com.sflpro.notifier.externalclients.email.test.AbstractEmailNotificationUnitTest;
import com.sflpro.notifier.spi.email.TemplatedEmailMessage;
import com.sflpro.notifier.spi.email.TemplatedEmailMessageBuilder;
import com.sflpro.notifier.spi.email.TemplatedEmailSender;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;

/**
 * Created by Hayk Mkrtchyan.
 * Date: 7/2/19
 * Time: 4:12 PM
 */
public class MandrillTemplatedEmailSenderTest extends AbstractEmailNotificationUnitTest {

    private TemplatedEmailSender templatedEmailSender;

    @Mock
    private MandrillApiCommunicator mandrillApiCommunicator;

    @Before
    public void prepare() {
        templatedEmailSender = new MandrillTemplatedEmailSender(mandrillApiCommunicator);
    }

    @Test
    public void testSendWithInvalidArguments() {
        assertThatThrownBy(() -> templatedEmailSender.send(null))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    public void testSend() {
        final TemplatedEmailMessage message = new TemplatedEmailMessageBuilder(
                uuid(),
                uuid(),
                Collections.singleton(uuid()),
                uuid(),
                Collections.singletonMap(uuid(), uuid()),
                Collections.emptySet()
        ).build();
        templatedEmailSender.send(message);
        verify(mandrillApiCommunicator).sendEmailTemplate(message);
    }
}
