package com.uber.driver.onboarding.api.service.impl;

import com.uber.driver.onboarding.api.service.IDriverOnboarding;
import com.uber.driver.onboarding.core.event.kafka.KafkaMsgProducer;
import com.uber.driver.onboarding.core.event.model.Message;
import com.uber.driver.onboarding.core.event.model.MessageType;
import com.uber.driver.onboarding.core.repository.dao.mysql.UserDao;
import com.uber.driver.onboarding.core.repository.entity.User;
import com.uber.driver.onboarding.model.enums.DriverState;
import com.uber.driver.onboarding.model.enums.UserType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DriverOnboardingService implements IDriverOnboarding {

    private static final Logger log = LoggerFactory.getLogger(UserService.class);

    private final UserDao userDao;
    private final KafkaMsgProducer msgProducer;

    public DriverOnboardingService(UserDao userDao, KafkaMsgProducer msgProducer) {
        this.userDao = userDao;
        this.msgProducer = msgProducer;
    }

    @Override
    public void initDriverVerification(String userId) {
        User user = userDao.findById(userId);
        validateDriver(user);
        user.setDriverState(DriverState.BACKGROUND_VERIFICATION_IN_PROGRESS);

        // send Event for processing
        sendStateEvent(user);

        // Update user state
        userDao.saveOrUpdate(user);
    }

    @Override
    public void confirmDriverVerification(String userId) {
        User user = userDao.findById(userId);
        user.setDriverState(DriverState.BACKGROUND_VERIFIED);
        sendStateEvent(user);
        userDao.saveOrUpdate(user);
    }

    private void validateDriver(User user) {
        assert user != null;
        assert user.getUserType().equals(UserType.DRIVER);
        if (user.getDriverState() != DriverState.DOCUMENT_COLLECTED) {
            throw new RuntimeException("Driver documents not collected or in-progress or already verified.");
        }
    }

    private void sendStateEvent(User user) {
        MessageType messageType = MessageType.lookUp(user.getDriverState());
        if(messageType != null) {
            msgProducer.publishMessage(new Message(user.getId(), messageType, user.toString(), null));
        }
    }

}
