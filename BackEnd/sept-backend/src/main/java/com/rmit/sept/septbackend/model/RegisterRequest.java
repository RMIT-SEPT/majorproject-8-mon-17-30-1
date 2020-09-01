package com.rmit.sept.septbackend.model;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.*;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString
@JsonNaming(PropertyNamingStrategy.KebabCaseStrategy.class)
public class RegisterRequest {
    private String username;
    private String password;
    private String firstName;
    private String lastName;
    private String streetAddress;
    private String city;
    @Enumerated(EnumType.STRING)
    private State state;
    private String postcode;
}
