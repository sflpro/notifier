package com.sflpro.notifier.externalclients.email.mandrill;

import com.microtripit.mandrillapp.lutung.controller.MandrillMessagesApi;
import com.sflpro.notifier.externalclients.email.mandrill.communicator.MandrillApiCommunicator;
import com.sflpro.notifier.externalclients.email.mandrill.communicator.MandrillApiCommunicatorImpl;
import com.sflpro.notifier.externalclients.email.test.AbstractEmailNotificationUnitTest;
import com.sflpro.notifier.spi.email.SimpleEmailMessage;
import com.sflpro.notifier.spi.email.TemplatedEmailMessage;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import java.util.Collections;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * Created by Hayk Mkrtchyan.
 * Date: 7/1/19
 * Time: 12:31 PM
 */
public class MandrillApiCommunicatorTest extends AbstractEmailNotificationUnitTest {

    private MandrillApiCommunicator mandrillApiCommunicator;

    @Mock
    private MandrillMessagesApi mandrillMessagesApi;

    @Mock
    private SimpleEmailMessage simpleEmailMessage;

    @Before
    public void prepare() {
        mandrillApiCommunicator = new MandrillApiCommunicatorImpl(mandrillMessagesApi);
    }

    @Test
    public void testSendEmailWithIllegalArguments() {
        assertThatThrownBy(() -> mandrillApiCommunicator.sendEmail(null))
                .isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> mandrillApiCommunicator.sendEmail(InvalidSimpleEmailMessage.withoutFrom()))
                .isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> mandrillApiCommunicator.sendEmail(InvalidSimpleEmailMessage.withoutTo()))
                .isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> mandrillApiCommunicator.sendEmail(InvalidSimpleEmailMessage.withoutSubject()))
                .isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> mandrillApiCommunicator.sendEmail(InvalidSimpleEmailMessage.withoutBody()))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    public void testSendEmailTemplateWithIllegalArguments() {
        assertThatThrownBy(() -> mandrillApiCommunicator.sendEmailTemplate(null))
                .isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> mandrillApiCommunicator.sendEmailTemplate(InvalidTemplatedEmailMessage.withoutFrom()))
                .isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> mandrillApiCommunicator.sendEmailTemplate(InvalidTemplatedEmailMessage.withoutTo()))
                .isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> mandrillApiCommunicator.sendEmailTemplate(InvalidTemplatedEmailMessage.withoutTemplateId()))
                .isInstanceOf(IllegalArgumentException.class);
    }

    private static final class InvalidSimpleEmailMessage implements SimpleEmailMessage {
        private final String from;
        private final String to;
        private final String subject;
        private final String body;

        private InvalidSimpleEmailMessage(final String from, final String to, final String subject, final String body) {
            this.from = from;
            this.to = to;
            this.subject = subject;
            this.body = body;
        }


        static SimpleEmailMessage withoutFrom() {
            return new InvalidSimpleEmailMessage(null, uuid(), uuid(), uuid());
        }

        static SimpleEmailMessage withoutTo() {
            return new InvalidSimpleEmailMessage(uuid(), null, uuid(), uuid());
        }

        static SimpleEmailMessage withoutSubject() {
            return new InvalidSimpleEmailMessage(uuid(), uuid(), null, uuid());
        }

        static SimpleEmailMessage withoutBody() {
            return new InvalidSimpleEmailMessage(uuid(), uuid(), uuid(), null);
        }

        @Override
        public String body() {
            return body;
        }

        @Override
        public String subject() {
            return subject;
        }

        @Override
        public String from() {
            return from;
        }

        @Override
        public String to() {
            return to;
        }
    }

    private static final class InvalidTemplatedEmailMessage implements TemplatedEmailMessage {
        private final String from;
        private final String to;
        private final String templateId;

        private InvalidTemplatedEmailMessage(final String from, final String to, final String templateId) {
            this.from = from;
            this.to = to;
            this.templateId = templateId;
        }


        static TemplatedEmailMessage withoutFrom() {
            return new InvalidTemplatedEmailMessage(null, uuid(), uuid());
        }

        static TemplatedEmailMessage withoutTo() {
            return new InvalidTemplatedEmailMessage(uuid(), null, uuid());
        }

        static TemplatedEmailMessage withoutTemplateId() {
            return new InvalidTemplatedEmailMessage(uuid(), uuid(), null);
        }

        static TemplatedEmailMessage withoutVariables() {
            return new InvalidTemplatedEmailMessage(uuid(), uuid(), null);
        }

        @Override
        public Map<String, ?> variables() {
            return Collections.emptyMap();
        }

        @Override
        public String templateId() {
            return templateId;
        }

        @Override
        public String from() {
            return from;
        }

        @Override
        public String to() {
            return to;
        }
    }
}
