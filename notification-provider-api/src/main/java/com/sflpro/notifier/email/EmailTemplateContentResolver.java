package com.sflpro.notifier.email;

import java.util.Map;

/**
 * Created by Hayk Mkrtchyan.
 * Date: 6/19/19
 * Time: 5:17 PM
 */
public interface EmailTemplateContentResolver {

    EmailTemplateContent resolve(final String templateId, final Map<String, ? extends Object> variables);
}
