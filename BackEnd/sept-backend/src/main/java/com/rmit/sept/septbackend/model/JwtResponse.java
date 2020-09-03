package com.rmit.sept.septbackend.model;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.*;
import org.springframework.http.HttpStatus;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString
@JsonNaming(PropertyNamingStrategy.KebabCaseStrategy.class)
public class JwtResponse {
    private final String type = "Bearer";
    private String token;
    private String username;
    private Role role;
}
