package com.sflpro.notifier.spi.email;

import org.junit.Test;

import java.util.Collections;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class TemplatedEmailMessageBuilderTest {


    @Test
    public void createBuilderWithIllegalArguments() {
        assertThatThrownBy(() -> new TemplatedEmailMessageBuilder(
                        null,
                        uuid(),
                        uuid(),
                        Collections.emptyMap(),
                        Collections.emptySet()
                )
        ).isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> new TemplatedEmailMessageBuilder(
                        uuid(),
                        null,
                        uuid(),
                        Collections.emptyMap(),
                        Collections.emptySet()
                )
        ).isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> new TemplatedEmailMessageBuilder(
                        uuid(),
                        uuid(),
                        null,
                        Collections.emptyMap(),
                        Collections.emptySet()
                )
        ).isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> new TemplatedEmailMessageBuilder(
                        uuid(),
                        uuid(),
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
        final String subject = uuid();
        final String templateId = uuid();
        final Map<String, String> variables = Collections.singletonMap(uuid(), uuid());
        final Locale locale = Locale.forLanguageTag("hy");
        assertThat(new TemplatedEmailMessageBuilder(
                from,
                to,
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
