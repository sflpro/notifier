package com.sflpro.notifier.services;

import com.sflpro.notifier.db.NotifierPersistenceConfiguration;
import com.sflpro.notifier.services.system.concurrency.ExecutorBuilder;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.ExecutorService;

@Configuration
@ComponentScan(basePackages = "com.sflpro.notifier.services")
@PropertySource(value = "classpath:/com/sflpro/notifier/services.properties", ignoreResourceNotFound = true)
@Import({NotifierPersistenceConfiguration.class})
@ImportResource(locations = {
        "applicationContext-externalclients-email.xml",
        "applicationContext-externalclients-push.xml",
        "applicationContext-externalclients-sms.xml"
})
public class NotifierServicesConfiguration {

    @Bean
    public ExecutorService executorService(ExecutorBuilder executorBuilder) {
        return executorBuilder.createExecutorService();
    }

    /**
     * TODO replace this with WebClient, see documentation of {@link RestTemplate}
     */
    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder.build();
    }
}
