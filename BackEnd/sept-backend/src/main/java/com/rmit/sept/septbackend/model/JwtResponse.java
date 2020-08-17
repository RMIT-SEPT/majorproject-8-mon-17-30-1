package com.rmit.sept.septbackend.model;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString
public class JwtResponse {
    private final String type = "Bearer";
    private String token;
    private String username;
    private Role role;
}
