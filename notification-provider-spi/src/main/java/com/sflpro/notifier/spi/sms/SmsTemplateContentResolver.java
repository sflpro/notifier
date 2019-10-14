package com.sflpro.notifier.spi.sms;

import java.util.Locale;
import java.util.Map;

/**
 * Created by Hayk Mkrtchyan.
 * Date: 6/19/19
 * Time: 5:17 PM
 */
public interface SmsTemplateContentResolver {

    String resolve(final String templateId, final Map<String, ?> variables);

    String resolve(final String templateId,final Map<String,?> variables,final Locale locale);
}
