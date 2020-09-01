package com.rmit.sept.septbackend.model;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString
public class RegisterRequest {
    private String username;
    private String password;



}
