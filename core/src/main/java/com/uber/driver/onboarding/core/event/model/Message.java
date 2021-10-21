package com.uber.driver.onboarding.core.event.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;

public class Message {

    @JsonProperty("messageId")
    private final String messageId;

    @JsonProperty("messageType")
    private final MessageType messageType;

    @JsonProperty("body")
    private final String body;

    @JsonProperty("headers")
    private final Map<String, Object> headers;

    @JsonCreator
    public Message(@JsonProperty("messageId") String messageId, @JsonProperty("messageType") MessageType messageType, @JsonProperty("body") String body, @JsonProperty("headers") Map<String, Object> headers) {
        this.messageId = messageId;
        this.messageType = messageType;
        this.body = body;
        this.headers = headers;
    }

    public String getMessageId() {
        return messageId;
    }

    public MessageType getMessageType() {
        return messageType;
    }

    public String getBody() {
        return body;
    }

    public Map<String, Object> getHeaders() {
        return headers;
    }

    @Override
    public String toString() {
        return "Message{" +
                "messageId='" + messageId + '\'' +
                ", messageType=" + messageType +
                ", body='" + body + '\'' +
                ", headers=" + headers +
                '}';
    }
}
