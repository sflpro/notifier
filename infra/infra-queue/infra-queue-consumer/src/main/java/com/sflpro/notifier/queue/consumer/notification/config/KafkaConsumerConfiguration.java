package com.sflpro.notifier.queue.consumer.notification.config;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.ByteArrayDeserializer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;

import javax.annotation.PostConstruct;
import java.net.InetAddress;
import java.net.UnknownHostException;
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

    private static final String CONSUMER_GROUP_FALLBACK = "notifier";

    private static final Logger logger = LoggerFactory.getLogger(KafkaConsumerConfiguration.class);

    @Value("${kafka.bootstrap.servers}")
    private String bootstrapServers;

    @Value("${kafka.heartbeat.interval.ms}")
    private Integer heartbeatInterval;

    @Value("${kafka.concurrency.factor}")
    private int kafkaConcurrencyFactor;

    @PostConstruct
    public void validateConfig() {
        if(kafkaConcurrencyFactor <1 ) {
            throw new IllegalStateException("Kafka concurrency factor should be a positive integer");
        }
    }

    @Bean
    public Map<String, Object> setupConsumerConfig() {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ConsumerConfig.HEARTBEAT_INTERVAL_MS_CONFIG, heartbeatInterval);
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, ByteArrayDeserializer.class);

        String consumerGroupId;
        try {
            consumerGroupId = InetAddress.getLocalHost().getHostName();
        } catch (UnknownHostException e) {
            logger.warn("Unable to determine the hostname of the machine, using {} as Kafka consumer group", CONSUMER_GROUP_FALLBACK, e);
            consumerGroupId = CONSUMER_GROUP_FALLBACK;
        }

        props.put(ConsumerConfig.GROUP_ID_CONFIG, consumerGroupId);

        return props;
    }

    @Bean
    public ConsumerFactory<String, byte[]> setupConsumerFactory() {
        ConsumerFactory<String, byte[]> consumerFactory =  new DefaultKafkaConsumerFactory<>(setupConsumerConfig(), new StringDeserializer(),
                new ByteArrayDeserializer());

        logger.info("Kafka consumer factory initialized");
        return consumerFactory;
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, byte[]> kafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, byte[]> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(setupConsumerFactory());

        logger.info("Kafka concurrent listener container factory initialized");
        return factory;
    }
}
