package com.uber.driver.onboarding.core.event;

import com.uber.driver.onboarding.core.event.kafka.config.IKafkaProducerProperties;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.Serializer;

import java.util.Properties;

/**
 * Factory class to create producers. As of now used to create only kafka producers. Can be
 * extended to have other producer types.
 *
 * @author drs
 */
public class ProducerFactory {

    public static <K, V> KafkaProducer<K, V> buildProducer(IKafkaProducerProperties IKafkaProducerProperties, Serializer<K> keySerializer, Serializer<V> valueSerializer) {
        Properties properties = new Properties();
        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, IKafkaProducerProperties.getBootstrapServers());
        properties.put(ProducerConfig.CLIENT_ID_CONFIG, IKafkaProducerProperties.getClientId());
        properties.put(ProducerConfig.ACKS_CONFIG, IKafkaProducerProperties.getAcknowledgmentType());
        return new KafkaProducer<>(properties, keySerializer, valueSerializer);
    }

}