package com.rmit.sept.septbackend.entity;

import lombok.*;

import javax.persistence.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode
@Entity
@Table(name = "service_worker")
public class ServiceWorkerEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "service_worker_id")
    private int serviceWorkerId;
    @ManyToOne
    @JoinColumn(name = "service_id", referencedColumnName = "service_id")
    private ServiceEntity service;
    @ManyToOne
    @JoinColumn(name = "worker_id", referencedColumnName = "worker_id")
    private WorkerEntity worker;

    public ServiceWorkerEntity(ServiceEntity service, WorkerEntity worker) {
        this.service = service;
        this.worker = worker;
    }
}
