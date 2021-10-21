package com.uber.driver.onboarding.model.rest;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.uber.driver.onboarding.model.enums.FailureType;

public class Error {

    private final FailureType failureType;
    private final String errorMessage;

    @JsonCreator
    public Error(@JsonProperty("failureType") FailureType failureType, @JsonProperty("errorMessage") String errorMessage) {
        this.failureType = failureType;
        this.errorMessage = errorMessage;
    }

    public FailureType getFailureType() {
        return failureType;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    @Override
    public String toString() {
        return "Error{" +
                "failureType=" + failureType +
                ", errorMessage='" + errorMessage + '\'' +
                '}';
    }
}
