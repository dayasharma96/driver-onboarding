package com.uber.driver.onboarding.model.util;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.StringWriter;

public class JsonUtil {

    private static final Logger logger = LoggerFactory.getLogger(JsonUtil.class);

    private static final ObjectMapper MAPPER = getNewObjectMapper();

    public static final ObjectMapper defaultTypeObjectMapper = getDefaultTypingObjectMapper();

    public static ObjectMapper getObjectMapper() {
        return MAPPER;
    }

    public static <T> T toObject(String json, Class<T> clazz) {
        return toObject(json, clazz, getObjectMapper());
    }

    public static <T> T toDefaultObject(String json, Class<T> clazz) {
        return toObject(json, clazz, getDefaultTypingObjectMapper());
    }

    public static <T> T toObject(Object input, Class<T> clazz) {
        return toObject(input, clazz, getObjectMapper());
    }

    public static <T> T toObject(Object input, Class<T> clazz, ObjectMapper mapper) {
        if (mapper == null) {
            mapper = getObjectMapper();
        }
        T object = null;
        try {
            object = mapper.convertValue(input, clazz);
        } catch (Exception e) {
            logger.error("Exception while Deserializing input to Object(" + clazz.getName() + ")\nInput: " + input.toString(), e);
        }
        return object;
    }

    public static <T> T toObject(String json, Class<T> clazz, ObjectMapper mapper) {
        if (mapper == null) {
            mapper = getObjectMapper();
        }
        T object = null;
        try {
            object = mapper.reader(clazz).readValue(json);
        } catch (IOException e) {
            logger.error("Exception while Deserializing Json to Object(" + clazz.getName() + ")\nJson: " + json, e);
        }
        return object;
    }

    public static String toJson(Object object) {
        return toJson(object, false, MAPPER);
    }

    public static String toJson(Object object, boolean prettify) {
        return toJson(object, prettify, MAPPER);
    }

    public static String toJson(Object object, ObjectMapper mapper) {
        return toJson(object, false, mapper);
    }

    public static String toJson(Object object, boolean prettify, ObjectMapper mapper) {
        StringWriter writer = new StringWriter();
        if (mapper == null) {
            mapper = getObjectMapper();
        }
        try {
            if (prettify) {
                mapper.writerWithDefaultPrettyPrinter().writeValue(writer, object);
            } else {
                mapper.writer().writeValue(writer, object);
            }
        } catch (IOException e) {
            logger.error("Exception while Serializing Object(" + object + ") to Json.", e);
        }
        return writer.toString();
    }

    private static ObjectMapper getDefaultTypingObjectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.enableDefaultTyping();
        logger.info("New ObjectMapper Created => " + mapper);
        return mapper;
    }

    private static ObjectMapper getNewObjectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        logger.info("New ObjectMapper Created => " + mapper);
        mapper.disable(DeserializationFeature.ADJUST_DATES_TO_CONTEXT_TIME_ZONE);
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        return mapper;
    }

    public static <T> T toType(String json, TypeReference<T> typeRef) {
        T value = null;
        try {
            value = MAPPER.readValue(json, typeRef);
        } catch (IOException e) {
            logger.error("Exception while Deserializing Json to Map(" + typeRef.getType() + ")\nJson: " + json, e);
        }
        return value;
    }

    public static <T> T toMap(String json, TypeReference<T> typeRef) {
        return toType(json, typeRef);
    }

    public static <T> T toList(String json, TypeReference<T> typeRef) {
        return toType(json, typeRef);
    }

}
