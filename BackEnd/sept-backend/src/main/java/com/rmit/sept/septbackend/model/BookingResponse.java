package com.rmit.sept.septbackend.model;

import lombok.*;
import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString
public class BookingResponse {
    private String serviceName;
    private String workerFullName;
    private LocalDateTime date;
    private int bookingId;
    private String customerUsername;

}
