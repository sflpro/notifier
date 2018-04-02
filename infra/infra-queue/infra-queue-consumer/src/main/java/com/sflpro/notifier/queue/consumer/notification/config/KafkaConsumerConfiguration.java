package com.sflpro.notifier.queue.consumer.notification.config;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.ByteArrayDeserializer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.classify.BinaryExceptionClassifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.AbstractMessageListenerContainer;
import org.springframework.retry.RetryContext;
import org.springframework.retry.RetryPolicy;
import org.springframework.retry.backoff.BackOffPolicy;
import org.springframework.retry.backoff.ExponentialBackOffPolicy;
import org.springframework.retry.policy.SimpleRetryPolicy;
import org.springframework.retry.support.RetryTemplate;

import javax.annotation.PostConstruct;
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

    private static final Logger logger = LoggerFactory.getLogger(KafkaConsumerConfiguration.class);

    @Value("${kafka.bootstrap.servers}")
    private String bootstrapServers;

    @Value("${kafka.heartbeat.interval.ms}")
    private Integer heartbeatInterval;

    @Value("${kafka.concurrency.factor}")
    private int kafkaConcurrencyFactor;

    @PostConstruct
    public void validateConfig() {
        if (kafkaConcurrencyFactor < 1) {
            throw new IllegalStateException("Kafka concurrency factor should be a positive integer");
        }
    }

    @Bean
    public Map<String, Object> setupConsumerConfig() {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, false);
        props.put(ConsumerConfig.HEARTBEAT_INTERVAL_MS_CONFIG, heartbeatInterval);
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, ByteArrayDeserializer.class);
        props.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, 1);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "notifier-worker");

        return props;
    }

    @Bean
    public ConsumerFactory<String, byte[]> setupConsumerFactory() {
        ConsumerFactory<String, byte[]> consumerFactory = new DefaultKafkaConsumerFactory<>(setupConsumerConfig(), new StringDeserializer(),
                new ByteArrayDeserializer());
        logger.info("Kafka consumer factory initialized");
        return consumerFactory;
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, byte[]> kafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, byte[]> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(setupConsumerFactory());
        factory.setRetryTemplate(retryTemplate());
        factory.setConcurrency(kafkaConcurrencyFactor);
        factory.getContainerProperties().setAckMode(AbstractMessageListenerContainer.AckMode.MANUAL);
        factory.getContainerProperties().setAckOnError(false);
        /*
        try {
            factory.getContainerProperties().setClientId(InetAddress.getLocalHost().getHostName());
        } catch (UnknownHostException e) {
            logger.warn("Unable to determine the hostname of the machine", e);
        }
        */

        logger.info("Kafka concurrent listener container factory initialized");
        return factory;
    }

    private RetryPolicy retryPolicy() {
        return new SimpleRetryPolicy() {
            private BinaryExceptionClassifier retryableClassifier = new BinaryExceptionClassifier(true);

            @Override
            public boolean canRetry(RetryContext context) {
                Throwable t = context.getLastThrowable();
                return (t == null || retryableClassifier.classify(t));
            }
        };
    }

    private BackOffPolicy backOffPolicy() {
        ExponentialBackOffPolicy policy = new ExponentialBackOffPolicy();
        policy.setInitialInterval(5000);
        policy.setMultiplier(1);
        return policy;
    }

    private RetryTemplate retryTemplate() {
        RetryTemplate template = new RetryTemplate();
        template.setRetryPolicy(retryPolicy());
        template.setBackOffPolicy(backOffPolicy());
        return template;
    }
}
