package com.rmit.sept.septbackend.model;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.*;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@ToString
@JsonNaming(PropertyNamingStrategy.KebabCaseStrategy.class)
public class CustomerRegisterArguments extends AbstractRegisterArguments {
    private String streetAddress;
    private String city;
    @Enumerated(EnumType.STRING)
    private State state;
    private String postcode;

    public CustomerRegisterArguments(String streetAddress, String city, State state, String postcode) {
        super(Role.CUSTOMER);
        this.streetAddress = streetAddress;
        this.city = city;
        this.state = state;
        this.postcode = postcode;
    }
}
