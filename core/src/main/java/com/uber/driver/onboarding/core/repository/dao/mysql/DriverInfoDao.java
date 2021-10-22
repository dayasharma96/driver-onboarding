package com.uber.driver.onboarding.core.repository.dao.mysql;

import com.uber.driver.onboarding.core.repository.dao.IDriverInfoDao;
import com.uber.driver.onboarding.core.repository.entity.DriverInfo;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.io.Serializable;

@Transactional
public class DriverInfoDao extends AbstractMySqlDao<DriverInfo> implements Serializable, IDriverInfoDao {

    private static final long serialVersionUID = 475692382715734651L;

    public DriverInfoDao(Class<DriverInfo> clazz, EntityManager entityManager) {
        super(clazz, entityManager);
    }


}
