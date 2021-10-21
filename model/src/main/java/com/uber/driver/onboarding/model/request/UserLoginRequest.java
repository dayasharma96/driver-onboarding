package com.uber.driver.onboarding.model.request;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.uber.driver.onboarding.model.enums.UserType;
import com.uber.driver.onboarding.model.exception.InvalidRequestArgument;
import com.uber.driver.onboarding.model.util.JsonUtil;
import org.apache.commons.lang3.StringUtils;

@JsonIgnoreProperties(ignoreUnknown = true)
public class UserLoginRequest {

    private final String email;
    private final String password;
    private final UserType userType;

    @JsonCreator
    public UserLoginRequest(@JsonProperty("email") String email, @JsonProperty("password") String password, @JsonProperty("userType") UserType userType) {
        if (StringUtils.isBlank(email)) {
            throw new InvalidRequestArgument("Email cannot be blank.");
        }
        if (StringUtils.isBlank(password)) {
            throw new InvalidRequestArgument("Password cannot be blank.");
        }
        if (userType == null) {
            throw new InvalidRequestArgument("UserType cannot be null.");
        }
        this.email = email;
        this.password = password;
        this.userType = userType;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public UserType getUserType() {
        return userType;
    }

    @Override
    public String toString() {
        return JsonUtil.toJson(this);
    }
}
