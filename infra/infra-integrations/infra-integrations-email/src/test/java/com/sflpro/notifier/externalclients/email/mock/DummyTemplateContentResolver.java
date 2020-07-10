package com.sflpro.notifier.externalclients.email.mock;

import com.sflpro.notifier.spi.template.TemplateContent;
import com.sflpro.notifier.spi.template.TemplateContentResolver;

import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

import static java.lang.String.format;

/**
 * Created by Hayk Mkrtchyan.
 * Date: 7/12/19
 * Time: 6:48 PM
 */
class DummyTemplateContentResolver implements TemplateContentResolver {

    @Override
    public TemplateContent resolve(final String templateId, final Map<String, ?> variables) {
        return TemplateContent.of("Hey!", format("%s - %s", templateId, variables.entrySet().stream().map(entry -> entry.getKey() + " is " + entry.getValue()).collect(Collectors.joining("\n"))));
    }

    @Override
    public TemplateContent resolve(String templateId, Map<String, ?> variables, Locale locale) {
        return TemplateContent.of("Hey!", format("%s - %s - %s", templateId, locale, variables.entrySet().stream().map(entry -> entry.getKey() + " is " + entry.getValue()).collect(Collectors.joining("\n"))));
    }
}
