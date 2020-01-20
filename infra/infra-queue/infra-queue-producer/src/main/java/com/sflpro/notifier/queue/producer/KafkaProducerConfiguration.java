package com.sflpro.notifier.queue.producer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sflpro.notifier.queue.QueueConfigurationDefaults;
import com.sflpro.notifier.queue.producer.connector.AmqpConnectorService;
import com.sflpro.notifier.queue.producer.connector.impl.KafkaConnectorServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.ByteArraySerializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Lazy;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * Company: SFL LLC
 * Created on 07/02/2018
 *
 * @author Davit Harutyunyan
 */
@Configuration
@ConditionalOnProperty(name = "notifier.queue.engine", havingValue = "kafka")
@Import({QueueConfigurationDefaults.class})
public class KafkaProducerConfiguration {

    private static final Logger logger = LoggerFactory.getLogger(KafkaProducerConfiguration.class);

    @Value("${kafka.bootstrap.servers}")
    private String bootstrapServers;

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

    @Value("${notifier.queue.topic}")
    private String topicNames;

    @Bean
    public ProducerFactory<String, byte[]> createPublisherFactory() {
        logger.info("Creating ProducerFactory.");
        return new DefaultKafkaProducerFactory<>(publisherConfig());
    }

    @Bean
    public Map<String, Object> publisherConfig() {
        logger.debug("Creating publisherConfig.");
        final Map<String, Object> props = new HashMap<>();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ProducerConfig.LINGER_MS_CONFIG, 1);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, ByteArraySerializer.class);
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
    public KafkaTemplate<String, byte[]> kafkaTemplate() {
        logger.info("Creating kafkaTemplate.");
        return new KafkaTemplate<>(createPublisherFactory());
    }

    @Bean
    @Lazy(false)
    public AmqpConnectorService kafkaConnectorService(@Qualifier("amqpObjectMapper") final ObjectMapper objectMapper) {
        logger.info("Creating kafka based component of type AmqpConnectorService.");
        return new KafkaConnectorServiceImpl(topicNames, objectMapper, kafkaTemplate());
    }
}
