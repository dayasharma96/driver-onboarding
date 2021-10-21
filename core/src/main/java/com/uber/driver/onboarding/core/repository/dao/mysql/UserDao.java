package com.uber.driver.onboarding.core.repository.dao.mysql;

import com.uber.driver.onboarding.core.repository.dao.IUserDao;
import com.uber.driver.onboarding.core.repository.entity.User;
import com.uber.driver.onboarding.model.enums.UserType;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.io.Serializable;
import java.util.Date;

@Transactional
public class UserDao extends AbstractMySqlDao<User> implements Serializable, IUserDao {

    private static final long serialVersionUID = 238513884602399712L;

    public UserDao(Class<User> clazz, EntityManager entityManager) {
        super(clazz, entityManager);
    }

    @Override
    public User findByEmailType(String email, UserType type) {
        return (User) entityManager.unwrap(Session.class).createCriteria(clazz)
                .add(Restrictions.and(Restrictions.eq("email", email),
                        Restrictions.eq("userType", type))).uniqueResult();
    }

    @Override
    public void saveOrUpdate(User entity) {
        entity.setLastUpdateDate(new Date());
        super.saveOrUpdate(entity);
    }
}
