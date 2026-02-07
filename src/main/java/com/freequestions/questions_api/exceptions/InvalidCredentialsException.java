package com.freequestions.questions_api.exceptions;

public class InvalidCredentialsException extends RuntimeException {
    public InvalidCredentialsException() {

        super("Invalid email or password");
    }
}
