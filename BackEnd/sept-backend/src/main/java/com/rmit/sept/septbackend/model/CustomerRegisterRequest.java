package com.rmit.sept.septbackend.model;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.*;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@ToString
@JsonNaming(PropertyNamingStrategy.KebabCaseStrategy.class)
public class CustomerRegisterRequest extends AbstractRegisterRequest {
    private String streetAddress;
    private String city;
    @Enumerated(EnumType.STRING)
    private State state;
    private String postcode;
}
