package com.uber.driver.onboarding.core.repository.dao;

import com.uber.driver.onboarding.core.repository.entity.User;
import com.uber.driver.onboarding.model.enums.UserType;

public interface IUserDao extends GenericDao<User> {
    User findByEmailType(String email, UserType type);
}
