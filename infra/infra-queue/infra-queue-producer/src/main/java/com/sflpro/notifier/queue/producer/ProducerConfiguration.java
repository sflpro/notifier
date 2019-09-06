package com.sflpro.notifier.queue.producer;

import com.sflpro.notifier.queue.QueueConfigurationDefaults;
import com.sflpro.notifier.queue.producer.notification.NotificationQueueProducerConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@ComponentScan("com.sflpro.notifier.queue.producer")
@Import({
        QueueConfigurationDefaults.class,
        RabbitProducerConfiguration.class,
        KafkaProducerConfiguration.class,
        NotificationQueueProducerConfiguration.class
})
public class ProducerConfiguration {
}
