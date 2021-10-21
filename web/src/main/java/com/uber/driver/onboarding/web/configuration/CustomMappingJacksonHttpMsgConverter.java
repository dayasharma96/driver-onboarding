package com.uber.driver.onboarding.web.configuration;

import com.fasterxml.jackson.core.JsonEncoding;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.exc.InvalidDefinitionException;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConversionException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.http.converter.json.MappingJacksonInputMessage;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.Type;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

/**
 * @author drs
 * This has been customized with object mapper as rest request from nobroker web were
 * sometimes failing as it was based on an old http client dependency which implicitly uses
 * older mapper versions leading to non UTF-8 character during json deserialization.
 */
public class CustomMappingJacksonHttpMsgConverter extends MappingJackson2HttpMessageConverter {

    private static final Map<String, JsonEncoding> ENCODINGS = new HashMap<>(JsonEncoding.values().length + 1);
    private final ObjectMapper objectMapper;

    public CustomMappingJacksonHttpMsgConverter(ObjectMapper objectMapper) {
        super();
        this.objectMapper = objectMapper;
    }

    @Override
    public Object read(Type type, Class<?> contextClass, HttpInputMessage inputMessage) throws IOException, HttpMessageNotReadableException {
        JavaType javaType = this.getJavaType(type, contextClass);
        return this.readCustomJavaType(javaType, inputMessage);
    }

    @Override
    protected Object readInternal(Class<?> clazz, HttpInputMessage inputMessage) throws IOException, HttpMessageNotReadableException {
        JavaType javaType = this.getJavaType(clazz, null);
        return this.readCustomJavaType(javaType, inputMessage);
    }

    private Object readCustomJavaType(JavaType javaType, HttpInputMessage inputMessage) throws IOException {
        MediaType contentType = inputMessage.getHeaders().getContentType();
        Charset charset = this.getCharset(contentType);
        boolean isUnicode = ENCODINGS.containsKey(charset.name());

        try {
            if (inputMessage instanceof MappingJacksonInputMessage) {
                Class<?> deserializationView = ((MappingJacksonInputMessage) inputMessage).getDeserializationView();
                if (deserializationView != null) {
                    ObjectReader objectReader = this.objectMapper.readerWithView(deserializationView).forType(javaType);
                    if (isUnicode) {
                        return objectReader.readValue(inputMessage.getBody());
                    }

                    Reader reader = new InputStreamReader(inputMessage.getBody(), charset);
                    return objectReader.readValue(reader);
                }
            }

            if (isUnicode) {
                return this.objectMapper.readValue(inputMessage.getBody(), javaType);
            } else {
                Reader reader = new InputStreamReader(inputMessage.getBody(), charset);
                return this.objectMapper.readValue(reader, javaType);
            }
        } catch (InvalidDefinitionException var9) {
            throw new HttpMessageConversionException("Type definition error: " + var9.getType(), var9);
        } catch (JsonProcessingException var10) {
            throw new HttpMessageNotReadableException("JSON parse error: " + var10.getOriginalMessage(), var10, inputMessage);
        }
    }
}
