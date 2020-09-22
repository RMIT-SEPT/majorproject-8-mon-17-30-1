package com.rmit.sept.septbackend.entity;

import com.rmit.sept.septbackend.model.Status;
import lombok.*;

import javax.persistence.*;

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
    @Enumerated(EnumType.STRING)
    private Status status;

    public ServiceEntity(BusinessEntity business, String serviceName, int durationMinutes) {
        this.business = business;
        this.serviceName = serviceName;
        this.durationMinutes = durationMinutes;
        this.status = Status.ACTIVE;
    }
}

