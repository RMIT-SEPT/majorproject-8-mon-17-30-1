package com.rmit.sept.septbackend.entity;

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
@Table(name = "service_worker_availability")
public class ServiceWorkerAvailabilityEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "service_worker_availability_id")
    private int serviceWorkerAvailabilityId;
    @ManyToOne
    @JoinColumn(name = "service_worker_id", referencedColumnName = "service_worker_id")
    private ServiceWorkerEntity serviceWorker;
    @ManyToOne
    @JoinColumn(name = "availability_id", referencedColumnName = "availability_id")
    private AvailabilityEntity availability;
    /**
     * Inclusive effective start date
     */
    private LocalDate effectiveStartDate;

    /**
     * Inclusive effective end date
     */
    private LocalDate effectiveEndDate;

    public ServiceWorkerAvailabilityEntity(ServiceWorkerEntity serviceWorker, AvailabilityEntity availability, LocalDate effectiveStartDate, LocalDate effectiveEndDate) {
        this.serviceWorker = serviceWorker;
        this.availability = availability;
        this.effectiveStartDate = effectiveStartDate;
        this.effectiveEndDate = effectiveEndDate;
    }
}
