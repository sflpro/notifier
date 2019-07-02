package com.sflpro.notifier.externalclients.email.mandrill;

import com.microtripit.mandrillapp.lutung.controller.MandrillMessagesApi;
import com.microtripit.mandrillapp.lutung.model.MandrillApiError;
import com.microtripit.mandrillapp.lutung.view.MandrillMessage;
import com.microtripit.mandrillapp.lutung.view.MandrillMessageStatus;
import com.sflpro.notifier.externalclients.email.mandrill.communicator.MandrillApiCommunicator;
import com.sflpro.notifier.externalclients.email.mandrill.communicator.MandrillApiCommunicatorImpl;
import com.sflpro.notifier.externalclients.email.mandrill.exception.MandrillMessageInvalidException;
import com.sflpro.notifier.externalclients.email.mandrill.exception.MandrillMessageRejectedException;
import com.sflpro.notifier.externalclients.email.test.AbstractEmailNotificationUnitTest;
import com.sflpro.notifier.spi.email.SimpleEmailMessage;
import com.sflpro.notifier.spi.email.TemplatedEmailMessage;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.Map;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by Hayk Mkrtchyan.
 * Date: 7/1/19
 * Time: 12:31 PM
 */
public class MandrillApiCommunicatorImplTest extends AbstractEmailNotificationUnitTest {

    private MandrillApiCommunicator mandrillApiCommunicator;

    @Mock
    private MandrillMessagesApi mandrillMessagesApi;

    @Mock
    private MandrillMessageStatus mandrillMessageStatus;

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

    @Test
    public void testSendEmailTemplateStatusRejected() throws IOException, MandrillApiError {
        final TemplatedEmailMessage templatedEmailMessage = TemplatedEmailMessage.of(uuid(), uuid(), uuid(), Collections.singletonMap(uuid(), uuid()));
        when(mandrillMessageStatus.getStatus()).thenReturn("rejected");
        when(mandrillMessageStatus.getRejectReason()).thenReturn("Something went wrong.");
        when(mandrillMessagesApi.sendTemplate(
                eq(templatedEmailMessage.templateId()),
                isNull(),
                isA(MandrillMessage.class),
                eq(Boolean.FALSE))
        ).thenReturn(new MandrillMessageStatus[]{mandrillMessageStatus});
        assertThatThrownBy(() -> mandrillApiCommunicator.sendEmailTemplate(templatedEmailMessage))
                .isInstanceOf(MandrillMessageRejectedException.class);
        verify(mandrillMessageStatus).getStatus();
        verify(mandrillMessageStatus).getRejectReason();
        verify(mandrillMessagesApi).sendTemplate(
                eq(templatedEmailMessage.templateId()),
                isNull(),
                isA(MandrillMessage.class),
                eq(Boolean.FALSE)
        );
    }

    @Test
    public void testSendEmailTemplateStatusInvalid() throws IOException, MandrillApiError {
        final TemplatedEmailMessage templatedEmailMessage = TemplatedEmailMessage.of(uuid(), uuid(), uuid(), Collections.singletonMap(uuid(), uuid()));
        when(mandrillMessageStatus.getStatus()).thenReturn("invalid");
        when(mandrillMessagesApi.sendTemplate(
                eq(templatedEmailMessage.templateId()),
                isNull(),
                isA(MandrillMessage.class),
                eq(Boolean.FALSE))
        ).thenReturn(new MandrillMessageStatus[]{mandrillMessageStatus});
        assertThatThrownBy(() -> mandrillApiCommunicator.sendEmailTemplate(templatedEmailMessage))
                .isInstanceOf(MandrillMessageInvalidException.class);
        verify(mandrillMessageStatus).getStatus();
        verify(mandrillMessagesApi).sendTemplate(
                eq(templatedEmailMessage.templateId()),
                isNull(),
                isA(MandrillMessage.class),
                eq(Boolean.FALSE)
        );
    }

