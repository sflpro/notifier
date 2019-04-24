package com.sflpro.notifier.services.template;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

/**
 * User: Ruben Vardanyan
 * Company: SFL LLC
 * Date: 4/18/19
 * Time: 5:41 PM
 */

@Configuration
public class FreemarkerConfiguration {

    @Value("${freemarker.templatesPath}")
    private String templatesPath;

    @Bean
    public FreeMarkerConfigurer freeMarkerConfigurer(){
        FreeMarkerConfigurer freeMarkerConfigurer = new FreeMarkerConfigurer();
        freeMarkerConfigurer.setTemplateLoaderPath(templatesPath); //defines the classpath location of the freemarker templates
        freeMarkerConfigurer.setDefaultEncoding("UTF-8"); // Default encoding of the template files
        return freeMarkerConfigurer;
    }
}
