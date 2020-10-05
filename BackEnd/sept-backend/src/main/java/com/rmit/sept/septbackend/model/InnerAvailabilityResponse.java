package com.rmit.sept.septbackend.model;

import lombok.*;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString
public class InnerAvailabilityResponse {
    private int serviceWorkerAvailabilityId;
    private String serviceName;
    private DayOfWeek day;
    private LocalTime startTime;
    private LocalTime endTime;
    private LocalDate effectiveStartDate;
    private LocalDate effectiveEndDate;
}
