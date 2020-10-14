package com.rmit.sept.septbackend.exception;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;
import java.util.Collections;

@ControllerAdvice
public class BasicExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler({Exception.class})
    public ResponseEntity<Object> handleAll(Exception ex, WebRequest request) {
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        ResponseError responseError = new ResponseError(
                ((ServletWebRequest) request).getRequest().getRequestURI(),
                status,
                "An unexpected error occurred",
                ex.getLocalizedMessage()
        );
        return new ResponseEntity<>(responseError, new HttpHeaders(), status);
    }

    @ExceptionHandler({ResponseException.class})
    public ResponseEntity<Object> handleResponse(ResponseException ex, WebRequest request) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        ResponseError responseError = new ResponseError(
                ((ServletWebRequest) request).getRequest().getRequestURI(),
                status,
                ex.getErrors()
        );
        return new ResponseEntity<>(responseError, new HttpHeaders(), status);
    }

    @ExceptionHandler({InternalAuthenticationServiceException.class})
    public ResponseEntity<Object> handleResponse(InternalAuthenticationServiceException ex, WebRequest request) {
        return this.handleUnauthorisedException(ex, request, "Your username/password is incorrect");
    }


    @ExceptionHandler({BadCredentialsException.class})
    public ResponseEntity<Object> handleResponse(BadCredentialsException ex, WebRequest request) {
        return this.handleUnauthorisedException(ex, request, "Your username/password is incorrect");
    }

    @ExceptionHandler({AuthenticationException.class})
    public ResponseEntity<Object> handleResponse(AuthenticationException ex, WebRequest request) {
        return this.handleUnauthorisedException(ex, request, "You are not authorized to perform this operation");
    }

    private ResponseEntity<Object> handleUnauthorisedException(AuthenticationException ex, WebRequest request, String message) {
        HttpStatus status = HttpStatus.UNAUTHORIZED;
        ResponseError responseError = new ResponseError(
                ((ServletWebRequest) request).getRequest().getRequestURI(),
                status,
                message,
                ex.getLocalizedMessage()
        );
        return new ResponseEntity<>(responseError, new HttpHeaders(), status);
    }


    @Override
    protected ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body, HttpHeaders headers, HttpStatus status, WebRequest request) {
        ResponseError responseError = new ResponseError(
                LocalDateTime.now(),
                ((ServletWebRequest) request).getRequest().getRequestURI(),
                status.value(),
                status.getReasonPhrase(),
                Collections.singletonList(
                        new Error(
                                "An unexpected error occurred",
                                ex.getLocalizedMessage()
                        )
                )
        );
        return new ResponseEntity<>(responseError, headers, status);
    }
}

