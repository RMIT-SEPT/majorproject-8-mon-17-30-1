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
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class JwtResponse implements Response {
    private final String type = "Bearer";
    private String token;
    private String username;
    private Role role;

    @Override
    public HttpStatus getStatus() {
        return HttpStatus.OK;
    }
}
