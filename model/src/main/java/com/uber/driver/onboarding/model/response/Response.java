package com.uber.driver.onboarding.model.response;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.uber.driver.onboarding.model.rest.Error;

import java.util.List;
import java.util.Map;

/**
 * @param <T> Template response class.
 * @author drs
 */
public class Response<T> {

    private final int statusCode;
    private final T responseBody;
    private final Error error;
    private final Map<String, List<String>> headers;

    @JsonCreator
    private Response(@JsonProperty("statusCode") int statusCode, @JsonProperty("responseBody") T responseBody, @JsonProperty("error") Error error, @JsonProperty("headers") Map<String, List<String>> headers) {
        this.statusCode = statusCode;
        this.responseBody = responseBody;
        this.error = error;
        this.headers = headers;
    }

    public static class Builder<T> {
        private int statusCode;
        private T responseBody;
        private Error error;
        private Map<String, List<String>> headers;

        public Builder<T> withStatus(int code) {
            this.statusCode = code;
            return this;
        }

        public Builder<T> withError(Error error) {
            this.error = error;
            return this;
        }

        public Builder<T> withBody(T body) {
            this.responseBody = body;
            return this;
        }

        public Builder<T> withHeaders(Map<String, List<String>> headers) {
            this.headers = headers;
            return this;
        }

        public Response<T> build() {
            return new Response<>(statusCode, responseBody, error, headers);
        }

    }

    public int getStatusCode() {
        return statusCode;
    }

    public T getResponseBody() {
        return responseBody;
    }

    public Error getError() {
        return error;
    }

    public Map<String, List<String>> getHeaders() {
        return headers;
    }

    @Override
    public String toString() {
        return "Response{" +
                "statusCode=" + statusCode +
                ", responseBody=" + responseBody +
                ", error=" + error +
                ", headers=" + headers +
                '}';
    }
}