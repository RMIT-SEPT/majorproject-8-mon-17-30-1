package com.rmit.sept.septbackend.model;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "role", visible = true)
@JsonSubTypes({
        @JsonSubTypes.Type(value = AdminRegisterArguments.class, name = "ADMIN"),
        @JsonSubTypes.Type(value = WorkerRegisterArguments.class, name = "WORKER"),
        @JsonSubTypes.Type(value = CustomerRegisterArguments.class, name = "CUSTOMER")
})
public interface RoleRegisterArguments {
    Role getRole();

    void setRole(Role role);
}
