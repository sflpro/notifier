package com.sflpro.notifier.services.template.impl;

import com.sflpro.notifier.services.template.TemplatingConfiguration;
import freemarker.cache.URLTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.TemplateExceptionHandler;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URL;

import static java.lang.String.format;

@Component
public class TemplatingConfigurationImpl implements TemplatingConfiguration {

    @Value("${notifier.templates.path}")
    private String templatesPath = "/";
    @Value("${notifier.templates.extension:.ftl}")
    private String templateExtension = ".ftl";

    private ResourceLoader resourceLoader = new DefaultResourceLoader();

    public Configuration getFreemarkerConfiguration() {
        final freemarker.template.Configuration cfg = new freemarker.template.Configuration(freemarker.template.Configuration.VERSION_2_3_28);
        cfg.setTemplateLoader(new URLTemplateLoader() {
            @Override
            protected URL getURL(final String resourceName) {
                try {
                    if (!resourceName.endsWith(templateExtension)) {
                        return resourceLoader.getResource(templatesPath + "/" + resourceName +templateExtension).getURL();
                    }else{
                        return resourceLoader.getResource(templatesPath + "/" + resourceName).getURL();
                    }
                } catch (final IOException ex) {
                    throw new TemplateLookupFailedException(resourceName, templatesPath, ex);
                }
            }
        });
        cfg.setLocalizedLookup(false);
        cfg.setDefaultEncoding("UTF-8");
        cfg.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
        cfg.setLogTemplateExceptions(false);
        cfg.setWrapUncheckedExceptions(true);
        return cfg;
    }


    private static final class TemplateLookupFailedException extends RuntimeException {

        private static String message(final String resourceName, final String templatesPath) {
            return format("Resource '%s' in the '%s' is missing.", resourceName, templatesPath);
        }

        TemplateLookupFailedException(final String resourceName, final String templatesPath, final Exception cause) {
            super(message(resourceName, templatesPath), cause);
        }
    }
}
