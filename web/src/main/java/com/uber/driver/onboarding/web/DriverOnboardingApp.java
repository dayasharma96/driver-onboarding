package com.uber.driver.onboarding.web;

import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan("com.uber")
@EnableAutoConfiguration
public class DriverOnboardingApp {

    private static final String SPRING_ACTIVE_PROFILE_KEY = "spring.profiles.active";
    private static final String ACCESS_LOG_FILE_KEY = "ACCESS_LOG_FILE";
    private static final String APP_LOG_FILE_KEY = "LOG_FILE";

    public static void main(String[] args) {
        setDefaultParameters();
        SpringApplication.run(DriverOnboardingApp.class, args);
        System.out.println("Started Driver Onboarding Application");
    }

    private static void setDefaultParameters() {
        String springProfileActive = System.getProperty(SPRING_ACTIVE_PROFILE_KEY);
        String accessLogPath = System.getProperty(ACCESS_LOG_FILE_KEY);
        String appLogPath = System.getProperty(APP_LOG_FILE_KEY);
        if (StringUtils.isBlank(springProfileActive)) {
            System.out.println("spring.profiles.active system property is missing. Setting value dev");
            System.setProperty(SPRING_ACTIVE_PROFILE_KEY, "dev");
        }
        if (StringUtils.isBlank(accessLogPath)) {
            System.setProperty(ACCESS_LOG_FILE_KEY, "/tmp/driver-onboarding/access.log");
        }
        if (StringUtils.isBlank(appLogPath)) {
            System.setProperty(APP_LOG_FILE_KEY, "/tmp/driver-onboarding/app.log");
        }
    }

}
