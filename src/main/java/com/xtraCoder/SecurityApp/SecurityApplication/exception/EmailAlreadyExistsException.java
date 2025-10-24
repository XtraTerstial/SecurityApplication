package com.xtraCoder.SecurityApp.SecurityApplication.exception;

import org.springframework.security.authentication.BadCredentialsException;

public class EmailAlreadyExistsException extends BadCredentialsException {
    public EmailAlreadyExistsException(String message) {
        super(message);
    }
}
