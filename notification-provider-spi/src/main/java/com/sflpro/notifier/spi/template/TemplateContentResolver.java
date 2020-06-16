package com.sflpro.notifier.spi.template;

import java.util.Locale;
import java.util.Map;

/**
 * Created by Hayk Mkrtchyan.
 * Date: 6/19/19
 * Time: 5:17 PM
 */
public interface TemplateContentResolver {

    TemplateContent resolve(final String templateId, final Map<String, ?> variables);

    TemplateContent resolve(final String templateId, final Map<String, ?> variables, final Locale locale);
}
