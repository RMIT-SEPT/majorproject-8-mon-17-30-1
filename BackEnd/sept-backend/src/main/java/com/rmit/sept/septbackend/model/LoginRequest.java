package com.rmit.sept.septbackend.model;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString
public class LoginRequest {
    private String username;
    private String password;
}
