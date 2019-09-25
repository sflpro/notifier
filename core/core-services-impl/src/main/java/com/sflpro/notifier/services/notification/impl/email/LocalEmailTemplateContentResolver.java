package com.sflpro.notifier.services.notification.impl.email;


import com.sflpro.notifier.services.template.TemplatingService;
import com.sflpro.notifier.spi.email.EmailTemplateContent;
import com.sflpro.notifier.spi.email.EmailTemplateContentResolver;
import org.springframework.util.Assert;

import java.util.Locale;
import java.util.Map;
import java.util.function.BiFunction;

/**
 * Created by Hayk Mkrtchyan.
 * Date: 6/19/19
 * Time: 6:06 PM
 */

class LocalEmailTemplateContentResolver implements EmailTemplateContentResolver {

    private static final String SUBJECT_SUFFIX = "_subject";
    private static final String BODY_SUFFIX = "_content";

    private final TemplatingService templatingService;

    LocalEmailTemplateContentResolver(final TemplatingService templatingService) {
        this.templatingService = templatingService;
    }

    @Override
    public EmailTemplateContent resolve(final String templateId, final Map<String, ?> variables) {
        return resolve(
                templateId,
                variables,
                (templateName, vars) -> templatingService.getContentForTemplate(templateName, variables)
        );
    }

    @Override
    public EmailTemplateContent resolve(final String templateId, final Map<String, ?> variables, final Locale locale) {
        Assert.notNull(locale, "Null was passed as an argument for parameter 'locale'.");
        return resolve(
                templateId,
                variables,
                (templateName, vars) -> templatingService.getContentForTemplate(templateName, variables, locale)
        );
    }

    private EmailTemplateContent resolve(final String templateId, final Map<String, ?> variables,
                                         final BiFunction<String, Map<String, ?>, String> templateProvider) {
        Assert.notNull(templateId, "Null or empty text was passed as an argument for parameter 'templateId'.");
        Assert.notNull(variables, "Null was passed as an argument for parameter 'variables'.");
        return EmailTemplateContent.of(
                templateProvider.apply(templateId + SUBJECT_SUFFIX, variables),
                templateProvider.apply(templateId + BODY_SUFFIX, variables)
        );
    }
}
