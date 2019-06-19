package com.sflpro.notifier.email;

import org.springframework.util.Assert;

/**
 * Created by Hayk Mkrtchyan.
 * Date: 6/19/19
 * Time: 5:28 PM
 */
public interface EmailTemplateContent {

    String subject();

    String body();

    static EmailTemplateContent of(final String subject, final String body) {
        Assert.hasText(subject, "Null or empty text was passed as an argument for parameter 'subject'.");
        Assert.hasText(body, "Null or empty text was passed as an argument for parameter 'body'.");
        return new ImmutableEmailTemplateContent(subject, body);
    }
}
