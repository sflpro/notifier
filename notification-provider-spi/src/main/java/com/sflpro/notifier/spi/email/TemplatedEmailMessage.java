package com.sflpro.notifier.spi.email;

import org.springframework.util.Assert;

import java.util.Locale;
import java.util.Map;
import java.util.Optional;

/**
 * Created by Hayk Mkrtchyan.
 * Date: 6/19/19
 * Time: 10:40 AM
 */
public interface TemplatedEmailMessage extends EmailMessage {

    String templateId();

    @SuppressWarnings("squid:S1452")
    Map<String, ?> variables();

    default Optional<String> subject() {
        return Optional.empty();
    }

    default TemplatedEmailMessage withSubject(final String subject) {
        Assert.notNull(subject, "Null was passed as an argument for parameter 'subject'.");
        return new ImmutableTemplatedEmailMessage(from(), to(), templateId(), subject, variables(), locale().orElse(null));
    }

    default TemplatedEmailMessage withLocale(final Locale locale) {
        Assert.notNull(locale, "Null was passed as an argument for parameter 'locale'.");
        return new ImmutableTemplatedEmailMessage(from(), to(), templateId(), subject().orElse(null), variables(), locale);
    }

    default Optional<Locale> locale() {
        return Optional.empty();
    }

    static TemplatedEmailMessage of(final String from,
                                    final String to,
                                    final String templateId,
                                    final Map<String, ?> variables) {
        Assert.hasText(from, "Null or empty text was passed as an argument for parameter 'from'.");
        Assert.hasText(to, "Null or empty text was passed as an argument for parameter 'to'.");
        Assert.hasText(templateId, "Null or empty text was passed as an argument for parameter 'templateId'.");
        Assert.notNull(variables, "Null was passed as an argument for parameter 'variables'.");
        return new ImmutableTemplatedEmailMessage(from, to, templateId, null, variables, null);
    }

}
