package com.uber.driver.onboarding.core.repository.dao;

import com.uber.driver.onboarding.core.repository.entity.User;

public interface IUserDao {
    User findByEmail(String email);
}
