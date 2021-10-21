package com.uber.driver.onboarding.model.exception;

public class InvalidRequestArgument extends RuntimeException {

    private static final long serialVersionUID = 5094859570810251006L;

    public InvalidRequestArgument() {
        super();
    }

    public InvalidRequestArgument(String message) {
        super(message);
    }

    public InvalidRequestArgument(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidRequestArgument(Throwable cause) {
        super(cause);
    }

    protected InvalidRequestArgument(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
