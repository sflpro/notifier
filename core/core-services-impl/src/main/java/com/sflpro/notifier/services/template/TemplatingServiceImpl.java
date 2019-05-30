package com.sflpro.notifier.services.template;

import com.sflpro.notifier.services.common.exception.ServicesRuntimeException;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.io.StringWriter;
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

    private final Configuration configuration;

    @Autowired
    public TemplatingServiceImpl(TemplatingConfiguration configurationHolder) {
        LOGGER.debug("Initializing templating service");
        this.configuration = configurationHolder.getFreemarkerConfiguration();
    }

    @Override
    public String getContentForTemplate(@Nonnull final String templateName, @Nonnull final Map<String, String> parameters) {
        Assert.notNull(templateName, "Template name should not be null");
        Assert.notNull(parameters, "Parameters should not be null");
        try {
            final Template template = configuration.getTemplate(templateName);
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
}
