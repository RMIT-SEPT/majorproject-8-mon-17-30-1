package com.rmit.sept.septbackend.entity;

import com.rmit.sept.septbackend.model.Status;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode
@Entity
@Table(name = "booking")
public class BookingEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int bookingId;
    @ManyToOne
    @JoinColumn(name = "service_id", referencedColumnName = "service_id")
    private ServiceEntity service;
    @ManyToOne
    @JoinColumn(name = "customer_id", referencedColumnName = "customer_id")
    private CustomerEntity customer;
    private LocalDateTime bookingTime;
    private LocalDateTime createdTime;
    private LocalDateTime lastModifiedTime;
    private Status status;
}
