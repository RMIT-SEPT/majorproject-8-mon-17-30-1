package com.rmit.sept.septbackend.entity;

import com.rmit.sept.septbackend.model.Status;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;

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
    @GeneratedValue
    @Column(name = "booking_id")
    private int bookingId;
    @ManyToOne
    @JoinColumn(name = "service_id", referencedColumnName = "service_id")
    private ServiceEntity service;
    @ManyToOne
    @JoinColumn(name = "customer_id", referencedColumnName = "customer_id")
    private CustomerEntity customer;
    private LocalDate bookingTime;
    private LocalDate createdTime;
    private LocalDate lastModifiedTime;
    private Status status;
}
