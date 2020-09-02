package com.rmit.sept.septbackend.entity;

import lombok.*;

import javax.persistence.*;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode
@Entity
@Table(name = "service")
public class ServiceEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "service_id")
    private int serviceId;
    @ManyToOne
    @JoinColumn(name = "business_id", referencedColumnName = "business_id")
    private BusinessEntity business;
    private String serviceName;
    private int durationMinutes;
    @ManyToMany
    @JoinTable(name = "service_worker",
            joinColumns = {@JoinColumn(name = "service_id")},
            inverseJoinColumns = {@JoinColumn(name = "worker_id")})
    private Set<WorkerEntity> workers;
}

