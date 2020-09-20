package com.rmit.sept.septbackend.model;

import lombok.*;

import javax.validation.constraints.NotNull;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString
//@JsonNaming(PropertyNamingStrategy.KebabCaseStrategy.class)
public class NewWorkerRequest {
    @NotNull
    private String username;
    @NotNull
    private String firstName;
    @NotNull
    private String lastName;
//    @NotNull
//    private RoleRegisterArguments roleArgs;
}
