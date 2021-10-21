package com.uber.driver.onboarding.api.service.impl;

import com.uber.driver.onboarding.api.service.IUserService;
import com.uber.driver.onboarding.core.repository.dao.mysql.UserDao;
import com.uber.driver.onboarding.core.repository.entity.User;
import com.uber.driver.onboarding.model.enums.UserType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UserService implements IUserService {

    private static final Logger log = LoggerFactory.getLogger(UserService.class);

    private final UserDao userDao;

    public UserService(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public User signup(UserType type, String email, String password) {
        User user = User.buildNew(type, email, password);
        userDao.saveOrUpdate(user);
        return user;
    }
}
