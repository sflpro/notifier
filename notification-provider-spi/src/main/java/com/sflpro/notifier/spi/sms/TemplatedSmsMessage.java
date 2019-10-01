package com.sflpro.notifier.spi.sms;

import org.springframework.util.Assert;

import java.util.Locale;
import java.util.Map;
import java.util.Optional;

/**
 * Created by Hayk Mkrtchyan.
 * Date: 6/20/19
 * Time: 3:08 PM
 */
public interface TemplatedSmsMessage extends SmsMessage {

    String templateId();

    @SuppressWarnings("squid:S1452")
    Map<String, ?> variables();

    Optional<Locale> locale();

    default String resolveBodyWith(final SmsTemplateContentResolver resolver) {
        return locale()
                .map(locale -> resolver.resolve(templateId(), variables(), locale))
                .orElseGet(() -> resolver.resolve(templateId(), variables()));
    }

    static TemplatedSmsMessage of(final long internalId,
                                  final String senderNumber,
                                  final String recipientNumber,
                                  final String templateId,
                                  final Map<String, ?> variables) {
        Assert.hasText(senderNumber, "Null or empty text was passed as an argument for parameter 'sender'.");
        Assert.hasText(templateId, "Null or empty text was passed as an argument for parameter 'templateId'.");
        return new ImmutableTemplatedSmsMessage(internalId, senderNumber, recipientNumber, templateId, variables, null);
    }

    static TemplatedSmsMessage of(final long internalId,
                                  final String senderNumber,
                                  final String recipientNumber,
                                  final String templateId,
                                  final Map<String, ?> variables,
                                  final Locale locale) {
        Assert.hasText(senderNumber, "Null or empty text was passed as an argument for parameter 'sender'.");
        Assert.hasText(templateId, "Null or empty text was passed as an argument for parameter 'templateId'.");
        Assert.notNull(locale, "Null was passed as an argument for parameter 'locale'.");
        return new ImmutableTemplatedSmsMessage(internalId, senderNumber, recipientNumber, templateId, variables, locale);
    }
}
