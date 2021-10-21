package com.uber.driver.onboarding.core.event.kafka.serde;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.common.errors.SerializationException;
import org.apache.kafka.common.serialization.Serializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * Serializer class used with kafka producer using jackson.
 *
 * @param <T>
 * @author drs
 */
public class KafkaJsonSerializer<T> implements Serializer<T> {

    private static final Logger log = LoggerFactory.getLogger(KafkaJsonSerializer.class);
    private final ObjectMapper mapper;

    public KafkaJsonSerializer(ObjectMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {

    }

    @Override
    public byte[] serialize(String topic, T data) {
        try {
            return mapper.writeValueAsString(data).getBytes();
        } catch (Exception ex) {
            log.error("Exception while serializing data : " + data.toString());
            throw new SerializationException(ex);
        }
    }

    @Override
    public void close() {

    }
}
