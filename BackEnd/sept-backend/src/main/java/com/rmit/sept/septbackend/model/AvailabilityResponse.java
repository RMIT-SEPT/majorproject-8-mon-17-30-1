package com.rmit.sept.septbackend.model;

import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString
public class AvailabilityResponse {
    private int workerId;
    private LocalDate effectiveStartDate;
    private LocalDate effectiveEndDate;
    private List<InnerAvailabilityResponse> availability = new ArrayList<>();
}
