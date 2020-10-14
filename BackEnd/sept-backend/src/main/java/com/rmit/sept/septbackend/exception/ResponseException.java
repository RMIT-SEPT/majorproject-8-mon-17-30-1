package com.rmit.sept.septbackend.exception;

import lombok.*;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ResponseException extends RuntimeException {
    private List<Error> errors;
}
