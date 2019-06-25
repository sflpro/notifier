package com.sflpro.notifier.services;

import com.sflpro.notifier.db.NotifierPersistenceConfiguration;
import com.sflpro.notifier.services.system.concurrency.ExecutorBuilder;
import org.springframework.context.annotation.*;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.concurrent.ExecutorService;

@Configuration
@ComponentScan(basePackages = "com.sflpro.notifier.services")
@PropertySource(value = "classpath:/com/sflpro/notifier/services.properties", ignoreResourceNotFound = true)
@Import({NotifierPersistenceConfiguration.class})
public class NotifierServicesConfiguration {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    public ExecutorService executorService(ExecutorBuilder executorBuilder) {
        return executorBuilder.createExecutorService();
    }
}
