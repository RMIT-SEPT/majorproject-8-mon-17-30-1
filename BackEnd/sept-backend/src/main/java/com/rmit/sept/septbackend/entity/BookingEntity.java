package com.rmit.sept.septbackend.entity;

import com.rmit.sept.septbackend.model.Status;
import lombok.*;

import javax.persistence.*;
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
    @JoinColumn(name = "service_worker_id", referencedColumnName = "service_worker_id")
    private ServiceWorkerEntity serviceWorker;
    @ManyToOne
    @JoinColumn(name = "customer_id", referencedColumnName = "customer_id")
    private CustomerEntity customer;
    private LocalDateTime bookingTime;
    private LocalDateTime createdTime;
    private LocalDateTime lastModifiedTime;
    private Status status;

    public BookingEntity(ServiceWorkerEntity serviceWorker, CustomerEntity customer, LocalDateTime bookingTime, int bookingId) {
        this.serviceWorker = serviceWorker;
        this.customer = customer;
        this.bookingTime = bookingTime;
        this.bookingId = bookingId;
    }
}
