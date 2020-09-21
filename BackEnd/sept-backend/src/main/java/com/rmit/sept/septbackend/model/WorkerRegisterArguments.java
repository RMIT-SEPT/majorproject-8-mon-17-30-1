package com.rmit.sept.septbackend.model;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.*;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@ToString
@JsonNaming(PropertyNamingStrategy.KebabCaseStrategy.class)
public class WorkerRegisterArguments extends AbstractRegisterArguments {
    public WorkerRegisterArguments() {
        super(Role.WORKER);
    }
}
