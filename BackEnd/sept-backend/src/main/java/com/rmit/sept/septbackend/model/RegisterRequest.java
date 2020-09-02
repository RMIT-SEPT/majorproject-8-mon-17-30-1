package com.rmit.sept.septbackend.model;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "role", visible = true)
@JsonSubTypes({
        @JsonSubTypes.Type(value = AdminRegisterRequest.class, name = "ADMIN"),
        @JsonSubTypes.Type(value = WorkerRegisterRequest.class, name = "WORKER"),
        @JsonSubTypes.Type(value = CustomerRegisterRequest.class, name = "CUSTOMER")
})
public interface RegisterRequest {
    String getUsername();

    void setUsername(String username);

    String getPassword();

    void setPassword(String password);

    String getFirstName();

    void setFirstName(String firstName);

    String getLastName();

    void setLastName(String lastName);

    Role getRole();
}
