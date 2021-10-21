package com.uber.driver.onboarding.core.event.kafka.config;

import java.util.List;

public class KafkaConsumerProps implements IKafkaConsumerProperties {

    private final String bootstrapServers;
    private final String groupId;
    private final List<String> topics;

    public KafkaConsumerProps(String bootstrapServers, String groupId, List<String> topics) {
        this.bootstrapServers = bootstrapServers;
        this.groupId = groupId;
        this.topics = topics;
    }

    @Override
    public String getBootstrapServers() {
        return bootstrapServers;
    }

    @Override
    public String getGroupId() {
        return groupId;
    }

    @Override
    public List<String> getTopics() {
        return topics;
    }

    @Override
    public int getMaxPollRecordAtTime() {
        return 100;
    }

    @Override
    public int getMaxPollIntervalInMs() {
        return 120000;
    }

    @Override
    public int getSessionTimeOutInMs() {
        return 60000;
    }

    @Override
    public boolean enableAutoCommit() {
        return false;
    }

    @Override
    public OFFSET_RESET getOffsetResetOnDeletion() {
        return OFFSET_RESET.EARLIEST;
    }
}
