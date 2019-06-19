package com.sflpro.notifier.services.template;

import javax.annotation.Nonnull;
import java.util.Map;

/**
 * User: Ruben Vardanyan
 * Company: SFL LLC
 * Date: 4/17/19
 * Time: 9:32 PM
 */
public interface TemplatingService {

    /**
     * Generates content according to the given template name with the given parameters
     * @param templateName templateName
     * @param parameters parameters
     * @return Generated content
     */
    String getContentForTemplate(@Nonnull final String templateName, @Nonnull final Map<String, ? extends Object> parameters);

}
