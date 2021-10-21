package com.uber.driver.onboarding.core.event.kafka.serde;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.common.errors.SerializationException;
import org.apache.kafka.common.serialization.Deserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * Deserializer class used with kafka consumer using jackson.
 *
 * @param <T>
 * @author drs
 */
public class KafkaJsonDeserializer<T> implements Deserializer<T> {

    private static final Logger log = LoggerFactory.getLogger(KafkaJsonDeserializer.class);
    private final ObjectMapper mapper;
    private Class<T> clazz;

    public KafkaJsonDeserializer(ObjectMapper mapper, Class<T> clazz) {
        this.mapper = mapper;
        this.clazz = clazz;
    }

    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {
        clazz = (Class<T>) configs.get("type");
    }

    @Override
    public T deserialize(String topic, byte[] data) {
        if (data == null || data.length == 0)
            return null;
        try {
            return mapper.readValue(data, clazz);
        } catch (Exception ex) {
            String dataStr = null;
            try {
                dataStr = mapper.writeValueAsString(data);
            } catch (JsonProcessingException e) {
                log.error(e.getMessage());
            }
            log.error("Exception while de-serialising data : [{}] from topic [{}] into required type [{}] ", dataStr, topic, clazz);
            throw new SerializationException(ex);
        }
    }

    @Override
    public void close() {

    }
}
