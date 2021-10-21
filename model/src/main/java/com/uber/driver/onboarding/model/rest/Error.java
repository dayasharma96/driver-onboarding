package com.uber.driver.onboarding.model.rest;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.uber.driver.onboarding.model.enums.FailureType;

public class Error {

    private final FailureType failureType;
    private final String errorMessage;
    private final String stackTrace;

    @JsonCreator
    public Error(@JsonProperty("failureType") FailureType failureType, @JsonProperty("errorMessage") String errorMessage, @JsonProperty("stackTrace") String stackTrace) {
        this.failureType = failureType;
        this.errorMessage = errorMessage;
        this.stackTrace = stackTrace;
    }

    public FailureType getFailureType() {
        return failureType;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public String getStackTrace() {
        return stackTrace;
    }

    @Override
    public String toString() {
        return "Error{" +
                "failureType=" + failureType +
                ", errorMessage='" + errorMessage + '\'' +
                ", stackTrace='" + stackTrace + '\'' +
                '}';
    }
}
