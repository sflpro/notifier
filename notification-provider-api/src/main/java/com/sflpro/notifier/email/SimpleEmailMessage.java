package com.sflpro.notifier.email;

import org.springframework.util.Assert;

/**
 * Created by Hayk Mkrtchyan.
 * Date: 6/19/19
 * Time: 10:31 AM
 */
public interface SimpleEmailMessage extends EmailMessage{

    String body();

    String subject();

    static SimpleEmailMessage of(final String from, final String to, final String subject, final String body) {
        Assert.hasText(from, "Null or empty text was passed as an argument for parameter 'from'.");
        Assert.hasText(to, "Null or empty text was passed as an argument for parameter 'to'.");
        Assert.hasText(body, "Null or empty text was passed as an argument for parameter 'body'.");
        Assert.hasText(subject, "Null or empty text was passed as an argument for parameter 'subject'.");
        return new ImmutableSimpleEmailMessage(from, to, subject, body);
    }
}
