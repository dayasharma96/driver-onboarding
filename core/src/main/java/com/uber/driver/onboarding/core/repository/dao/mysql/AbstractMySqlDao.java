package com.uber.driver.onboarding.core.repository.dao.mysql;

import com.uber.driver.onboarding.core.repository.dao.GenericDao;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

@Transactional
public abstract class AbstractMySqlDao<T> implements GenericDao<T> {

    private static final Logger log = LoggerFactory.getLogger(AbstractMySqlDao.class);

    protected final Class<T> clazz;
    protected final EntityManager entityManager;

    public AbstractMySqlDao(Class<T> clazz, EntityManager entityManager) {
        this.clazz = clazz;
        this.entityManager = entityManager;
    }

    @SuppressWarnings({"unchecked"})
    public T findById(String id) {
        return (T) entityManager.unwrap(Session.class).createCriteria(clazz)
                .add(Restrictions.eq("id", id)).uniqueResult();
    }

    public void saveOrUpdate(T entity) {
        entityManager.unwrap(Session.class).saveOrUpdate(entity);
    }

}
