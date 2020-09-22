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
    private int serviceId;
    private int WorkerId;
    private String customerUsername;
//    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
//    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime bookingTime;
}
