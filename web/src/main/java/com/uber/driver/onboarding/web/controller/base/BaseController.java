package com.uber.driver.onboarding.web.controller.base;

import com.uber.driver.onboarding.model.enums.FailureType;
import com.uber.driver.onboarding.model.rest.Error;
import com.uber.driver.onboarding.model.response.Response;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

public abstract class BaseController {

    private static final Logger logger = LoggerFactory.getLogger(BaseController.class);

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Response<?> handleIllegalArgException(IllegalArgumentException ex) {
        logger.error("IllegalArgumentException while processing request : " + ex);
        return new Response.Builder<>().withStatus(HttpStatus.BAD_REQUEST.value()).withError(new Error(FailureType.BAD_REQUEST, ExceptionUtils.getStackTrace(ex))).build();
    }

    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Response<?> handleRuntimeException(RuntimeException ex) {
        logger.error("RuntimeException while processing request : ", ex);
        return new Response.Builder<>().withStatus(HttpStatus.INTERNAL_SERVER_ERROR.value()).withError(new Error(FailureType.INTERNAL_SERVER_ERROR, ExceptionUtils.getStackTrace(ex))).build();
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Response<?> handleGenericException(Exception ex) {
        logger.error("Exception while processing request : ", ex);
        return new Response.Builder<>().withStatus(HttpStatus.INTERNAL_SERVER_ERROR.value()).withError(new Error(FailureType.INTERNAL_SERVER_ERROR, ExceptionUtils.getStackTrace(ex))).build();
    }

}
