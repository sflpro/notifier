package com.sflpro.notifier.email;

import org.springframework.util.Assert;

import java.util.Map;

/**
 * Created by Hayk Mkrtchyan.
 * Date: 6/19/19
 * Time: 10:40 AM
 */
public interface TemplatedEmailMessage extends EmailMessage {

    String templateId();

    Map<String, ? extends Object> variables();

    static TemplatedEmailMessage of(final String from, final String to, final String templateId, final Map<String,? extends Object> variables) {
        Assert.hasText(from, "Null or empty text was passed as an argument for parameter 'from'.");
        Assert.hasText(to, "Null or empty text was passed as an argument for parameter 'to'.");
        Assert.hasText(templateId, "Null or empty text was passed as an argument for parameter 'templateId'.");
        return new ImmutableTemplatedEmailMessage(from, to, templateId, variables);
    }
}
