package com.sflpro.notifier.services.template.impl;

import com.sflpro.notifier.services.template.TemplatingConfiguration;
import freemarker.cache.ClassTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.TemplateExceptionHandler;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class TemplatingConfigurationImpl implements TemplatingConfiguration {
    @Value("${notifier.templates.path}")
    private String templatesPath;

    public Configuration getFreemarkerConfiguration() {
        freemarker.template.Configuration cfg = new freemarker.template.Configuration(freemarker.template.Configuration.VERSION_2_3_28);
        cfg.setTemplateLoader(new ClassTemplateLoader(getClass(), templatesPath));
        cfg.setDefaultEncoding("UTF-8");
        cfg.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
        cfg.setLogTemplateExceptions(false);
        cfg.setWrapUncheckedExceptions(true);
        return cfg;
    }
}
