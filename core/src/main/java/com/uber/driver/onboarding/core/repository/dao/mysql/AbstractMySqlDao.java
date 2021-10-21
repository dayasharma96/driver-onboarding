package com.uber.driver.onboarding.core.repository.dao.mysql;

import com.uber.driver.onboarding.core.repository.dao.GenericDao;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractMySqlDao<T> implements GenericDao<T> {

    private static final Logger log = LoggerFactory.getLogger(AbstractMySqlDao.class);
    private final Class<T> clazz;

    protected final SessionFactory sessionFactory;

    public AbstractMySqlDao(Class<T> clazz, SessionFactory sessionFactory) {
        this.clazz = clazz;
        this.sessionFactory = sessionFactory;
    }

    @SuppressWarnings("unchecked")
    public T findById(String id) {
        return (T) sessionFactory.getCurrentSession().createCriteria(clazz)
                .add(Restrictions.eq("id", id)).uniqueResult();
    }

    public void saveOrUpdate(T entity) {
        sessionFactory.getCurrentSession().saveOrUpdate(entity);
    }

}
