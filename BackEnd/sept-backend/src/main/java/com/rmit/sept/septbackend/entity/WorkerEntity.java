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
@Table(name = "worker")
public class WorkerEntity {
    @Id
    @GeneratedValue
    @Column(name = "worker_id")
    private int workerId;
    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "user_id")
    private UserEntity user;
    @ManyToMany
    @JoinTable(name = "service_worker",
            joinColumns = {@JoinColumn(name = "worker_id")},
            inverseJoinColumns = {@JoinColumn(name = "service_id")})
    private Set<ServiceEntity> services;

    public void addService(ServiceEntity serviceEntity) {
        services.add(serviceEntity);
        serviceEntity.getWorkers().add(this);
    }

    public void removeService(ServiceEntity serviceEntity) {
        services.remove(serviceEntity);
        serviceEntity.getWorkers().remove(this);
    }

}

