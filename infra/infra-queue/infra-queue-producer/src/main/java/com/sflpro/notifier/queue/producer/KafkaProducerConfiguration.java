package com.sflpro.notifier.queue.producer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sflpro.notifier.queue.QueueConfigurationDefaults;
import com.sflpro.notifier.queue.producer.connector.AmqpConnectorService;
import com.sflpro.notifier.queue.producer.connector.impl.KafkaConnectorServiceImpl;
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
