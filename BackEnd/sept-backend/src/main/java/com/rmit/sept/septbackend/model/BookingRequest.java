package com.rmit.sept.septbackend.model;

import lombok.*;

import java.time.LocalDateTime;


@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode()
@ToString
public class BookingRequest {
    private int bookingId;
    private String serviceWorkerId;
    private String customerId;
    private LocalDateTime bookingTime;
    private LocalDateTime createdTime;
    private LocalDateTime lastModifiedTime;
    private Status status;
}
