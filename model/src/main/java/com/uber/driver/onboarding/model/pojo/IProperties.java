package com.uber.driver.onboarding.model.pojo;

public interface IProperties {

    String getPropertyValue(String propertyName);

    String getPropertyValue(String propertyName, String defaultValue);

    boolean getBooleanPropertyValue(String propertyName, boolean defaultValue);

    int getIntPropertyValue(String propertyName, int defaultValue);

    long getLongPropertyValue(String propertyName, long defaultValue);
}
