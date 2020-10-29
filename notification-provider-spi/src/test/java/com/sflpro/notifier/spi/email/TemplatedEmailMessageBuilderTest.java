package com.sflpro.notifier.spi.email;

import org.junit.Test;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class TemplatedEmailMessageBuilderTest {


    @Test
    public void createBuilderWithIllegalArguments() {
        assertThatThrownBy(() -> new TemplatedEmailMessageBuilder(
                                   null,
                                   uuid(),
                                   Collections.emptySet(),
                                   uuid(),
                                   Collections.emptyMap(),
                                   Collections.emptySet()
                           )
        ).isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> new TemplatedEmailMessageBuilder(
                                   uuid(),
                                   null,
                                   Collections.emptySet(),
                                   uuid(),
                                   Collections.emptyMap(),
                                   Collections.emptySet()
                           )
        ).isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> new TemplatedEmailMessageBuilder(
                                   uuid(),
                                   uuid(),
                                   Collections.emptySet(),
                                   null,
                                   Collections.emptyMap(),
                                   Collections.emptySet()
                           )
        ).isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> new TemplatedEmailMessageBuilder(
                                   uuid(),
                                   uuid(),
                                   Collections.emptySet(),
                                   uuid(),
                                   null,
                                   Collections.emptySet()
                           )
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    public void withLocaleWithIllegalArgument() {
        assertThatThrownBy(() -> new TemplatedEmailMessageBuilder(
                                   uuid(),
                                   uuid(),
                                   Collections.emptySet(),
                                   uuid(),
                                   Collections.emptyMap(),
                                   Collections.emptySet()
                           ).withLocale(null)
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    public void withSubjectWithIllegalArgument() {
        assertThatThrownBy(() -> new TemplatedEmailMessageBuilder(
                                   uuid(),
                                   uuid(),
                                   Collections.emptySet(),
                                   uuid(),
                                   Collections.emptyMap(),
                                   Collections.emptySet()
                           ).withSubject(null)
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    public void build() {
        final String from = uuid();
        final String to = uuid();
        final Set<String> replyTo = Collections.singleton(uuid());
        final String subject = uuid();
        final String templateId = uuid();
        final Map<String, String> variables = Collections.singletonMap(uuid(), uuid());
        final Locale locale = Locale.forLanguageTag("hy");
        assertThat(new TemplatedEmailMessageBuilder(
                from,
                to,
                replyTo,
                templateId,
                variables,
                Collections.emptySet()
        )
                .withSubject(subject)
                .withLocale(locale).build())
                .hasFieldOrPropertyWithValue("from", from)
                .hasFieldOrPropertyWithValue("to", to)
                .hasFieldOrPropertyWithValue("subject", subject)
                .hasFieldOrPropertyWithValue("templateId", templateId)
                .hasFieldOrPropertyWithValue("locale", locale)
                .hasFieldOrPropertyWithValue("variables", variables);
    }

    private static String uuid() {
        return UUID.randomUUID().toString();
    }
}
