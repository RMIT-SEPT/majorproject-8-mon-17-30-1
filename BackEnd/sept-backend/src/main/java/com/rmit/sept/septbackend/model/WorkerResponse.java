package com.rmit.sept.septbackend.model;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString
public class WorkerResponse {
    private int workerId;
    private String userName;
    private String firstName;
    private String lastName;
}
