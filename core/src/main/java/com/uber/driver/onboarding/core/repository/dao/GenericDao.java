package com.uber.driver.onboarding.core.repository.dao;

public interface GenericDao<T> {
    void saveOrUpdate(T value);
    T findById(String id);
}
