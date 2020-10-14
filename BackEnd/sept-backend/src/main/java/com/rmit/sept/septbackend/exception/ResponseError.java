package com.rmit.sept.septbackend.exception;

import lombok.*;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString
public class ResponseError {
    private LocalDateTime timestamp;
    private String path;
    private int status;
    private String error;
    private List<Error> errors = new ArrayList<>();

    public ResponseError(String path, HttpStatus status, List<Error> errors) {
        this.timestamp = LocalDateTime.now();
        this.path = path;
        this.status = status.value();
        this.error = status.getReasonPhrase();
        this.errors = errors;
    }

    public ResponseError(String path, HttpStatus status, String message, String debugMessage) {
        this.timestamp = LocalDateTime.now();
        this.path = path;
        this.status = status.value();
        this.error = status.getReasonPhrase();
        this.errors.add(new Error(message, debugMessage));
    }
}
