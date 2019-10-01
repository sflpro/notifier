package com.sflpro.notifier.services.notification.impl.sms;

import com.sflpro.notifier.services.template.TemplatingService;
import com.sflpro.notifier.spi.sms.SmsTemplateContentResolver;
import org.springframework.util.Assert;

import java.util.Locale;
import java.util.Map;
import java.util.function.BiFunction;

/**
 * Created by Hayk Mkrtchyan.
 * Date: 6/19/19
 * Time: 6:06 PM
 */

class LocalSmsTemplateContentResolver implements SmsTemplateContentResolver {

    private final TemplatingService templatingService;

    LocalSmsTemplateContentResolver(final TemplatingService templatingService) {
        this.templatingService = templatingService;
    }

    @Override
    public String resolve(final String templateId, final Map<String, ?> variables) {
        return resolve(templateId, variables,
                templatingService::getContentForTemplate);
    }

    @Override
    public String resolve(final String templateId, final Map<String, ?> variables, final Locale locale) {
        Assert.notNull(locale);
        return resolve(templateId, variables,
                (templateName, vars) -> templatingService.getContentForTemplate(templateId, vars, locale));
    }

    private String resolve(final String templateId, final Map<String, ?> variables,
                           final BiFunction<String, Map<String, ?>, String> templateProvider) {
        Assert.hasText(templateId, "Null or empty text was passed as an argument for parameter 'templateId'.");
        Assert.notNull(variables, "Null was passed as an argument for parameter 'variables'.");
        return templateProvider.apply(templateId, variables);
    }
}
