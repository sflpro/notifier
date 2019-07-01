package com.sflpro.notifier.services.template;

import com.sflpro.notifier.services.common.exception.ServicesRuntimeException;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import io.vavr.Lazy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.IOException;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.util.Locale;
import java.util.Map;

/**
 * User: Ruben Vardanyan
 * Company: SFL LLC
 * Date: 4/17/19
 * Time: 9:36 PM
 */
@Component
public class TemplatingServiceImpl implements TemplatingService {

    private static final Logger LOGGER = LoggerFactory.getLogger(TemplatingServiceImpl.class);

    private final Lazy<Configuration> localizedLookupEnabledConfiguration;

    private final Lazy<Configuration> localizedLookupDisabledConfiguration;

    @Autowired
    public TemplatingServiceImpl(TemplatingConfiguration configurationHolder) {
        LOGGER.debug("Initializing templating service");
        localizedLookupEnabledConfiguration = Lazy.of(() -> configurationHolder.getFreemarkerConfiguration(true));
        localizedLookupDisabledConfiguration = Lazy.of(() -> configurationHolder.getFreemarkerConfiguration(false));
    }

    @Override
    public String getContentForTemplate(@Nonnull final String templateName, @Nonnull final Map<String, ?> parameters) {
        return getContentForTemplateInternal(templateName, parameters, null);
    }

    @Override
    public String getContentForTemplate(@Nonnull final String templateName, @Nonnull final Map<String, ?> parameters, final Locale locale) {
        Assert.notNull(locale, "Parameters should not be null");
        return getContentForTemplateInternal(templateName, parameters, locale);
    }

    private String getContentForTemplateInternal(@Nonnull final String templateName, @Nonnull final Map<String, ?> parameters, @Nullable final Locale locale) {
        Assert.notNull(templateName, "Template name should not be null");
        Assert.notNull(parameters, "Parameters should not be null");
        try {
            final Template template = configuration(locale != null).getTemplate(templateName, locale, StandardCharsets.UTF_8.name(), true, true);
            Assert.notNull(template, "Template is missing.");
            final StringWriter resultWriter = new StringWriter();
            template.process(parameters, resultWriter);
            final String result = resultWriter.toString();
            resultWriter.flush();
            resultWriter.close();
            return result;
        } catch (IOException | TemplateException e) {
            LOGGER.error("Error occurred while processing template for template name - {}, with parameters -{}", templateName, parameters);
            throw new ServicesRuntimeException("Error occurred while processing template for template name - " + templateName + " with parameters - " + parameters, e);
        }
    }

    private Configuration configuration(final boolean localizedLookupEnabled) {
        return localizedLookupEnabled ? localizedLookupEnabledConfiguration.get() : localizedLookupDisabledConfiguration.get();
    }
}
