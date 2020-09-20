package com.rmit.sept.septbackend.model;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString
//@JsonNaming(PropertyNamingStrategy.KebabCaseStrategy.class)
public class WorkerResponse {
    private int workerId;
    private String firstName;
    private String lastName;
}
