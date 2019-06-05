package com.sflpro.notifier.queue.producer;

import com.sflpro.notifier.queue.QueueConfigurationDefaults;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@ComponentScan("com.sflpro.notifier.queue.producer")
@Import({QueueConfigurationDefaults.class, RabbitProducerConfiguration.class, KafkaProducerConfiguration.class})
public class ProducerConfiguration {

}
