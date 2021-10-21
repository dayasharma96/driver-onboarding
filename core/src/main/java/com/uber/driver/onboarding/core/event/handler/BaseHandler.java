package com.uber.driver.onboarding.core.event.handler;



import com.uber.driver.onboarding.core.event.model.Message;
import com.uber.driver.onboarding.core.event.model.MessageType;

import java.util.Map;

/**
 * @author drs
 * Acts as a locator for underlying message handler.
 */
public class BaseHandler implements IMessageHandler {

    private final Map<MessageType, IMessageHandler> messageHandlerMap;

    public BaseHandler(Map<MessageType, IMessageHandler> messageHandlerMap) {
        this.messageHandlerMap = messageHandlerMap;
    }

    @Override
    public void handleMessage(Message message) {
        if (messageHandlerMap.containsKey(message.getMessageType())) {
            IMessageHandler handler = messageHandlerMap.get(message.getMessageType());
            handler.validateMessage(message);
            handler.handleMessage(message);
        }
    }

    @Override
    public void validateMessage(Message message) {

    }

    public Map<MessageType, IMessageHandler> getMessageHandlerMap() {
        return messageHandlerMap;
    }
}
