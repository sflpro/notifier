package com.sflpro.notifier.queue;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;

/**
 * Company: SFL LLC
 * Created on 08/02/2018
 *
 * @author Davit Harutyunyan
 */
@Configuration
@ConditionalOnProperty(prefix = "queue.connector.service", value = "rabbit")
@ImportResource("applicationContext-queue-producer-amqp.xml")
public class QueueConditionalConfiguration {
}
