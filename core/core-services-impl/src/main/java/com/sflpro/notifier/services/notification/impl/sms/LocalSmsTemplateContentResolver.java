package com.sflpro.notifier.services.notification.impl.sms;

import com.sflpro.notifier.services.template.TemplatingService;
import com.sflpro.notifier.spi.sms.SmsTemplateContentResolver;

import java.util.Map;

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
        return templatingService.getContentForTemplate(templateId, variables);
    }
}
