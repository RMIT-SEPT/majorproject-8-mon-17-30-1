package com.rmit.sept.septbackend.exception;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString
public class Error {
    private String message;
    private String debugMessage;
}
