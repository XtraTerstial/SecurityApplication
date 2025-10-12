package com.xtraCoder.SecurityApp.SecurityApplication.advice;

import com.xtraCoder.SecurityApp.SecurityApplication.exception.ResourseNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourseNotFoundException.class)
    public ResponseEntity<ApiError> handleResourceNotFoundException(ResourseNotFoundException exception) {
        ApiError apiError = new ApiError(exception.getLocalizedMessage(), HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(apiError, HttpStatus.NOT_FOUND);
    }

}
