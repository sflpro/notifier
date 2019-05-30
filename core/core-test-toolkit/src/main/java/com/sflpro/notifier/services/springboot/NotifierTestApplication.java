package com.sflpro.notifier.services.springboot;

import com.sflpro.notifier.services.notification.email.SmtpTransportService;
import com.sflpro.notifier.services.notification.impl.email.SmtpTransportInMemoryServiceImpl;
import com.sflpro.notifier.services.notification.template.DummyTemplatingServiceImpl;
import com.sflpro.notifier.services.template.TemplatingService;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;

@SpringBootApplication
@ComponentScan("com.sflpro.notifier")
@PropertySource(value = "classpath:/com/sflpro/notifier/test/services-integration-test.properties", ignoreResourceNotFound = true)
public class NotifierTestApplication {

    @Bean
    public SmtpTransportService smtpTransportService() {
        return new SmtpTransportInMemoryServiceImpl();
    }

    @Bean
    public TemplatingService templatingService() {
        return new DummyTemplatingServiceImpl();
    }
}
