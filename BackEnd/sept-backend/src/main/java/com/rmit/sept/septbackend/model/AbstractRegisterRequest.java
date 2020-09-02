package com.rmit.sept.septbackend.model;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.*;

@Getter
@Setter
@EqualsAndHashCode
@ToString
@JsonNaming(PropertyNamingStrategy.KebabCaseStrategy.class)
public abstract class AbstractRegisterRequest implements RegisterRequest {
    private String username;
    private String password;
    private String firstName;
    private String lastName;
    private Role role;
}
