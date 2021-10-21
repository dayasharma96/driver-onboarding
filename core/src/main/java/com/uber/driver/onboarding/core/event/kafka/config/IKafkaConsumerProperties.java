package com.uber.driver.onboarding.core.event.kafka.config;

import java.util.List;

public interface IKafkaConsumerProperties {

    enum OFFSET_RESET {
        EARLIEST("earliest"),
        LATEST("latest"),
        NONE("none");

        private final String value;

        OFFSET_RESET(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }

    String getBootstrapServers();
    String getGroupId();
    List<String> getTopics();
    int getMaxPollRecordAtTime();
    int getMaxPollIntervalInMs();
    int getSessionTimeOutInMs();
    boolean enableAutoCommit();
    OFFSET_RESET getOffsetResetOnDeletion();
}