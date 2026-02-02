package com.freequestions.questions_api.exceptions.model;

import java.time.OffsetDateTime;
import java.util.Map;

public class ApiErrorResponse {
    private final int status;
    private final String error;
    private final String message;
    private final OffsetDateTime timestamp;
    private final Map<String, String> fieldErrors;

    public ApiErrorResponse(int status, String error, String message, Map<String, String> fieldErrors) {
        this.status = status;
        this.error = error;
        this.message = message;
        this.fieldErrors = fieldErrors;
        this.timestamp = OffsetDateTime.now();
    }

    public int getStatus() {
        return status;
    }

    public String getError() {
        return error;
    }

    public String getMessage() {
        return message;
    }

    public OffsetDateTime getTimestamp() {
        return timestamp;
    }

    public Map<String, String> getFieldErrors() {
        return fieldErrors;
    }
}
