package com.sflpro.notifier.queue.consumer.notification.config;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.ByteArrayDeserializer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * Company: SFL LLC
 * Created on 08/02/2018
 *
 * @author Davit Harutyunyan
 */
@ConditionalOnProperty(name = "queue.engine", havingValue = "kafka")
@Configuration
@EnableKafka
public class KafkaConsumerConfiguration {

    @Value("${kafka.bootstrap.servers}")
    private String bootstrapServers;

    @Bean
    public Map<String, Object> setupConsumerConfig() {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, ByteArrayDeserializer.class);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "json");

        return props;
    }

    @Bean
    public ConsumerFactory<String, byte[]> setupConsumerFactory() {
        return new DefaultKafkaConsumerFactory<>(setupConsumerConfig(), new StringDeserializer(),
                new ByteArrayDeserializer());
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, byte[]> kafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, byte[]> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(setupConsumerFactory());

        return factory;
    }
}
