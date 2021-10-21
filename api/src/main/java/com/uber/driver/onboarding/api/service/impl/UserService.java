package com.uber.driver.onboarding.api.service.impl;

import com.uber.driver.onboarding.api.service.IUserService;
import com.uber.driver.onboarding.core.event.kafka.KafkaMsgProducer;
import com.uber.driver.onboarding.core.event.model.Message;
import com.uber.driver.onboarding.core.event.model.MessageType;
import com.uber.driver.onboarding.core.repository.dao.mysql.UserDao;
import com.uber.driver.onboarding.core.repository.entity.User;
import com.uber.driver.onboarding.core.util.SecurityUtil;
import com.uber.driver.onboarding.core.util.SessionUtil;
import com.uber.driver.onboarding.model.enums.DriverState;
import com.uber.driver.onboarding.model.enums.UserType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UserService implements IUserService {

    private static final Logger log = LoggerFactory.getLogger(UserService.class);

    private final UserDao userDao;
    private final KafkaMsgProducer msgProducer;

    public UserService(UserDao userDao, KafkaMsgProducer msgProducer) {
        this.userDao = userDao;
        this.msgProducer = msgProducer;
    }

    @Override
    public User signup(UserType type, String email, String password) {
        User user = userDao.findByEmailType(email, type);
        if (user != null) {
            throw new RuntimeException("User already exists with email : " + email + " & type : " + type);
        }
        user = User.buildNew(type, email, password);
        userDao.saveOrUpdate(user);
        SessionUtil.login(user);
        return user;
    }

    @Override
    public User login(UserType type, String email, String password) {
        User user = userDao.findByEmailType(email, type);
        if (user == null) {
            throw new RuntimeException("User not found with email : " + email + " and type : " + type);
        }
        if (SecurityUtil.isPasswordMatch(user.getPwdHash(), user.getPwdSalt(), password)) {
            SessionUtil.login(user);
            return user;
        }
        throw new RuntimeException("Invalid user credentials");
    }

    @Override
    public User findUser(String id) {
        return userDao.findById(id);
    }

}
