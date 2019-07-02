package com.sflpro.notifier.services.template.impl;

import com.sflpro.notifier.services.template.TemplatingConfiguration;
import freemarker.cache.URLTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.TemplateExceptionHandler;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URL;

import static java.lang.String.format;

@Component
public class TemplatingConfigurationImpl implements TemplatingConfiguration {


    private String templatesPath = "/";
    private String templateExtension = ".ftl";

    private final ResourceLoader resourceLoader = new DefaultResourceLoader();

    TemplatingConfigurationImpl(
            @Value("${notifier.templates.path}") final String templatesPath,
            @Value("${notifier.templates.extension:.ftl}") final String templateExtension) {
        this.templatesPath = templatesPath;
        this.templateExtension = templateExtension;
    }


    public Configuration getFreemarkerConfiguration(final boolean localizedLookupEnabled) {
        final freemarker.template.Configuration cfg = new freemarker.template.Configuration(freemarker.template.Configuration.VERSION_2_3_28);
        cfg.setTemplateLoader(new URLTemplateLoader() {
            @Override
            protected URL getURL(final String resourceName) {
                try {
                    final Resource resource = getResource(resourceName);
                    return resource.exists() ? resource.getURL() : null;
                } catch (final IOException ex) {
                    throw new TemplateLookupFailedException(resourceName, templatesPath, ex);
                }
            }
        });
        cfg.setLocalizedLookup(localizedLookupEnabled);
        cfg.setDefaultEncoding("UTF-8");
        cfg.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
        cfg.setLogTemplateExceptions(false);
        cfg.setWrapUncheckedExceptions(true);
        return cfg;
    }

    private Resource getResource(final String resourceName) throws IOException {
        if (!resourceName.endsWith(templateExtension)) {
            return resourceLoader.getResource(templatesPath + "/" + resourceName + templateExtension);
        } else {
            return resourceLoader.getResource(templatesPath + "/" + resourceName);
        }
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
