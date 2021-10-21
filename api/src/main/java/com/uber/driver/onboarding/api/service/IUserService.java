package com.uber.driver.onboarding.api.service;

import com.uber.driver.onboarding.core.repository.entity.User;
import com.uber.driver.onboarding.model.enums.UserType;

public interface IUserService {
    User signup(UserType type, String email, String password);
}
