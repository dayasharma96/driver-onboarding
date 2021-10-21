package com.uber.driver.onboarding.core.repository.dao.mysql;

import com.uber.driver.onboarding.core.repository.entity.User;
import org.hibernate.SessionFactory;

import java.io.Serializable;

public class UserDao extends AbstractMySqlDao<User> implements Serializable {

    private static final long serialVersionUID = 238513884602399712L;

    public UserDao(Class<User> clazz, SessionFactory sessionFactory) {
        super(clazz, sessionFactory);
    }
}
