package com.sflpro.notifier.spi.email;

import org.springframework.util.Assert;

import java.util.Locale;
import java.util.Map;

public class TemplatedEmailMessageBuilder {

    private final String from;
    private final String to;
    private final String templateId;
    private String subject;
    final Map<String, ?> variables;
    private Locale locale;

    public TemplatedEmailMessageBuilder(final String from,
                                        final String to,
                                        final String templateId,
                                        final Map<String, ?> variables) {
        Assert.hasText(from, "Null or empty text was passed as an argument for parameter 'from'.");
        Assert.hasText(to, "Null or empty text was passed as an argument for parameter 'to'.");
        Assert.hasText(templateId, "Null or empty text was passed as an argument for parameter 'templateId'.");
        Assert.notNull(variables, "Null was passed as an argument for parameter 'variables'.");
        this.from = from;
        this.to = to;
        this.templateId = templateId;
        this.variables = variables;

    }

    public TemplatedEmailMessageBuilder withLocale(final Locale locale) {
        Assert.notNull(locale, "Null was passed as an argument for parameter 'locale'.");
        this.locale = locale;
        return this;
    }

    public TemplatedEmailMessageBuilder withSubject(final String subject) {
        Assert.notNull(subject, "Null was passed as an argument for parameter 'subject'.");
        this.subject = subject;
        return this;
    }

    public TemplatedEmailMessage build() {
        return new ImmutableTemplatedEmailMessage(
                from,
                to,
                templateId,
                subject,
                variables,
                locale
        );
    }


}
