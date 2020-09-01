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
    @GeneratedValue
    @Column(name = "service_id")
    private int serviceId;
    @ManyToOne
    @JoinColumn(name = "business_id", referencedColumnName = "business_id")
    private BusinessEntity business;
    private String serviceName;
    private int durationMinutes;
    @ManyToMany(mappedBy = "services")
    private Set<WorkerEntity> workers;

    public void addWorker(WorkerEntity workerEntity) {
        workers.add(workerEntity);
        workerEntity.getServices().add(this);
    }

    public void removeWorker(WorkerEntity workerEntity) {
        workers.remove(workerEntity);
        workerEntity.getServices().remove(this);
    }
}

