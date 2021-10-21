package com.uber.driver.onboarding.core.event.handler;


import com.uber.driver.onboarding.core.event.model.Message;

/**
 * @author drs
 * Interface to handle event msgs from any type of event consumer(kafka, storm, flink, etc)..
 */
public interface IMessageHandler {

    void validateMessage(Message message);

    void handleMessage(Message message);

}
