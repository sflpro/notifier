package com.sflpro.notifier.queue;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@ComponentScan(basePackages = "com.sflpro.notifier.queue.amqp")
@PropertySource(value = "classpath:/com/sflpro/notifier/queue.properties", ignoreResourceNotFound = true)
public class QueueConfigurationDefaults {

    @Bean
    public ObjectMapper amqpObjectMapper() {
        return new ObjectMapper();
    }
}
