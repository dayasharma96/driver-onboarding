package com.uber.driver.onboarding.web.configuration;

import com.uber.driver.onboarding.api.service.IUserService;
import com.uber.driver.onboarding.api.service.impl.UserService;
import com.uber.driver.onboarding.core.repository.dao.mysql.UserDao;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApiContext {

    @Bean
    public IUserService getUserService(UserDao userDao) {
        return new UserService(userDao);
    }

}
