package com.sflpro.notifier.queue.consumer;

import com.sflpro.notifier.queue.QueueConfigurationDefaults;
import org.apache.commons.lang3.StringUtils;
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
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Lazy;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.ContainerProperties;
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
@Configuration
@ConditionalOnProperty(name = "notifier.queue.engine", havingValue = "kafka")
@Import({QueueConfigurationDefaults.class})
@EnableKafka
public class KafkaConsumerConfiguration {

    private static final Logger logger = LoggerFactory.getLogger(KafkaConsumerConfiguration.class);

    @Value("${kafka.bootstrap.servers}")
    private String bootstrapServers;

    @Value("${kafka.heartbeat.interval.ms}")
    private Integer heartbeatInterval;

    @Value("${kafka.concurrency.factor}")
    private int kafkaConcurrencyFactor;

    @Value(value = "${kafka.request.timeout.ms}")
    private String requestTimeoutMs;

    @Value(value = "${kafka.retry.backoff.ms}")
    private String retryBackoffMs;

    @Value(value = "${kafka.ssl.endpoint.identification.algorithm}")
    private String sslEndpointIdentificationAlgorithm;

    @Value(value = "${kafka.sasl.mechanism}")
    private String saslMechanism;

    @Value(value = "${kafka.sasl.jaas.config}")
    private String saslJaasConfig;

    @Value(value = "${kafka.security.protocol}")
    private String securityProtocol;

    @PostConstruct
    public void validateConfig() {
        if (kafkaConcurrencyFactor < 1) {
            throw new IllegalStateException("Kafka concurrency factor should be a positive integer");
        }
        logger.info("Kafka consumer configuration successfully done.");
    }

    @Bean
    public Map<String, Object> setupConsumerConfig() {
        logger.info("Creating setupConsumerConfig.");
        final Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, false);
        props.put(ConsumerConfig.HEARTBEAT_INTERVAL_MS_CONFIG, heartbeatInterval);
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, ByteArrayDeserializer.class);
        props.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, 1);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "notifier-worker");
        props.put(ConsumerConfig.REQUEST_TIMEOUT_MS_CONFIG, StringUtils.defaultIfBlank(requestTimeoutMs, "20000"));
        props.put(ConsumerConfig.RETRY_BACKOFF_MS_CONFIG, StringUtils.defaultIfBlank(retryBackoffMs, "500"));
        // Security
        if (StringUtils.isNoneBlank(sslEndpointIdentificationAlgorithm)) {
            props.put("ssl.endpoint.identification.algorithm", sslEndpointIdentificationAlgorithm);
        }
        if (StringUtils.isNoneBlank(saslMechanism)) {
            props.put("sasl.mechanism", saslMechanism);
        }
        if (StringUtils.isNoneBlank(saslJaasConfig)) {
            props.put("sasl.jaas.config", saslJaasConfig);
        }
        if (StringUtils.isNoneBlank(securityProtocol)) {
            props.put("security.protocol", securityProtocol);
        }
        return props;
    }

    @Bean
    public ConsumerFactory<String, byte[]> setupConsumerFactory() {
        logger.info("Creating setupConsumerFactory.");
        ConsumerFactory<String, byte[]> consumerFactory = new DefaultKafkaConsumerFactory<>(setupConsumerConfig(), new StringDeserializer(),
                new ByteArrayDeserializer());
        logger.info("Kafka consumer factory initialized");
        return consumerFactory;
    }

    @Lazy(false)
    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, byte[]> kafkaListenerContainerFactory() {
        logger.info("Creating kafkaListenerContainerFactory.");
        final ConcurrentKafkaListenerContainerFactory<String, byte[]> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(setupConsumerFactory());
        factory.setRetryTemplate(retryTemplate());
        factory.setConcurrency(kafkaConcurrencyFactor);
        factory.getContainerProperties().setAckMode(ContainerProperties.AckMode.MANUAL);
        factory.getContainerProperties().setAckOnError(false);

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