    @Test
    public void testSendEmailTemplateStatusSuccess() throws IOException, MandrillApiError {
        final String variableName = uuid();
        final TemplatedEmailMessage templatedEmailMessage = TemplatedEmailMessage.of(
                uuid(),
                uuid(),
                uuid(),
                Collections.singletonMap(variableName, uuid())
        );
        when(mandrillMessageStatus.getStatus()).thenReturn(uuid());
        when(mandrillMessageStatus.getEmail()).thenReturn(templatedEmailMessage.to());
        when(mandrillMessageStatus.getId()).thenReturn(uuid());
        when(mandrillMessagesApi.sendTemplate(
                eq(templatedEmailMessage.templateId()),
                isNull(),
                isA(MandrillMessage.class),
                eq(Boolean.FALSE))
        ).thenAnswer(invocation -> {
            final MandrillMessage mandrillMessage = invocation.getArgument(2);
            assertThat(mandrillMessage)
                    .hasFieldOrPropertyWithValue("fromEmail", templatedEmailMessage.from());
            assertThat(mandrillMessage.getTo().stream()
                    .map(MandrillMessage.Recipient::getEmail)
                    .anyMatch(templatedEmailMessage.to()::equals)
            ).isTrue();
            assertThat(mandrillMessage.getMergeVars().stream()
                    .map(MandrillMessage.MergeVarBucket::getVars)
                    .flatMap(Arrays::stream)
                    .collect(Collectors.toMap(MandrillMessage.MergeVar::getName, MandrillMessage.MergeVar::getContent))
                    .get(variableName)
            ).isEqualTo(templatedEmailMessage.variables().get(variableName));
            return new MandrillMessageStatus[]{mandrillMessageStatus};
        });
        mandrillApiCommunicator.sendEmailTemplate(templatedEmailMessage);
        verify(mandrillMessageStatus).getStatus();
        verify(mandrillMessageStatus).getEmail();
        verify(mandrillMessageStatus).getId();
        verify(mandrillMessagesApi).sendTemplate(
                eq(templatedEmailMessage.templateId()),
                isNull(),
                isA(MandrillMessage.class),
                eq(Boolean.FALSE)
        );
    }

    @Test
    public void testSendEmailStatusRejected() throws IOException, MandrillApiError {
        final SimpleEmailMessage message = SimpleEmailMessage.of(uuid(), uuid(), uuid(), uuid());
        when(mandrillMessageStatus.getStatus()).thenReturn("rejected");
        when(mandrillMessagesApi.send(
                isA(MandrillMessage.class),
                eq(Boolean.FALSE)
        )).thenReturn(new MandrillMessageStatus[]{mandrillMessageStatus});
        assertThatThrownBy(() -> mandrillApiCommunicator.sendEmail(message))
                .isInstanceOf(MandrillMessageRejectedException.class);
        verify(mandrillMessageStatus).getStatus();
        verify(mandrillMessagesApi).send(
                isA(MandrillMessage.class),
                eq(Boolean.FALSE)
        );
    }

    @Test
    public void testSendEmailStatusInvalid() throws IOException, MandrillApiError {
        final SimpleEmailMessage message = SimpleEmailMessage.of(uuid(), uuid(), uuid(), uuid());
        when(mandrillMessageStatus.getStatus()).thenReturn("invalid");
        when(mandrillMessagesApi.send(
                isA(MandrillMessage.class),
                eq(Boolean.FALSE)
        )).thenReturn(new MandrillMessageStatus[]{mandrillMessageStatus});
        assertThatThrownBy(() -> mandrillApiCommunicator.sendEmail(message))
                .isInstanceOf(MandrillMessageInvalidException.class);
        verify(mandrillMessageStatus).getStatus();
        verify(mandrillMessagesApi).send(
                isA(MandrillMessage.class),
                eq(Boolean.FALSE)
        );
    }

    @Test
    public void testSendEmaileStatusSuccess() throws IOException, MandrillApiError {
        final SimpleEmailMessage message = SimpleEmailMessage.of(uuid(), uuid(), uuid(), uuid());
        when(mandrillMessageStatus.getStatus()).thenReturn("invalid");
        when(mandrillMessagesApi.send(
                isA(MandrillMessage.class),
                eq(Boolean.FALSE)
        )).thenAnswer(invocation -> {
            final MandrillMessage mandrillMessage = invocation.getArgument(0);
            assertThat(mandrillMessage)
                    .hasFieldOrPropertyWithValue("fromEmail", message.from())
                    .hasFieldOrPropertyWithValue("subject", message.subject())
                    .hasFieldOrPropertyWithValue("html", message.body());
            assertThat(mandrillMessage.getTo().stream()
                    .map(MandrillMessage.Recipient::getEmail)
                    .anyMatch(message.to()::equals)
            ).isTrue();
            return new MandrillMessageStatus[]{mandrillMessageStatus};
        });
        when(mandrillMessageStatus.getStatus()).thenReturn(uuid());
        when(mandrillMessageStatus.getEmail()).thenReturn(message.to());
        when(mandrillMessageStatus.getId()).thenReturn(uuid());
        mandrillApiCommunicator.sendEmail(message);
        verify(mandrillMessageStatus).getStatus();
        verify(mandrillMessageStatus).getEmail();
        verify(mandrillMessageStatus).getId();
        verify(mandrillMessagesApi).send(
                isA(MandrillMessage.class),
                eq(Boolean.FALSE)
        );
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
