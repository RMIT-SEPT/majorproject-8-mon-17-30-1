package com.rmit.sept.septbackend.entity;

import lombok.*;

import javax.persistence.*;
import java.time.DayOfWeek;
import java.time.LocalTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode
@Entity
@Table(name = "availability")
public class AvailabilityEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "availability_id")
    private int availabilityId;
    @Enumerated(EnumType.STRING)
    private DayOfWeek day;
    private LocalTime startTime;
    private LocalTime endTime;
}
