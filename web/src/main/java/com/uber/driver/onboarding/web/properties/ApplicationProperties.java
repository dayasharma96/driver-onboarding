package com.uber.driver.onboarding.web.properties;

import com.uber.driver.onboarding.model.pojo.IProperties;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Objects;

@Component
public class ApplicationProperties implements IProperties {

    private static final Logger logger = LoggerFactory.getLogger(ApplicationProperties.class);

    private final ConfigurableEnvironment environment;

    public ApplicationProperties(ConfigurableEnvironment environment) {
        Objects.requireNonNull(environment, "arg 'environment' can not be null.");
        this.environment = environment;
    }

    public boolean getBooleanPropertyValue(String propertyName, boolean defaultValue) {
        final String value = getPropertyValue(propertyName);
        boolean propValue = defaultValue;
        if (StringUtils.isNotBlank(value)) {
            propValue = Boolean.parseBoolean(value);
        }
        return propValue;
    }

    public String getPropertyValue(String propertyName) {
        return environment.getProperty(propertyName);
    }

    public String getPropertyValue(String propertyName, String defaultValue) {
        String value = environment.getProperty(propertyName);
        if (StringUtils.isBlank(value)) {
            value = defaultValue;
        }
        return value;
    }

    public int getIntPropertyValue(String propertyName, int defaultValue) {
        final String value = getPropertyValue(propertyName);
        int result = defaultValue;
        if (StringUtils.isNotBlank(value)) {
            try {
                result = Integer.parseInt(value);
            } catch (NumberFormatException nfe) {
                logger.error("Exception while reading property [{}={}]. Will fallback to default value ", propertyName, value, nfe);
            }
        }
        return result;
    }


    public long getLongPropertyValue(String propertyName, long defaultValue) {
        final String value = getPropertyValue(propertyName);
        long result = defaultValue;
        if (StringUtils.isNotBlank(value)) {
            try {
                result = Long.parseLong(value);
            } catch (NumberFormatException nfe) {
                logger.error("Exception while reading property [{}={}]. Will fallback to default value ", propertyName, value, nfe);
            }
        }
        return result;
    }

    /**
     * Returns a Boolean Property Value. Uses Default Value as a fallback in which case the failure is logged.
     *
     * @param props        Properties to use
     * @param code         property Key.
     * @param defaultValue Default value to be used in case property not present or is not a number
     * @return property value
     */
    public static boolean getBooleanPropertyValue(Map<String, String> props, String code, boolean defaultValue) {
        boolean propertyValue = defaultValue;
        String valueInPropertiesFile = getPropertyValue(props, code, null);
        if (valueInPropertiesFile != null) {
            try {
                propertyValue = Boolean.parseBoolean(valueInPropertiesFile);
            } catch (Exception e) {
                if (logger.isInfoEnabled()) {
                    logger.info("ERROR: " + "found non boolean property in method getBooleanPropertyValue()." + "SUSPECTED CAUSE: " + "Reloadable property " + code + " not correctly set."
                            + "Using default value to proceed. default Value =" + defaultValue + ".", e);
                }
                propertyValue = defaultValue; // use default value
            }
        }
        return propertyValue;
    }

    /**
     * Get the property value corresponding to a key.
     *
     * @param props        Properties to use
     * @param code         property key.
     * @param defaultValue Default Value to return if property is not found
     * @return property value.
     */
    public static String getPropertyValue(Map<String, String> props, String code, String defaultValue) {
        String value = null;
        if (props != null) {
            value = props.get(code);
        }
        if (value == null) {
            value = defaultValue;
        }

        return value;
    }

}