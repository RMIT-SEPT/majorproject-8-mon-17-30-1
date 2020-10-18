package com.rmit.sept.septbackend.model;

import lombok.*;

import javax.validation.constraints.NotNull;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString
public class AvailabilityRequest {
    @NotNull
    private int workerId;
    @NotNull
    private int serviceId;
    @NotNull
    private DayOfWeek day;
    @NotNull
    private LocalTime startTime;
    @NotNull
    private LocalTime endTime;
    private LocalDate effectiveStartDate = LocalDate.now();
    @NotNull
    private LocalDate effectiveEndDate;
}
