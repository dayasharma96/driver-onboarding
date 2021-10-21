package com.uber.driver.onboarding.core.event.kafka.config;


public class KafkaProducerProps implements IKafkaProducerProperties {

    private final String bootstrapServers;
    private final String clientId;

    public KafkaProducerProps(String bootstrapServers, String clientId) {
        this.bootstrapServers = bootstrapServers;
        this.clientId = clientId;
    }

    @Override
    public String getBootstrapServers() {
        return bootstrapServers;
    }

    @Override
    public String getAcknowledgmentType() {
        return "all";
    }

    @Override
    public String getClientId() {
        return clientId;
    }
}
