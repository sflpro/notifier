package com.sflpro.notifier.services.notification.template;

import com.sflpro.notifier.services.template.TemplatingService;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import javax.annotation.Nonnull;
import java.util.Map;

/**
 * User: Ruben Vardanyan
 * Company: SFL LLC
 * Date: 4/18/19
 * Time: 5:48 PM
 */
@Component
public class DummyTemplatingServiceImpl implements TemplatingService {

    @Override
    public String getContentForTemplate(@Nonnull final String templateName, @Nonnull final Map<String, ?> parameters) {
        Assert.notNull(templateName, "templateName should not be null");
        Assert.notNull(parameters, "parameters should not be null");
        return "Generated content";
    }
}
