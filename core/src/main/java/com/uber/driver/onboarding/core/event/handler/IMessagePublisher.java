package com.uber.driver.onboarding.core.event.handler;


import com.uber.driver.onboarding.core.event.model.Message;

public interface IMessagePublisher {

    void publishMessage(Message message);

    default void assertionError(boolean success, String msg) {
        if (success) {
            throw new RuntimeException(msg);
        }
    }

    default void validateMessage(Message message) {

    }

}
