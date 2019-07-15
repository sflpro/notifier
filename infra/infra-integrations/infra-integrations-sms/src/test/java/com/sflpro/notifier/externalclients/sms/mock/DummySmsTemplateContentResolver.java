package com.sflpro.notifier.externalclients.sms.mock;

import com.sflpro.notifier.spi.sms.SmsTemplateContentResolver;

import java.util.Map;
import java.util.stream.Collectors;

import static java.lang.String.format;

/**
 * Created by Hayk Mkrtchyan.
 * Date: 7/12/19
 * Time: 6:39 PM
 */
class DummySmsTemplateContentResolver implements SmsTemplateContentResolver {

    @Override
    public String resolve(final String templateId, final Map<String, ?> variables) {
        return format("%s - %s", templateId, variables.entrySet().stream()
                .map(entry -> entry.getKey() + " is " + entry.getValue())
                .collect(Collectors.joining("\n")));
    }
}
