package com.uber.driver.onboarding.core.event.kafka.config;

public interface IKafkaProducerProperties {
    String getBootstrapServers();
    String getAcknowledgmentType();
    String getClientId();
}