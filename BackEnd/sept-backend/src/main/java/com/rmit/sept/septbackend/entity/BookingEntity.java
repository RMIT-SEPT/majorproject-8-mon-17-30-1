package com.rmit.sept.septbackend.entity;

import com.rmit.sept.septbackend.model.Status;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
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
    @Enumerated(EnumType.STRING)
    private Status status;

    public BookingEntity(ServiceWorkerEntity serviceWorker, CustomerEntity customer, LocalDateTime bookingTime) {
        this.serviceWorker = serviceWorker;
        this.customer = customer;
        this.bookingTime = bookingTime;
        this.createdTime = LocalDateTime.now();
        this.lastModifiedTime = LocalDateTime.now();
        this.status = Status.ACTIVE;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BookingEntity that = (BookingEntity) o;
        return bookingId == that.bookingId &&
                Objects.equals(serviceWorker, that.serviceWorker) &&
                Objects.equals(customer, that.customer) &&
                Objects.equals(bookingTime, that.bookingTime) &&
                status == that.status;
    }

    @Override
    public int hashCode() {
        return Objects.hash(bookingId, serviceWorker, customer, bookingTime, status);
    }
}
