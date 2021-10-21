package com.uber.driver.onboarding.api.handler;

import com.uber.driver.onboarding.core.event.handler.IMessageHandler;
import com.uber.driver.onboarding.core.event.model.Message;
import com.uber.driver.onboarding.core.repository.entity.User;
import com.uber.driver.onboarding.model.util.JsonUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractMsgHandler implements IMessageHandler {

    private static final Logger log = LoggerFactory.getLogger(AbstractMsgHandler.class);

    @Override
    public void handleMessage(Message message) {
        try {
            validate(message);
            processMessage(message);
        } catch (Exception ex) {
            log.error("Exception while handling message for body : {}", message, ex);
        }
    }

    private void validate(Message message) {
        assertionError(message == null, "Received null message for abstract handler.");
        assert message != null;
        assertionError(StringUtils.isBlank(message.getMessageId()), "Message Id not found for abstract Handler having msg : " + message);
        assertionError(StringUtils.isBlank(message.getBody()), "Message Body not found for abstract Handler having msg : " + message);
        validateMessage(message);
    }

    protected void assertionError(boolean success, String msg) {
        if (success) {
            throw new RuntimeException(msg);
        }
    }

    protected abstract void processMessage(Message message);

    protected User getUserFromMessage(Message message) {
        return JsonUtil.toObject(message.getBody(), User.class);
    }

}
