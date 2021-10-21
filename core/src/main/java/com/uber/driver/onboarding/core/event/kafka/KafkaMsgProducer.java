package com.uber.driver.onboarding.core.event.kafka;

import com.uber.driver.onboarding.core.event.handler.IMessagePublisher;
import com.uber.driver.onboarding.core.event.model.Message;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;

/**
 * Base class for all producers.
 *
 * @author drs
 */
public class KafkaMsgProducer implements IMessagePublisher, Serializable {

    private static final Logger log = LoggerFactory.getLogger(KafkaMsgProducer.class);
    private static final long serialVersionUID = 2764860601836167523L;

    private final KafkaProducer<String, Message> kafkaProducer;

    public KafkaMsgProducer(KafkaProducer<String, Message> kafkaProducer) {
        this.kafkaProducer = kafkaProducer;
    }

    @Override
    public void publishMessage(Message message) {
        ProducerRecord<String, Message> producerRecord = new ProducerRecord<>(message.getMessageType().getEventTopic(), message.getMessageId(), message);
        try {
            RecordMetadata recordMetadata = kafkaProducer.send(producerRecord, ((metadata, exception) -> {
                if (null != exception) {
                    log.error("Error while sending message to kafka: " + message, exception);
                }
            })).get();
            if(recordMetadata == null) {
                throw new RuntimeException("Exception while publishing as record metadata is null.");
            }
        } catch (Exception e) {
            log.error("Error occured while producing message: " + message.toString(), e);
            throw new RuntimeException(e);
        }
    }
}
