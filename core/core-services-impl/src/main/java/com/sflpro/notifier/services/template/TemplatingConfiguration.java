package com.sflpro.notifier.services.template;

import freemarker.template.Configuration;

public interface TemplatingConfiguration {
    Configuration getFreemarkerConfiguration(final boolean localizedLookupEnabled);

    default Configuration getFreemarkerConfiguration(){
        return getFreemarkerConfiguration(true);
    }
}
