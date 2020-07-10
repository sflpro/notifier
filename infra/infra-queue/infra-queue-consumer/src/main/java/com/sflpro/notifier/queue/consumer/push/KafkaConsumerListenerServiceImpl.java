package com.sflpro.notifier.queue.consumer.push;

import com.sflpro.notifier.queue.amqp.rpc.RPCQueueMessageHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

/**
 * Company: SFL LLC
 * Created on 06/02/2018
 *
 * @author William Arustamyan
 */
@Service
@ConditionalOnProperty(name = "notifier.queue.engine", havingValue = "kafka")
public class KafkaConsumerListenerServiceImpl {

    private static final Logger LOGGER = LoggerFactory.getLogger(KafkaConsumerListenerServiceImpl.class);

    @Autowired
    private RPCQueueMessageHandler amqpRpcQueueMessageHandler;

    @KafkaListener(topics = "${kafka.topic.names}", properties = {"${kafka.auto.offset.reset:latest}"})
    public void listen(byte[] model) {
        LOGGER.debug("Listening kafka topic");
        amqpRpcQueueMessageHandler.handleMessage(model);
    }
}