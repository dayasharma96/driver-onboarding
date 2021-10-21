package com.uber.driver.onboarding.core.event;

import com.uber.driver.onboarding.core.event.kafka.config.IKafkaConsumerProperties;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.Deserializer;

import java.util.Properties;

/**
 * @author drs
 */
public class ConsumerFactory {

    public static <K, V> KafkaConsumer<K, V> buildConsumer(IKafkaConsumerProperties consumerProperties, Deserializer<K> keyDeserializer, Deserializer<V> valueDeserializer) {
        Properties props = new Properties();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, consumerProperties.getBootstrapServers());
        props.put(ConsumerConfig.GROUP_ID_CONFIG, consumerProperties.getGroupId());
        props.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, consumerProperties.getMaxPollRecordAtTime());
        props.put(ConsumerConfig.MAX_POLL_INTERVAL_MS_CONFIG, consumerProperties.getMaxPollIntervalInMs());
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, String.valueOf(consumerProperties.enableAutoCommit()));
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, consumerProperties.getOffsetResetOnDeletion().getValue());
        props.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, String.valueOf(consumerProperties.getSessionTimeOutInMs()));
        KafkaConsumer<K, V> consumer = new KafkaConsumer<>(props, keyDeserializer, valueDeserializer);
        consumer.subscribe(consumerProperties.getTopics());
        return consumer;
    }

}