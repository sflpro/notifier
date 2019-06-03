package com.sflpro.notifier.queue.consumer;

import com.sflpro.notifier.queue.QueueConfigurationDefaults;
import com.sflpro.notifier.queue.amqp.RabbitConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({QueueConfigurationDefaults.class, KafkaConsumerConfiguration.class, RabbitConfiguration.class})
@ComponentScan("com.sflpro.notifier.queue.consumer")
public class ConsumerConfiguration {

}
