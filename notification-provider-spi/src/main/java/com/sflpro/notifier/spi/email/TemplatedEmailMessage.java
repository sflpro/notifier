package com.sflpro.notifier.spi.email;

import org.springframework.util.Assert;

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

    default Optional<String> subject(){
        return Optional.empty();
    }

    static TemplatedEmailMessage of(final String from, final String to, final String templateId, final Map<String, ?> variables) {
        Assert.hasText(from, "Null or empty text was passed as an argument for parameter 'from'.");
        Assert.hasText(to, "Null or empty text was passed as an argument for parameter 'to'.");
        Assert.hasText(templateId, "Null or empty text was passed as an argument for parameter 'templateId'.");
        return new ImmutableTemplatedEmailMessage(from, to, templateId, variables);
    }

    static TemplatedEmailMessage of(final String from, final String to, final String templateId, final String subject, final Map<String, ?> variables) {
        Assert.hasText(from, "Null or empty text was passed as an argument for parameter 'from'.");
        Assert.hasText(to, "Null or empty text was passed as an argument for parameter 'to'.");
        Assert.hasText(templateId, "Null or empty text was passed as an argument for parameter 'templateId'.");
        Assert.hasText(subject, "Null or empty text was passed as an argument for parameter 'subject'.");
        return new ImmutableTemplatedEmailMessage(from, to, templateId, subject, variables);
    }
}
